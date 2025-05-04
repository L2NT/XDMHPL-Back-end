package com.example.XDMHPL_Back_end.Controller;

import java.util.List;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.XDMHPL_Back_end.DTO.LoginRequest;
import com.example.XDMHPL_Back_end.DTO.LoginResponse;
import com.example.XDMHPL_Back_end.DTO.UserDTO;
import com.example.XDMHPL_Back_end.Services.SessionService;
import com.example.XDMHPL_Back_end.Services.UserService;
import com.example.XDMHPL_Back_end.model.Session;
import com.example.XDMHPL_Back_end.model.Users;
import com.example.XDMHPL_Back_end.DTO.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // Cho phép CORS từ frontend
public class AuthController {

	@Autowired
    private UserService userService;
    
    @Autowired
    private SessionService sessionService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest servletRequest) {
        try {
            // 1. Xác thực tài khoản
            Users user = userService.loginValidate(
                    loginRequest.getUserIdentifier(),
                    loginRequest.getPassword(),
                    loginRequest.getRole()
            );

            if (user == null) {
                // Trả về lỗi 401 nếu tài khoản không tồn tại hoặc mật khẩu sai
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Tài khoản không tồn tại hoặc mật khẩu không chính xác."));
            }
            
            if (!user.getRole().equalsIgnoreCase(loginRequest.getRole())) {
                // Trả về lỗi 403 nếu role của người dùng không khớp với role được yêu cầu
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("Bạn không có quyền truy cập với vai trò này."));
            }
            
            // 2. Lấy deviceInfo (từ client hoặc User‑Agent header)
            String deviceInfo = loginRequest.getDeviceInfo() != null
                    ? loginRequest.getDeviceInfo()
                    : servletRequest.getHeader("User-Agent");

            // 3. Kiểm tra session đang hoạt động
            List<Session> activeSessions = sessionService.hasActiveSession(user);

            // Kiểm tra session trùng khớp với deviceInfo
            for (Session session : activeSessions) {
                if (deviceInfo.equals(session.getDeviceInfo()) && !session.isExpired()) {
                    // Nếu đã có session hợp lệ cho thiết bị này, trả về session hiện tại
                    LoginResponse resp = new LoginResponse();
                    resp.setSessionId(session.getSessionID());
                    resp.setUser(UserDTO.fromEntity(user));
                    resp.setMessage("Đăng nhập thành công với session hiện tại.");
                    return ResponseEntity.ok(resp);
                }
            }

            // 4. Xóa các session đã hết hạn
            sessionService.cleanExpiredSessions(user);

            // 5. Tạo session mới
            String sessionId = sessionService.createSession(user.getUserID(), deviceInfo);
            UserDTO userDTO = UserDTO.fromEntity(user);

            // 6. Trả về sessionId kèm thông tin cơ bản của user
            LoginResponse resp = new LoginResponse();
            resp.setSessionId(sessionId);
            resp.setUser(userDTO);
            resp.setMessage("Đăng nhập thành công. Đã tạo session mới.");
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            // Log lỗi và trả về lỗi hệ thống
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Lỗi hệ thống: " + e.getMessage()));
        }
    }

    @GetMapping("/check-session/{userID}")
    public ResponseEntity<?> checkUserSession(@PathVariable int userID) {
        try {
        	Users user = userService.getUserById(userID);
            // Kiểm tra xem user có session trong DB không
            List<Session> hasActiveSession = sessionService.hasActiveSession(user);

            if (!hasActiveSession.isEmpty()) {
                return ResponseEntity.ok(new ErrorResponse("Người dùng đã có session đang hoạt động."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Người dùng không có session đang hoạt động."));
            }
        } catch (Exception e) {
            // Log lỗi và trả về 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    @PutMapping("/update-session/{userID}")
    public ResponseEntity<?> updateSessionID(@PathVariable int userID, @RequestParam String newSessionID) {
        try {
            // Gọi UserService để cập nhật sessionID
            Users updatedUser = userService.updateSessionID(userID, newSessionID);

            // Chuyển đổi user entity thành DTO để trả về
            UserDTO userDTO = UserDTO.fromEntity(updatedUser);

            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            // Nếu không tìm thấy người dùng, trả về lỗi 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Người dùng không tồn tại."));
        } catch (Exception e) {
            // Log lỗi và trả về lỗi hệ thống
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("SessionID") String sessionId) {
        sessionService.logout(sessionId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/email-check/{email}")
	private Users emailCheck(@PathVariable String email) {
		return userService.getUserByEmail(email);
	}
	
	@GetMapping("/username-check/{userName}")
	private Users usersnameCheck(@PathVariable String userName){
		return userService.getUserByUsername(userName);
	}
	
	@GetMapping("/phone-check/{phoneNumber}")
	private Users phoneCheck(@PathVariable String phoneNumber) {
	    return userService.getUserByPhoneNumber(phoneNumber);
	}


    @GetMapping("/current-user/{userID}")
    public ResponseEntity<?> getCurrentUser( @PathVariable int userID) {
        try {
            return ResponseEntity.ok(userService.getCurrentUser(userID));

        } catch (Exception e) {
            // 5. Log lỗi và trả về 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Lỗi hệ thống: " + e.getMessage()));
        }
    }
}
