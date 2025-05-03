package com.example.XDMHPL_Back_end.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.XDMHPL_Back_end.DTO.UserDTO;
import com.example.XDMHPL_Back_end.Services.UserService;
import com.example.XDMHPL_Back_end.model.Users;
import org.springframework.util.StringUtils;
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService usersService;
	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/avatars"; // Thư mục lưu trữ avatar

	@GetMapping
	public List<UserDTO> getUsers() {
		List<Users> users = usersService.getAllUser();
		return users.stream().map(UserDTO::fromEntity).toList();
	}

	@PostMapping("/{id}/upload-avatar")
	public ResponseEntity<String> uploadAvatar(@PathVariable Integer id, @RequestParam("avatar") MultipartFile avatar) {
		try {
			Path avatarsDir = Paths.get(UPLOAD_DIR);
			if (!Files.exists(avatarsDir)) {
				Files.createDirectories(avatarsDir);
			}

			String contentType = avatar.getContentType();
			if (contentType == null || !contentType.startsWith("image")) {
				return ResponseEntity.status(400).body("Tệp tin phải là hình ảnh.");
			}

			String fileExtension = StringUtils.getFilenameExtension(avatar.getOriginalFilename());
			String newFileName = UUID.randomUUID().toString() + "." + fileExtension;

			Path filePath = avatarsDir.resolve(newFileName);
			Files.copy(avatar.getInputStream(), filePath);

			// Update avatar URL vào database
			String avatarUrl = "/avatars/" + newFileName; // Quan trọng, chỉ lưu phần URL
			usersService.updateUserAvatar(id, avatarUrl);

			return ResponseEntity.ok("Uploaded successfully: " + avatarUrl);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Failed to upload: " + e.getMessage());
		}
	}

	@PostMapping("/{id}/upload-cover")
	public ResponseEntity<String> uploadCoverPhoto(@PathVariable Integer id, @RequestParam("coverPhoto") MultipartFile coverPhoto) {
		try {
			// Thư mục lưu ảnh bìa
			Path coverPhotosDir = Paths.get("src/main/resources/static/uploads/covers");
			if (!Files.exists(coverPhotosDir)) {
				Files.createDirectories(coverPhotosDir);
			}
	
			// Kiểm tra file upload có đúng loại ảnh không
			String contentType = coverPhoto.getContentType();
			if (contentType == null || !contentType.startsWith("image")) {
				return ResponseEntity.status(400).body("Tệp tin phải là hình ảnh.");
			}

			// Lưu file
			String fileName = coverPhoto.getOriginalFilename();
			Path filePath = coverPhotosDir.resolve(fileName);
			Files.copy(coverPhoto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	
			// Cập nhật đường dẫn ảnh bìa vào database
			String relativeFilePath = "/covers/" + fileName;
			usersService.updateUserCoverPhoto(id, relativeFilePath);
	
			return ResponseEntity.ok("Cover photo uploaded successfully: " + filePath.toString());
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Failed to upload cover photo: " + e.getMessage());
		}
	}



	// API để cập nhật tiểu sử (Bio) của người dùng
    @PutMapping("/{userID}/bio")
    public String updateBio(@PathVariable int userID, @RequestBody String newBio) {
		String sanitizedBio = newBio.replace("\"", "");
        Users updatedUser = usersService.updateBio(userID, sanitizedBio);
        if (updatedUser != null) {
            return "Tiểu sử đã được cập nhật thành công.";
        } else {
            return "Không tìm thấy người dùng với ID: " + userID;
        }
    }

	// API để cập nhật thông tin cá nhân của người dùng
    @PutMapping("/{userID}/profile")
    public String updateProfile(@PathVariable int userID, @RequestBody Users userDetails) {
        Users updatedUser = usersService.updateProfile(userID, userDetails);
        if (updatedUser != null) {
            return "Thông tin người dùng đã được cập nhật thành công.";
        } else {
            return "Không tìm thấy người dùng với ID: " + userID;
        }
    }
	
	@PostMapping("/create-user")
	public Users addUser(@RequestBody Users user) {
	    return usersService.createUser(
	        user.getFullName(), 
	        user.getUserName(), 
	        user.getPassword(),
	        user.getEmail(), 
	        user.getAvatar(), 
	        user.getPhoneNumber(), 
	        user.getDateOfBirth(), 
	        user.getGender(), 
	        user.getCoverPhotoURL(), 
	        user.getSessionID(),
	        user.getRole()
	        );
	}
	
	
	@GetMapping("/{id}")
	public Users getUserById(@PathVariable Integer id) {
		return usersService.getUserById(id);
	}
	
	@GetMapping("/find/{id}")
	public Object finUsersByID(@PathVariable int id) {
		return usersService.findUserByID(id);
	}
	
	@PutMapping("/{id}/hide")
	public ResponseEntity<?> hideUser(@PathVariable int id) {
		boolean result = usersService.hideUserById(id);
		if (result) {
			return ResponseEntity.ok("Ẩn người dùng thành công");
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@PutMapping("/{id}/role")
	public ResponseEntity<Users> updateUserRole(@PathVariable int id, @RequestBody Map<String, String> payload) {
		String role = payload.get("role");
		if (role == null || role.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Users updatedUser = usersService.updateUserRole(id, role);
		return ResponseEntity.ok(updatedUser);
	}
}
