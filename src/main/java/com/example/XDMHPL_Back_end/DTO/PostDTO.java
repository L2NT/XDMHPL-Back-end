package com.example.XDMHPL_Back_end.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.PostShare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private int postID;
    private Date creationDate;
    private int userID;
    private String content;
    private int priorityScore;
    private int likeCount;
    private int commentCount;
    private int shareCount;
    private String postType;
    private int hide;
    
    // Thêm các trường cho PostShare
    private Integer originalPostID; // Cho post_type=SHARE
    private Integer parentShareID; // Cho post_type=SHARE

    private List<PostMediaDTO> mediaList;
    private List<CommentDTO> comments;
    private List<LikeDTO> likes;
    private List<ShareDTO> shares;

    // Phương thức chuyển đổi từ Entity sang DTO

    public static PostDTO fromEntity(Post post) {
        PostDTO dto = new PostDTO();
        dto.setPostID(post.getPostID());
        dto.setCreationDate(post.getCreationDate());
        dto.setUserID(post.getUser().getUserID());
        dto.setContent(post.getContent());
        dto.setPriorityScore(post.getPriorityScore());
        dto.setHide(post.getHide());
        
        // Đặt loại post
        if (post instanceof PostShare) {
            dto.setPostType("SHARE");
            PostShare sharePost = (PostShare) post;
            dto.setOriginalPostID(sharePost.getOriginalPostID());
            dto.setParentShareID(sharePost.getParentShareID());
        } else {
            dto.setPostType("POST");
        }

        // Chuyển đổi danh sách media
        if (post.getMediaList() != null) {
            List<PostMediaDTO> mediaDTOs = post.getMediaList().stream()
                    .map(PostMediaDTO::fromEntity)
                    .collect(Collectors.toList());
            dto.setMediaList(mediaDTOs);
        }

        // Đếm số lượng like
        if (post.getLikes() != null) {
            dto.setLikeCount(post.getLikes().size());
            List<LikeDTO> likeDTOs = post.getLikes().stream()
                    .map(LikeDTO::fromEntity)
                    .collect(Collectors.toList());
            dto.setLikes(likeDTOs);
        }

        // Đếm số lượng comment
        if (post.getComments() != null) {
            dto.setCommentCount(post.getComments().size());
            List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(CommentDTO::fromEntity)
                    .collect(Collectors.toList());
            dto.setComments(commentDTOs);
        }

        // Xử lý shares dựa trên loại bài đăng
        if (post instanceof PostShare) {
            // Nếu là post share, chỉ hiển thị danh sách các bài share trực tiếp từ nó
            if (post.getDirectSharedPosts() != null) {
                dto.setShareCount(post.getDirectSharedPosts().size());

                List<ShareDTO> shareDTOs = post.getDirectSharedPosts().stream()
                        .map(ShareDTO::fromEntity)
                        .collect(Collectors.toList());

                dto.setShares(shareDTOs);
            } else {
                dto.setShareCount(0);
                dto.setShares(new ArrayList<>());
            }
        } else {
            // Nếu là post gốc, hiển thị danh sách tất cả các bài share có nguồn gốc từ nó
            if (post.getAllSharedPosts() != null) {
                dto.setShareCount(post.getAllSharedPosts().size());

                List<ShareDTO> shareDTOs = post.getAllSharedPosts().stream()
                        .map(ShareDTO::fromEntity)
                        .collect(Collectors.toList());

                dto.setShares(shareDTOs);
            } else {
                dto.setShareCount(0);
                dto.setShares(new ArrayList<>());
            }
        }

        return dto;
    }
}