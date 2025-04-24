package com.example.XDMHPL_Back_end.DTO;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {
    private Integer friendID;
    private String status;
    private Date createdAt;

    // Thông tin chi tiết về người gửi lời mời kết bạn
    private Integer userID;
    private String fullName;
    private String username;
    private String email;
}
