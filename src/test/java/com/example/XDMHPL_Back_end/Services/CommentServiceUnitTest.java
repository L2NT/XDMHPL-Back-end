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
 * ============================================================================
 * UNIT TEST CHO COMMENTSERVICE - SERVICE LAYER
 * ============================================================================
 *
 * CÂU HỎI 1: Test này thuộc Test Level nào? Test Type nào?
 * TRÁ LỜI:
 * - Test Level: UNIT TEST - Service Layer
 * - Test Type: FUNCTIONAL TEST
 * - KHÔNG phải Integration Test vì tất cả dependencies đều MOCK
 * - KHÔNG phải Slice Test vì không load ApplicationContext
 *
 * CÂU HỎI 2: Tại sao sử dụng @ExtendWith(MockitoExtension.class)?
 * TRÁ LỜI:
 * - Kích hoạt Mockito framework cho JUnit 5
 * - Tự động khởi tạo các @Mock objects
 * - Inject các mock vào @InjectMocks
 * - Không cần MockitoAnnotations.openMocks(this)
 *
 * CÂU HỎI 3: Mock Objects là gì? Tại sao cần Mock?
 * TRÁ LỜI:
 * - Mock Objects: Đối tượng giả lập, thay thế dependencies thật
 * - Mockito Framework: Thư viện tạo và quản lý mock objects
 * - LÝ DO CẦN MOCK:
 *   + Test nhanh: Không cần database, network, file system
 *   + Isolated: Chỉ test business logic của service
 *   + Kiểm soát: Có thể giả lập bất kỳ scenario nào
 *   + Test edge cases: Exception, timeout, null values...
 *
 * CÂU HỎI 4: Khác biệt với Repository Test?
 * TRÁ LỜI:
 * - Repository Test: Dùng database THẬT (H2), test query methods
 * - Service Test: Mock TOÀN BỘ dependencies, test business logic
 * - Repository Test: @DataJpaTest (Slice Test)
 * - Service Test: @ExtendWith(MockitoExtension.class) (Pure Unit Test)
 *
 * CÂU HỎI 5: Test ở Service Layer test cái gì?
 * TRÁ LỜI:
 * - Business logic, nghiệp vụ của application
 * - Xử lý data từ DTO -> Entity
 * - Orchestration: Gọi nhiều repositories, services
 * - Validation: Kiểm tra input hợp lệ
 * - Exception handling: Xử lý lỗi
 * - Transaction management
 *
 * CÂU HỎI 6: Dependencies trong pom.xml?
 * TRÁ LỜI:
 * - mockito-core: Framework mock chính
 * - mockito-inline: Mock final classes và static methods
 * - mockito-junit-jupiter: Tích hợp Mockito với JUnit 5
 * - Đã bao gồm trong spring-boot-starter-test
 */
@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito
@DisplayName("CommentService Unit Tests")
class CommentServiceUnitTest {

    /**
     * CÂU HỎI: @Mock annotation làm gì?
     * TRÁ LỜI:
     * - Tạo mock object của interface/class
     * - Mock object này là "dummy", không có logic thật
     * - Cần define behavior bằng when().thenReturn()
     * - Tất cả methods mặc định return null/0/false
     */
    @Mock
    private UserRepository userRepository; // Mock Repository

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NotificationService notificationService;

    /**
     * CÂU HỎI: @InjectMocks annotation làm gì?
     * TRÁ LỜI:
     * - Tạo instance thật của class cần test
     * - Tự động inject các @Mock vào constructor/setter/field
     * - Đây là object chứa business logic cần test
     */
    @InjectMocks
    private CommentService commentService; // Object cần test

    private Users user;
    private Users postOwner;
    private Post post;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        // Khởi tạo test data (không cần database)
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
     * ========================================================================
     * TEST CASE 1: HAPPY PATH - Tạo comment thành công
     * ========================================================================
     *
     * CÂU HỎI: Test này test cái gì?
     * TRÁ LỜI:
     * - NGHIỆP VỤ: User tạo comment vào post của người khác
     * - Business Logic:
     *   1. Validate post tồn tại
     *   2. Validate user tồn tại
     *   3. Tạo Comment entity
     *   4. Lưu comment vào database
     *   5. Thêm comment vào post.comments list
     *   6. Tạo notification cho chủ post
     * - Không cần database thật, dùng Mock
     *
     * CÂU HỎI: when().thenReturn() là gì?
     * TRÁ LỜI:
     * - Define behavior cho mock object
     * - when(mockObj.method(params)): Khi gọi method với params
     * - thenReturn(value): Trả về value
     * - Giả lập dependency trả về data mong muốn
     *
     * CÂU HỎI: verify() làm gì?
     * TRÁ LỜI:
     * - Kiểm tra mock object đã được gọi chưa
     * - verify(mock, times(n)): Phải gọi đúng n lần
     * - verify(mock, never()): Không được gọi
     * - Đảm bảo service gọi đúng dependencies
     */
    @Test
    @DisplayName("Tạo comment thành công và gửi notification")
    void createComment_shouldReturnNotificationDTO_whenValidInput() {
        // Arrange: Setup mock behavior
        Comment savedComment = new Comment();
        savedComment.setCommentID(99);
        savedComment.setContent(commentDTO.getContent());
        savedComment.setUser(user);
        savedComment.setPost(post);
        savedComment.setCreationDate(new Date());

        NotificationDTO mockNotification = new NotificationDTO();

        // MOCK: Giả lập postRepository.findById() trả về post
        when(postRepository.findById(10)).thenReturn(Optional.of(post));

        // MOCK: Giả lập userRepository.findById() trả về user
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // MOCK: Giả lập commentRepository.save() trả về savedComment
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // MOCK: Giả lập postRepository.save() trả về post
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // MOCK: Giả lập notificationService.createNotification() trả về notification
        when(notificationService.createNotification(
                eq(2), eq(1), eq(NotificationStatus.COMMENT),
                eq(10), eq(99), isNull(), anyString()
        )).thenReturn(mockNotification);

        // Act: Gọi method cần test
        NotificationDTO result = commentService.createComment(commentDTO, 10, 1);

        // Assert: Verify kết quả
        assertNotNull(result, "NotificationDTO không được null");

        // VERIFY: Kiểm tra các dependencies đã được gọi đúng
        verify(postRepository, times(1)).findById(10); // Phải gọi đúng 1 lần
        verify(userRepository, times(1)).findById(1);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(postRepository, times(1)).save(post);
        verify(notificationService, times(1)).createNotification(
                eq(2), eq(1), eq(NotificationStatus.COMMENT),
                eq(10), eq(99), isNull(), eq("Đã bình luận về bài viết của bạn"));

        // ArgumentCaptor: Capture argument để verify chi tiết
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());
        assertTrue(postCaptor.getValue().getComments().contains(savedComment));
    }

    /**
     * ========================================================================
     * TEST CASE 2: VALIDATION - Post không tồn tại
     * ========================================================================
     *
     * CÂU HỎI: Tại sao test exception?
     * TRÁ LỜI:
     * - Đảm bảo service validate input đúng
     * - Xử lý edge case: data không hợp lệ
     * - Tránh NullPointerException trong production
     * - Verify error message rõ ràng
     */
    @Test
    @DisplayName("Throw exception khi post không tồn tại")
    void createComment_shouldThrowException_whenPostNotFound() {
        // Arrange: Mock repository trả về empty
        when(postRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert: Expect exception
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> commentService.createComment(commentDTO, 999, 1)
        );

        // Verify error message
        assertTrue(exception.getMessage().contains("Không tìm thấy bài đăng"));

        // Verify không lưu comment
        verify(commentRepository, never()).save(any());
        verify(notificationService, never()).createNotification(
                anyInt(), anyInt(), any(), anyInt(), anyInt(), any(), anyString());
    }

    /**
     * ========================================================================
     * TEST CASE 3: VALIDATION - User không tồn tại
     * ========================================================================
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
     * ========================================================================
     * TEST CASE 4: READ OPERATION - Lấy comment theo ID
     * ========================================================================
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
     * ========================================================================
     * TEST CASE 5: NEGATIVE CASE - Comment không tồn tại
     * ========================================================================
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
     * ========================================================================
     * TEST CASE 6: UPDATE OPERATION - Cập nhật comment
     * ========================================================================
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
     * ========================================================================
     * TEST CASE 7: DELETE OPERATION - Xóa comment thành công
     * ========================================================================
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
     * ========================================================================
     * TEST CASE 8: VALIDATION - Xóa comment không tồn tại
     * ========================================================================
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

/**
 * ============================================================================
 * TÓM TẮT SERVICE LAYER TEST
 * ============================================================================
 *
 * 1. TEST LEVEL: Unit Test - Service Layer
 * 2. TEST TYPE: Functional Test
 * 3. MOCKITO FRAMEWORK:
 *    - @Mock: Tạo mock object
 *    - @InjectMocks: Inject mock vào service
 *    - when().thenReturn(): Define mock behavior
 *    - verify(): Kiểm tra method đã được gọi
 *    - ArgumentCaptor: Capture argument để verify chi tiết
 *
 * 4. SO SÁNH VỚI REPOSITORY TEST:
 *    Repository Test          | Service Test
 *    ------------------------|------------------------
 *    @DataJpaTest            | @ExtendWith(MockitoExtension)
 *    H2 database thật        | Mock toàn bộ
 *    Test query methods      | Test business logic
 *    Slice Test              | Pure Unit Test
 *    Cần ApplicationContext  | Không cần Context
 *
 * 5. TEST COVERAGE:
 *    - Happy path: Thành công
 *    - Validation: Input không hợp lệ
 *    - Exception: Error handling
 *    - Negative cases: Data không tồn tại
 *    - Business rules: Notification, cascade...
 *
 * 6. KHI NÀO DỪNG VIẾT TEST?
 *    - Tất cả public methods đã được test
 *    - Happy path + negative cases
 *    - Exception handling
 *    - Code coverage >= 80%
 *
 * 7. LỢI ÍCH CỦA MOCK:
 *    - Nhanh: Không cần database
 *    - Isolated: Chỉ test service logic
 *    - Flexible: Giả lập mọi scenario
 *    - Deterministic: Kết quả nhất quán
 */
