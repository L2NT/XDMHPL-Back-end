package com.example.XDMHPL_Back_end.DTO;

import com.example.XDMHPL_Back_end.model.PostShare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareDTO {
    private int postID; 
    private int userID;
    private Integer originalPostID; // ID của bài đăng gốc
    private Integer parentShareID;  // ID của bài đăng share trực tiếp

    // Phương thức chuyển đổi từ PostShare sang ShareDTO
    public static ShareDTO fromEntity(PostShare postShare) {
        return new ShareDTO(
            postShare.getPostID(),
            postShare.getUser().getUserID(),
            postShare.getOriginalPostID(),
            postShare.getParentShareID()
        );
    }
}
