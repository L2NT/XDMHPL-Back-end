package com.example.XDMHPL_Back_end.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.XDMHPL_Back_end.DTO.CommentDTO;
import com.example.XDMHPL_Back_end.DTO.NotificationDTO;
import com.example.XDMHPL_Back_end.Repositories.CommentRepository;
import com.example.XDMHPL_Back_end.Repositories.PostRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.NotificationStatus;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

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

    private Users testUser;
    private Post testPost;
    private Comment testComment;
    private CommentDTO testCommentDTO;

    @BeforeEach
    void setUp() {
        // Tạo test user
        testUser = new Users();
        testUser.setUserID(1);
        testUser.setUserName("testuser");

        // Tạo test post
        testPost = new Post();
        testPost.setPostID(1);
        testPost.setUser(testUser);
        testPost.setComments(new ArrayList<>());

        // Tạo test comment
        testComment = new Comment();
        testComment.setCommentID(1);
        testComment.setContent("Test comment");
        testComment.setUser(testUser);
        testComment.setPost(testPost);

        // Tạo test commentDTO
        testCommentDTO = new CommentDTO();
        testCommentDTO.setContent("Test comment");
    }

    @Test
    void createComment_Success() {
        // Arrange
        NotificationDTO expectedNotification = new NotificationDTO();

        when(postRepository.findById(1)).thenReturn(Optional.of(testPost));
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);
        when(notificationService.createNotification(
                anyInt(), anyInt(), any(NotificationStatus.class),
                anyInt(), anyInt(), any(), anyString()
        )).thenReturn(expectedNotification);

        // Act
        NotificationDTO result = commentService.createComment(testCommentDTO, 1, 1);

        // Assert
        assertNotNull(result);
        verify(postRepository).findById(1);
        verify(userRepository).findById(1);
        verify(commentRepository).save(any(Comment.class));
        verify(postRepository).save(testPost);
        verify(notificationService).createNotification(
                eq(1), eq(1), eq(NotificationStatus.COMMENT),
                eq(1), anyInt(), any(), anyString()
        );
    }

    @Test
    void createComment_PostNotFound() {
        // Arrange
        when(postRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.createComment(testCommentDTO, 999, 1)
        );

        assertTrue(exception.getMessage().contains("Không tìm thấy bài đăng"));
        verify(postRepository).findById(999);
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_UserNotFound() {
        // Arrange
        when(postRepository.findById(1)).thenReturn(Optional.of(testPost));
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.createComment(testCommentDTO, 1, 999)
        );

        assertTrue(exception.getMessage().contains("Không tìm thấy người dùng"));
        verify(userRepository).findById(999);
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getCommentById_Success() {
        // Arrange
        when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));

        // Act
        Comment result = commentService.getCommentById(1);

        // Assert
        assertNotNull(result);
        assertEquals(testComment.getCommentID(), result.getCommentID());
        assertEquals(testComment.getContent(), result.getContent());
        verify(commentRepository).findById(1);
    }

    @Test
    void getCommentById_NotFound() {
        // Arrange
        when(commentRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.getCommentById(999)
        );

        assertTrue(exception.getMessage().contains("Không tìm thấy bình luận"));
        verify(commentRepository).findById(999);
    }

    @Test
    void updateComment_Success() {
        // Arrange
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setContent("Updated comment");

        when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // Act
        Comment result = commentService.updateComment(updateDTO, 1);

        // Assert
        assertNotNull(result);
        verify(commentRepository).findById(1);
        verify(commentRepository).save(testComment);
    }

    @Test
    void updateComment_NotFound() {
        // Arrange
        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setContent("Updated comment");

        when(commentRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.updateComment(updateDTO, 999)
        );

        assertTrue(exception.getMessage().contains("Không tìm thấy bình luận"));
        verify(commentRepository).findById(999);
        verify(commentRepository, never()).save(any());
    }

    @Test
    void deleteComment_Success() {
        // Arrange
        when(commentRepository.findById(1)).thenReturn(Optional.of(testComment));
        doNothing().when(commentRepository).delete(testComment);

        // Act
        Comment result = commentService.deleteComment(1);

        // Assert
        assertNotNull(result);
        assertEquals(testComment.getCommentID(), result.getCommentID());
        verify(commentRepository).findById(1);
        verify(commentRepository).delete(testComment);
    }

    @Test
    void deleteComment_NotFound() {
        // Arrange
        when(commentRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.deleteComment(999)
        );

        assertTrue(exception.getMessage().contains("Không tìm thấy bình luận"));
        verify(commentRepository).findById(999);
        verify(commentRepository, never()).delete(any());
    }
}