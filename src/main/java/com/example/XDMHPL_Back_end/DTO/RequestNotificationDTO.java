package com.example.XDMHPL_Back_end.DTO;

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
    private Boolean isOnline;
}
