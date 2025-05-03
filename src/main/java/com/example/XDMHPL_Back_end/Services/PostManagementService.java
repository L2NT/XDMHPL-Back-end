package com.example.XDMHPL_Back_end.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.DTO.PostDTO;
import com.example.XDMHPL_Back_end.Repositories.PostRepository;
import com.example.XDMHPL_Back_end.model.Post;


@Service
public class PostManagementService {
	private final PostRepository postRepository;
	 @Autowired
    public PostManagementService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
	 public boolean updatePostVisibility(int postID, int hide) {
        return postRepository.findByPostID(postID)
            .map(post -> {
                post.setHide(hide);
                return postRepository.save(post); // Lưu lại thay đổi
            })
            .orElse(null) != null; // Trả về null nếu không tìm thấy bài viết
    }
	 public boolean deletePost(int postID) {
	    return postRepository.findByPostID(postID).map(post -> {
	        postRepository.delete(post); 
	        return true; 
	    }).orElse(false);
	}
}