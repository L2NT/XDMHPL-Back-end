package com.example.XDMHPL_Back_end.DTO;

import com.example.XDMHPL_Back_end.model.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private int commentID;
    private String content;
    private int userID;
    private int postID;
    private String creationDate;

    // Constructors, getters, setters

    // Phương thức chuyển đổi từ Entity sang DTO
    public static CommentDTO fromEntity(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setCommentID(comment.getCommentID());
        dto.setUserID(comment.getUser().getUserID());
        dto.setPostID(comment.getPost().getPostID());
        dto.setCreationDate(comment.getCreationDate().toString()); // Chuyển đổi Date thành String
        return dto;
    }
}
