package com.example.XDMHPL_Back_end.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.XDMHPL_Back_end.DTO.OnlineStatusDTO;
import com.example.XDMHPL_Back_end.DTO.UserStatusDTO;
import com.example.XDMHPL_Back_end.Services.FriendService;
import com.example.XDMHPL_Back_end.Services.UserService;
import com.example.XDMHPL_Back_end.model.Users;

@Controller
@RequestMapping("/api/presence")
public class UserPresenceController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;
    
    // Constructor...
    
    @MessageMapping("/status/online")
    public void userOnline(Principal principal, @Payload UserStatusDTO statusDTO) {
        int userId = statusDTO.getUserId();
        System.out.println("User " + userId + " is online");

        // Cập nhật trạng thái online
        userService.updateOnlineStatus(userId, true);
        
        // Thông báo cho tất cả bạn bè
        notifyFriendsAboutStatus(userId, true);
    }
    
    @MessageMapping("/status/offline")
    public void userOffline(Principal principal, @Payload UserStatusDTO statusDTO) {
        int userId = statusDTO.getUserId();
        System.out.println("User " + userId + " is offline");
        // Cập nhật trạng thái offline
        userService.updateOnlineStatus(userId, false);
        
        // Thông báo cho tất cả bạn bè
        notifyFriendsAboutStatus(userId, false);
    }
    
    private void notifyFriendsAboutStatus(int userId, boolean isOnline) {
        List<Users> acceptedFriends = friendService.getAcceptedFriends(userId);
        
        OnlineStatusDTO statusUpdate = new OnlineStatusDTO(userId, isOnline);
        
        for (Users friend : acceptedFriends) {
            messagingTemplate.convertAndSendToUser(
                friend.getUserName(), 
                "/queue/friend-status", 
                statusUpdate
            );
        }
    }

    @MessageMapping("/status/heartbeat")
    public void userHeartbeat(@Payload UserStatusDTO statusDTO) {
        int userId = statusDTO.getUserId();
        // Cập nhật thời gian hoạt động cuối cùng
        userService.updateUserActivity(userId);
    }
}