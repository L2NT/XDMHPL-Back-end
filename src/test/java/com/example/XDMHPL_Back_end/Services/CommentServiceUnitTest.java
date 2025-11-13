package com.example.XDMHPL_Back_end.Services;

import com.example.XDMHPL_Back_end.DTO.CommentDTO;
import com.example.XDMHPL_Back_end.DTO.NotificationDTO;
import com.example.XDMHPL_Back_end.Repositories.CommentRepository;
import com.example.XDMHPL_Back_end.Repositories.PostRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;
import com.example.XDMHPL_Back_end.model.NotificationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test cho CommentService (đã map theo code thực tế trong repo)
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_shouldReturnNotificationDTO() {
        // Arrange
        CommentDTO dto = new CommentDTO();
        dto.setContent("Bình luận test");

        Users user = new Users();
        user.setUserID(1);
        user.setUserName("tester");

        Users postOwner = new Users();
        postOwner.setUserID(2);

        Post post = new Post();
        post.setPostID(10);
        post.setUser(postOwner);
        post.setComments(new ArrayList<>());

        Comment savedComment = new Comment();
        savedComment.setCommentID(99);
        savedComment.setContent("Bình luận test");
        savedComment.setUser(user);
        savedComment.setPost(post);
        savedComment.setCreationDate(new Date());

        NotificationDTO mockNotification = new NotificationDTO();

        when(postRepository.findById(10)).thenReturn(Optional.of(post));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(notificationService.createNotification(
                eq(post.getUser().getUserID()),
                eq(user.getUserID()),
                eq(NotificationStatus.COMMENT),
                eq(post.getPostID()),
                eq(savedComment.getCommentID()),
                isNull(),
                anyString()
        )).thenReturn(mockNotification);

        // Act
        NotificationDTO result = commentService.createComment(dto, 10, 1);

        // Assert: không phụ thuộc vào cấu trúc NotificationDTO
        assertNotNull(result, "NotificationDTO trả về không được null");
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(notificationService, times(1)).createNotification(
                anyInt(), anyInt(), eq(NotificationStatus.COMMENT),
                anyInt(), anyInt(), isNull(), anyString());
    }

    @Test
    void getCommentById_shouldThrowExceptionWhenNotFound() {
        when(commentRepository.findById(999)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> commentService.getCommentById(999));
        assertTrue(ex.getMessage().contains("Không tìm thấy bình luận"));
    }
}
