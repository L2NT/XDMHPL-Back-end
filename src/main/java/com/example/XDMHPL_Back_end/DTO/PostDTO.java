package com.example.XDMHPL_Back_end.DTO;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.XDMHPL_Back_end.model.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private int postID;
    private Date creationDate;
    private String type;
    private int userID;
    private String content;
    private int priorityScore;
    private List<PostMediaDTO> mediaList;
    private UserDTO user; // Thông tin người đăng bài

    // Constructors, getters, setters
    
    // Phương thức chuyển đổi từ Entity sang DTO
    public static PostDTO fromEntity(Post post) {
        PostDTO dto = new PostDTO();
        dto.setPostID(post.getPostID());
        dto.setCreationDate(post.getCreationDate());
        dto.setType(post.getType());
        dto.setUserID(post.getUserID());
        dto.setContent(post.getContent());
        dto.setPriorityScore(post.getPriorityScore());
        
        // Chuyển đổi danh sách media
        if (post.getMediaList() != null) {
            List<PostMediaDTO> mediaDTOs = post.getMediaList().stream()
                .map(PostMediaDTO::fromEntity)
                .collect(Collectors.toList());
            dto.setMediaList(mediaDTOs);
        }
        
        return dto;
    }
}