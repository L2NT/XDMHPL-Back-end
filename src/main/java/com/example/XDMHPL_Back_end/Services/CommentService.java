package com.example.XDMHPL_Back_end.Services;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.DTO.CommentDTO;
import com.example.XDMHPL_Back_end.DTO.NotificationDTO;
import com.example.XDMHPL_Back_end.DTO.RequestNotificationDTO;
import com.example.XDMHPL_Back_end.Repositories.CommentRepository;
import com.example.XDMHPL_Back_end.Repositories.PostRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.NotificationStatus;
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

    public RequestNotificationDTO createComment(Comment comment, int postId, int userId) {
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
       
        return new RequestNotificationDTO(savedComment.getCommentID(), null, post.getPostID(), userId, post.getUser().getUserID());
    }


    public Comment getCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bình luận với ID: " + commentId));
    }

    public Comment updateComment(Comment comment, int commentId) {
        Comment existingComment = getCommentById(commentId);
        existingComment.setContent(comment.getContent());
        return commentRepository.save(existingComment);
    }

    public Comment deleteComment(int commentId) {
        Comment existingComment = getCommentById(commentId);
        commentRepository.delete(existingComment);
        return existingComment;
    }
}
