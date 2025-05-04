package com.example.XDMHPL_Back_end.DTO;

import com.example.XDMHPL_Back_end.model.Notification;
import com.example.XDMHPL_Back_end.model.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private int notificationID;
    private String Content;
    private String creationDate;
    private NotificationStatus Type;
    private int CommentID;
    private int MessageID;
    private int PostID;
    private int SenderID;
    private int UserID;
    private int isReadFlag;
    private Boolean isOnline;

    // Constructors, getters, setters

    // Phương thức chuyển đổi từ Entity sang DTO
    public static NotificationDTO fromEntity(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationID(notification.getNotificationID());
        dto.setContent(notification.getContent());
        dto.setType(notification.getType());
        dto.setUserID(notification.getUser().getUserID());
        dto.setSenderID(notification.getSender().getUserID());
        dto.setPostID(notification.getPost() != null ? notification.getPost().getPostID() : 0);
        dto.setMessageID(notification.getMessage() != null ? notification.getMessage().getMessageID() : 0);
        dto.setCommentID(notification.getComment() != null ? notification.getComment().getCommentID() : 0);
        dto.setCreationDate(notification.getCreatedAt().toString()); // Chuyển đổi Date thành String
        dto.setIsReadFlag(notification.getIsReadFlag());
        dto.setIsOnline(notification.getUser().getIsOnline());
        return dto;
    }
}
