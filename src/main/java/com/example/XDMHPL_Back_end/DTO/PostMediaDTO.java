package com.example.XDMHPL_Back_end.DTO;
import com.example.XDMHPL_Back_end.model.PostMedia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostMediaDTO {
    private int postMediaID;
    private String type;
    private String mediaURL;
    private int postID;

    // Constructors, getters, setters
    
    // Phương thức chuyển đổi từ Entity sang DTO
    public static PostMediaDTO fromEntity(PostMedia media) {
        PostMediaDTO dto = new PostMediaDTO();
        dto.setPostMediaID(media.getPostMediaID());
        dto.setType(media.getType());
        dto.setMediaURL(media.getMediaURL());
        dto.setPostID(media.getPost().getPostID());
        return dto;
    }
    
    // Phương thức chuyển đổi từ DTO sang Entity
    public PostMedia toEntity() {
        PostMedia media = new PostMedia();
        media.setPostMediaID(this.postMediaID);
        media.setType(this.type);
        media.setMediaURL(this.mediaURL);
        // Post sẽ được set sau khi tạo object này
        return media;
    }
}