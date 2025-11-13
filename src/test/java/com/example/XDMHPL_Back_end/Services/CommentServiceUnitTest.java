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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
 * Unit Test cho CommentService
 * Test Level: Unit Test - Service Layer
 * Test Type: Functional Test
 * Sử dụng: Mockito để mock dependencies (Repository, NotificationService)
 *
 * Mục đích test:
 * - Kiểm tra business logic của service
 * - Đảm bảo các phương thức gọi đúng repository
 * - Kiểm tra exception handling
 * - Verify tương tác với dependencies
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService Unit Tests")
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

    private Users user;
    private Users postOwner;
    private Post post;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        // Khởi tạo test data
        user = new Users();
        user.setUserID(1);
        user.setUserName("commenter");
        user.setEmail("commenter@test.com");

        postOwner = new Users();
        postOwner.setUserID(2);
        postOwner.setUserName("postowner");

        post = new Post();
        post.setPostID(10);
        post.setUser(postOwner);
        post.setContent("Original post");
        post.setComments(new ArrayList<>());

        commentDTO = new CommentDTO();
        commentDTO.setContent("Test comment content");
    }

    /**
     * Test Case: Tạo comment thành công
     * Kịch bản: User comment vào post của người khác
     * Kết quả mong đợi:
     * - Comment được lưu vào database
     * - Notification được tạo cho chủ post
     * - Trả về NotificationDTO
     */
    @Test
    @DisplayName("Tạo comment thành công và gửi notification")
    void createComment_shouldReturnNotificationDTO_whenValidInput() {
        // Arrange
        Comment savedComment = new Comment();
        savedComment.setCommentID(99);
        savedComment.setContent(commentDTO.getContent());
        savedComment.setUser(user);
        savedComment.setPost(post);
        savedComment.setCreationDate(new Date());

        NotificationDTO mockNotification = new NotificationDTO();
        // Không set message nếu không có setter, chỉ verify được trả về

        when(postRepository.findById(10)).thenReturn(Optional.of(post));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(notificationService.createNotification(
                eq(2), eq(1), eq(NotificationStatus.COMMENT),
                eq(10), eq(99), isNull(), anyString()
        )).thenReturn(mockNotification);

        // Act
        NotificationDTO result = commentService.createComment(commentDTO, 10, 1);

        // Assert
        assertNotNull(result, "NotificationDTO không được null");

        // Verify interactions
        verify(postRepository, times(1)).findById(10);
        verify(userRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(postRepository, times(1)).save(post);
        verify(notificationService, times(1)).createNotification(
                eq(2), eq(1), eq(NotificationStatus.COMMENT),
                eq(10), eq(99), isNull(), eq("Đã bình luận về bài viết của bạn"));

        // Verify comment được thêm vào post
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());
        assertTrue(postCaptor.getValue().getComments().contains(savedComment));
    }

    /**
     * Test Case: Post không tồn tại
     * Kịch bản: PostID không hợp lệ
     * Kết quả mong đợi: Throw IllegalArgumentException
     */
    @Test
    @DisplayName("Throw exception khi post không tồn tại")
    void createComment_shouldThrowException_whenPostNotFound() {
        // Arrange
        when(postRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> commentService.createComment(commentDTO, 999, 1)
        );

        assertTrue(exception.getMessage().contains("Không tìm thấy bài đăng"));
        verify(commentRepository, never()).save(any());
        verify(notificationService, never()).createNotification(
                anyInt(), anyInt(), any(), anyInt(), anyInt(), any(), anyString());
    }

    /**
     * Test Case: User không tồn tại
     * Kịch bản: UserID không hợp lệ
     * Kết quả mong đợi: Throw IllegalArgumentException
     */
    @Test
    @DisplayName("Throw exception khi user không tồn tại")
    void createComment_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(postRepository.findById(10)).thenReturn(Optional.of(post));
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> commentService.createComment(commentDTO, 10, 999)
        );

        assertTrue(exception.getMessage().contains("Không tìm thấy người dùng"));
        verify(commentRepository, never()).save(any());
    }

    /**
     * Test Case: Lấy comment theo ID thành công
     * Kịch bản: CommentID hợp lệ
     * Kết quả mong đợi: Trả về comment
     */
    @Test
    @DisplayName("Lấy comment theo ID thành công")
    void getCommentById_shouldReturnComment_whenExists() {
        // Arrange
        Comment comment = new Comment();
        comment.setCommentID(1);
        comment.setContent("Existing comment");

        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        // Act
        Comment result = commentService.getCommentById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCommentID());
        assertEquals("Existing comment", result.getContent());
        verify(commentRepository, times(1)).findById(1);
    }

    /**
     * Test Case: Comment không tồn tại
     * Kịch bản: CommentID không hợp lệ
     * Kết quả mong đợi: Throw IllegalArgumentException
     */
    @Test
    @DisplayName("Throw exception khi comment không tồn tại")
    void getCommentById_shouldThrowException_whenNotFound() {
        // Arrange
        when(commentRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> commentService.getCommentById(999)
        );

        assertTrue(exception.getMessage().contains("Không tìm thấy bình luận"));
    }

    /**
     * Test Case: Cập nhật comment thành công
     * Kịch bản: Update nội dung comment
     * Kết quả mong đợi: Comment được update
     */
    @Test
    @DisplayName("Cập nhật comment thành công")
    void updateComment_shouldUpdateContent_whenCommentExists() {
        // Arrange
        Comment existingComment = new Comment();
        existingComment.setCommentID(1);
        existingComment.setContent("Old content");

        CommentDTO updateDTO = new CommentDTO();
        updateDTO.setContent("Updated content");

        when(commentRepository.findById(1)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);

        // Act
        Comment result = commentService.updateComment(updateDTO, 1);

        // Assert
        assertEquals("Updated content", result.getContent());
        verify(commentRepository, times(1)).save(existingComment);
    }

    /**
     * Test Case: Xóa comment thành công
     * Kịch bản: Comment tồn tại
     * Kết quả mong đợi: Comment bị xóa
     */
    @Test
    @DisplayName("Xóa comment thành công")
    void deleteComment_shouldDeleteComment_whenExists() {
        // Arrange
        Comment comment = new Comment();
        comment.setCommentID(1);
        comment.setContent("To be deleted");

        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

        // Act
        Comment result = commentService.deleteComment(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCommentID());
        verify(commentRepository, times(1)).delete(comment);
    }

    /**
     * Test Case: Xóa comment không tồn tại
     * Kịch bản: CommentID không hợp lệ
     * Kết quả mong đợi: Throw exception
     */
    @Test
    @DisplayName("Throw exception khi xóa comment không tồn tại")
    void deleteComment_shouldThrowException_whenNotFound() {
        // Arrange
        when(commentRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> commentService.deleteComment(999)
        );

        verify(commentRepository, never()).delete(any());
    }
}
