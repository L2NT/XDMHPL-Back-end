package com.example.XDMHPL_Back_end.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.XDMHPL_Back_end.Services.CommentService;
import com.example.XDMHPL_Back_end.Services.NotificationService;
import com.example.XDMHPL_Back_end.Services.PostService;
import com.example.XDMHPL_Back_end.Services.UserService;
import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.MessageModel;
import com.example.XDMHPL_Back_end.model.Notification;
import com.example.XDMHPL_Back_end.model.NotificationStatus;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserService userService; 

    @Autowired
    private PostService postService; 

    @Autowired
    private CommentService commentService;
    
    /**
     * Lấy tất cả thông báo của người dùng hiện tại
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestParam(required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Notification> notifications = notificationService.getAllNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Lấy thông báo chưa đọc của người dùng
     */
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@RequestParam(required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Notification> unreadNotifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(unreadNotifications);
    }
    
    /**
     * Tạo thông báo mới
     */
    // @PostMapping
    // public ResponseEntity<Notification> createNotification(@RequestBody Map<String, Object> payload) {
    //     try {
    //         Users user = userService.getUserById((Integer) payload.get("userId"));
    //         Users sender = userService.getUserById((Integer) payload.get("senderId"));
            
    //         NotificationStatus type = NotificationStatus.valueOf((String) payload.get("type"));
            
    //         Post post = null;
    //         Comment comment = null;
    //         Message message = null;
            
    //         if (payload.containsKey("postId")) {
    //             post = postService.getPostByID((Integer) payload.get("postId"));
    //         }
            
    //         if (payload.containsKey("commentId")) {
    //             comment = commentService.getCommentById((Integer) payload.get("commentId"));
    //         }
            

    //         if (payload.containsKey("messageId")) {
    //         }
            
    //         String content = (String) payload.get("content");
            
    //         // Tạo thông báo mới
    //         Notification notification = notificationService.createNotification(
    //                 user, sender, type, post, comment, message, content);
            
    //         return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }
    
    /**
     * Đánh dấu một thông báo cụ thể là đã đọc
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable int notificationId) {
        Notification notification = notificationService.markAsRead(notificationId);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Đánh dấu tất cả thông báo của một người dùng là đã đọc
     */
    // @PutMapping("/read-all")
    // public ResponseEntity<Map<String, Object>> markAllAsRead(@RequestParam int userId) {
    //     int count = notificationService.markAllAsRead(userId);
    //     Map<String, Object> response = new HashMap<>();
    //     response.put("message", "Đã đánh dấu " + count + " thông báo là đã đọc");
    //     response.put("count", count);
    //     return ResponseEntity.ok(response);
    // }
    
    /**
     * Xóa một thông báo cụ thể
     */
    // @DeleteMapping("/{notificationId}")
    // public ResponseEntity<Void> deleteNotification(@PathVariable int notificationId) {
    //     try {
    //         // Giả định bạn đã thêm phương thức deleteById trong service
    //         notificationService.deleteNotification(notificationId);
    //         return ResponseEntity.noContent().build();
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }
}
