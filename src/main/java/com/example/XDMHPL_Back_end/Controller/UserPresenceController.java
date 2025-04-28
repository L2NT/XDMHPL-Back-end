package com.example.XDMHPL_Back_end.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.example.XDMHPL_Back_end.DTO.OnlineStatusDTO;
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

    // Constructor...

    @MessageMapping("/status/online")
    public void userOnline( @Payload UserStatusDTO statusDTO) {
        int userId = statusDTO.getUserId();
        System.out.println("User " + userId + " is online");

        // Cập nhật trạng thái online
        userService.updateOnlineStatus(userId, true);

        // Thông báo cho tất cả bạn bè
        notifyFriendsAboutStatus(userId, true);
    }

    @MessageMapping("/status/offline")
    public void userOffline( @Payload UserStatusDTO statusDTO) {
        int userId = statusDTO.getUserId();
        System.out.println("User " + userId + " is offline");
        // Cập nhật trạng thái offline
        userService.updateOnlineStatus(userId, false);

        // Thông báo cho tất cả bạn bè
        notifyFriendsAboutStatus(userId, false);
    }

    private void notifyFriendsAboutStatus(int userId, boolean isOnline) {
        List<Users> acceptedFriends = friendService.getAcceptedFriends(userId);
        
        System.out.println("Found " + acceptedFriends.size() + " friends to notify");
        
        OnlineStatusDTO statusUpdate = new OnlineStatusDTO(userId, isOnline);
        
        for (Users friend : acceptedFriends) {
            System.out.println("About to send to: /user/" + friend.getUserID() + "/statususer");
            try {
                // In thêm chi tiết trạng thái để debug
                System.out.println("Status update: userId=" + statusUpdate.getUserId() + ", online=" + statusUpdate.isOnline());
                
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(friend.getUserID()),
                        "/queue/statususer",
                        statusUpdate);
                System.out.println("Message sent successfully");
            } catch (Exception e) {
                System.err.println("Error sending message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // @MessageMapping("/status/heartbeat")
    // public void userHeartbeat(@Payload UserStatusDTO statusDTO) {
    // int userId = statusDTO.getUserId();
    // // Cập nhật thời gian hoạt động cuối cùng
    // userService.updateUserActivity(userId);
    // }
}