package com.example.XDMHPL_Back_end.DTO;

import com.example.XDMHPL_Back_end.model.Notification;
import com.example.XDMHPL_Back_end.model.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestNotificationDTO {
    private Integer CommentID;
    private Integer MessageID;
    private Integer PostID;
    private int SenderID;
    private int UserID;
}
