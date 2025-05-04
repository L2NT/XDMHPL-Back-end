package com.example.XDMHPL_Back_end.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.XDMHPL_Back_end.DTO.OnlineStatusDTO;
import com.example.XDMHPL_Back_end.DTO.OnlineUsersListDTO;
import com.example.XDMHPL_Back_end.DTO.UserStatusDTO;
import com.example.XDMHPL_Back_end.Services.FriendService;
import com.example.XDMHPL_Back_end.Services.UserService;
import com.example.XDMHPL_Back_end.model.Users;

@RestController
public class UserPresenceController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    // Set để lưu trữ danh sách người dùng đang online
    private final Set<Integer> onlineUsers = ConcurrentHashMap.newKeySet();

    // Constructor...

    @MessageMapping("/status/online")
    public void userOnline(@Payload UserStatusDTO statusDTO) {
        int userId = statusDTO.getUserId();
        System.out.println("User " + userId + " is online");

        // Thêm vào danh sách online
        onlineUsers.add(userId);

        // Cập nhật trạng thái online trong database
        userService.updateOnlineStatus(userId, true);

        // Thông báo cho tất cả bạn bè
        notifyFriendsAboutStatus(userId, true);
    }


    @MessageMapping("/status/offline")
    public void userOffline(@Payload UserStatusDTO statusDTO) {
        int userId = statusDTO.getUserId();
        System.out.println("User " + userId + " is offline");

        // Xóa khỏi danh sách online
        onlineUsers.remove(userId);

        // Cập nhật trạng thái offline trong database
        userService.updateOnlineStatus(userId, false);

        // Thông báo cho tất cả bạn bè
        notifyFriendsAboutStatus(userId, false);
    }

    /**
     * Xử lý yêu cầu lấy danh sách người dùng online
     */
    @MessageMapping("/status/get-online-users")
    public void getOnlineUsers(@Payload UserStatusDTO request, Principal principal) {
        int userId = request.getUserId();
        String username = principal.getName();
        System.out.println("User " + username + " requested online users list");

        try {
            // Lấy danh sách bạn bè đã chấp nhận
            List<Users> acceptedFriends = friendService.getUserOnline(userId);
            System.out.println("Sent accepted friends list: " + acceptedFriends.size() + " friends");
    
            // Lọc ra bạn bè đang online và tạo danh sách OnlineStatusDTO
            List<OnlineStatusDTO> onlineFriends = acceptedFriends.stream()
                    .filter(friend -> onlineUsers.contains(friend.getUserID()))
                    .map(friend -> new OnlineStatusDTO(friend.getUserID(), true)) // isOnline = true vì đã lọc
                    .collect(Collectors.toList());
    
            // Gửi đến người dùng yêu cầu
            messagingTemplate.convertAndSend(
                    "/topic/status/" + username,
                    new OnlineUsersListDTO(onlineFriends));
            System.out.println("Sent online friends list: " + onlineFriends.size() + " friends");
        } catch (Exception e) {
            System.err.println("Error sending online users list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void notifyFriendsAboutStatus(int userId, boolean isOnline) {
        List<Users> acceptedFriends = friendService.getUserOnline(userId);

        System.out.println("Found " + acceptedFriends.size() + " friends to notify");

        OnlineStatusDTO statusUpdate = new OnlineStatusDTO(userId, isOnline);

        for (Users friend : acceptedFriends) {
            System.out.println("About to send to user: " + friend.getUserID());
            try {
                // SỬA ĐƯỜNG DẪN Ở ĐÂY
                // messagingTemplate.convertAndSendToUser(
                //         friend.getUserName(),
                //         "/queue/statususer", // Không cần thêm /user/ vì framework sẽ tự thêm
                //         statusUpdate);

                messagingTemplate.convertAndSend(
                        "/topic/status/" + friend.getUserName(),
                        statusUpdate);
                System.out.println("Message sent successfully");
            } catch (Exception e) {
                System.err.println("Error sending message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Xử lý khi người dùng ngắt kết nối
     */
    @EventListener
public void handleSessionDisconnect(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    Principal userPrincipal = headerAccessor.getUser();
    
    if (userPrincipal != null) {
        String username = userPrincipal.getName();
        System.out.println("User disconnected: " + username);
        
        try {
            // Tìm userId dựa trên username
            Users user = userService.getUserByUsername(username);
            if (user != null) {
                int userIdInt = user.getUserID();
                
                // Xóa khỏi danh sách online
                onlineUsers.remove(userIdInt);
                
                // Cập nhật trạng thái offline trong database
                userService.updateOnlineStatus(userIdInt, false);
                
                // Thông báo cho bạn bè
                notifyFriendsAboutStatus(userIdInt, false);
                
                System.out.println("User " + userIdInt + " disconnected");
            } else {
                System.out.println("Không thể tìm thấy user với username: " + username);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xử lý ngắt kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
}