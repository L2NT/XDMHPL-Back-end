package com.example.XDMHPL_Back_end.Services;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.Repositories.CommentRepository;
import com.example.XDMHPL_Back_end.Repositories.PostRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;

@Service
public class CommentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;
    public void createComment(Comment comment, int postId, int userId) {
        // Tìm bài đăng theo ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài đăng với ID: " + postId));
    
        // Tìm người dùng theo ID
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId));

        comment.setUser(user);
        comment.setContent(comment.getContent());
        comment.setPost(post);
        comment.setCreationDate(new Date());
        Comment savedComment=commentRepository.save(comment);
        post.getComments().add(savedComment);
        postRepository.save(post);
    }


    public Comment getCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bình luận với ID: " + commentId));
    }
}
