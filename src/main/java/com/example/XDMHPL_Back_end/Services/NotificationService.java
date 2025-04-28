package com.example.XDMHPL_Back_end.Services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.Repositories.NotificationRepository;
import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.Message;
import com.example.XDMHPL_Back_end.model.Notification;
import com.example.XDMHPL_Back_end.model.NotificationStatus;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;


@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    
    public Notification createNotification(Users user, Users sender, NotificationStatus type, 
                                           Post post, Comment comment, Message message, String content) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setSender(sender);
        notification.setType(type);
        notification.setPost(post);
        notification.setComment(comment);
        notification.setMessage(message);
        notification.setContent(content);
        notification.setCreatedAt(new Date());
        notification.setIsReadFlag(0); // 0 = chưa đọc
        
        return notificationRepository.save(notification);
    }
    
    public Notification markAsRead(int notificationID) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationID);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setIsReadFlag(1); // 1 = đã đọc
            return notificationRepository.save(notification);
        }
        return null;
    }
    

    public int markAllAsRead(int userID) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadStatus(userID, 0);
        for (Notification notification : unreadNotifications) {
            notification.setIsReadFlag(1); // 1 = đã đọc
            notificationRepository.save(notification);
        }
        return unreadNotifications.size();
    }
    
    public List<Notification> getUnreadNotifications(int userID) {
        return notificationRepository.findByUserIdAndReadStatus(userID, 0);
    }
    
    public List<Notification> getAllNotifications(int userID) {
        return notificationRepository.findByUserId(userID);
    }
}
