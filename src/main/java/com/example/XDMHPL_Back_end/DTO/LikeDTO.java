package com.example.XDMHPL_Back_end.DTO;

import com.example.XDMHPL_Back_end.model.Like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {
    private int id;
    private int postId;
    private int userId;

    public static LikeDTO fromEntity(Like like) {
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setId(like.getLikeID());
        likeDTO.setPostId(like.getPost().getPostID());
        likeDTO.setUserId(like.getUser().getUserID());
        return likeDTO;
    }
}