package com.example.XDMHPL_Back_end.Repositories;

import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Test cho CommentRepository
 * Mục đích: Kiểm tra các query methods của repository
 * Test Level: Unit Test - Repository Layer
 * Test Type: Functional Test
 *
 * Kiểm thử lát cắt (Slice Test): Chỉ load JPA components
 * Sử dụng: H2 in-memory database
 * Framework: Spring Data JPA Test (@DataJpaTest)
 */
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.show-sql=true",
    "spring.jpa.properties.hibernate.format_sql=true"
})
@DisplayName("CommentRepository Unit Tests")
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private Users user;
    private Post post;

    @BeforeEach
    void setUp() {
        // Xóa dữ liệu cũ để đảm bảo test độc lập
        commentRepository.deleteAll();
        entityManager.clear();

        // Tạo user test
        user = new Users();
        user.setUserName("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setPhoneNumber("0123456789");
        user.setRole("USER");
        user = entityManager.persistAndFlush(user);

        // Tạo post test
        post = new Post();
        post.setUser(user);
        post.setContent("Test post content");
        post.setCreationDate(new Date());
        post = entityManager.persistAndFlush(post);
    }

    /**
     * Test Case: Tìm comment theo postID và userID
     * Kịch bản: User đã comment vào post
     * Kết quả mong đợi: Tìm thấy comment
     *
     * NGHIỆP VỤ: Kiểm tra xem user đã comment vào post chưa
     */
    @Test
    @DisplayName("Tìm comment theo post và user - Khi đã tồn tại")
    void findByPostAndUser_shouldReturnComment_whenExists() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("Test comment");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());
        comment = entityManager.persistAndFlush(comment);

        int expectedCommentId = comment.getCommentID();
        entityManager.clear();

        // Act
        Comment found = commentRepository.findByPostAndUser(post, user);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getContent()).isEqualTo("Test comment");
        assertThat(found.getUser().getUserID()).isEqualTo(user.getUserID());
        assertThat(found.getPost().getPostID()).isEqualTo(post.getPostID());
        assertThat(found.getCommentID()).isEqualTo(expectedCommentId);
    }

    /**
     * Test Case: Tìm comment không tồn tại
     * Kịch bản: User chưa comment vào post
     * Kết quả mong đợi: Trả về null
     *
     * NGHIỆP VỤ: User chưa từng bình luận vào bài viết này
     */
    @Test
    @DisplayName("Tìm comment theo post và user - Khi chưa tồn tại")
    void findByPostAndUser_shouldReturnNull_whenNotExists() {
        // Act
        Comment found = commentRepository.findByPostAndUser(post, user);

        // Assert
        assertThat(found).isNull();
    }

    /**
     * Test Case: Tìm comment theo ID
     * Kịch bản: Comment ID hợp lệ
     * Kết quả mong đợi: Tìm thấy comment
     *
     * NGHIỆP VỤ: Lấy chi tiết comment theo ID
     */
    @Test
    @DisplayName("Tìm comment theo ID - Khi tồn tại")
    void findByCommentID_shouldReturnComment_whenExists() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("Test comment by ID");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());
        comment = entityManager.persistAndFlush(comment);

        int commentId = comment.getCommentID();
        entityManager.clear();

        // Act
        Optional<Comment> found = commentRepository.findByCommentID(commentId);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("Test comment by ID");
        assertThat(found.get().getCommentID()).isEqualTo(commentId);
    }

    /**
     * Test Case: Tìm comment với ID không tồn tại
     * Kịch bản: Comment ID không hợp lệ
     * Kết quả mong đợi: Optional empty
     *
     * NGHIỆP VỤ: Xử lý trường hợp ID không hợp lệ
     */
    @Test
    @DisplayName("Tìm comment theo ID - Khi không tồn tại")
    void findByCommentID_shouldReturnEmpty_whenNotExists() {
        // Act
        Optional<Comment> found = commentRepository.findByCommentID(9999);

        // Assert
        assertThat(found).isEmpty();
    }

    /**
     * Test Case: Lưu comment mới
     * Kịch bản: Tạo comment mới cho post
     * Kết quả mong đợi: Comment được lưu thành công
     *
     * NGHIỆP VỤ: User tạo comment mới cho bài viết
     */
    @Test
    @DisplayName("Lưu comment mới vào database")
    void save_shouldPersistComment() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("New comment");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());

        // Act
        Comment saved = commentRepository.save(comment);
        entityManager.flush();
        entityManager.clear();

        // Assert
        assertThat(saved.getCommentID()).isGreaterThan(0);

        Comment fromDb = entityManager.find(Comment.class, saved.getCommentID());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getContent()).isEqualTo("New comment");
    }

    /**
     * Test Case: Xóa comment
     * Kịch bản: Xóa comment đã tồn tại
     * Kết quả mong đợi: Comment bị xóa khỏi database
     *
     * NGHIỆP VỤ: User xóa comment của mình
     */
    @Test
    @DisplayName("Xóa comment khỏi database")
    void delete_shouldRemoveComment() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("Comment to delete");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());
        comment = entityManager.persistAndFlush(comment);

        int commentId = comment.getCommentID();

        // Act
        commentRepository.delete(comment);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Comment fromDb = entityManager.find(Comment.class, commentId);
        assertThat(fromDb).isNull();

        Optional<Comment> found = commentRepository.findByCommentID(commentId);
        assertThat(found).isEmpty();
    }

    /**
     * Test Case: Cập nhật comment
     * Kịch bản: Update nội dung comment đã tồn tại
     * Kết quả mong đợi: Comment được update thành công
     *
     * NGHIỆP VỤ: User chỉnh sửa comment của mình
     */
    @Test
    @DisplayName("Cập nhật nội dung comment")
    void update_shouldModifyComment() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("Original content");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());
        comment = entityManager.persistAndFlush(comment);

        int commentId = comment.getCommentID();
        entityManager.clear();

        // Act
        Comment toUpdate = commentRepository.findByCommentID(commentId).orElseThrow();
        toUpdate.setContent("Updated content");
        commentRepository.save(toUpdate);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Comment updated = entityManager.find(Comment.class, commentId);
        assertThat(updated).isNotNull();
        assertThat(updated.getContent()).isEqualTo("Updated content");
    }

    /**
     * Test Case: Count comments
     * Kịch bản: Đếm số lượng comment
     * Kết quả mong đợi: Trả về đúng số lượng
     *
     * NGHIỆP VỤ: Hiển thị số lượng comment của bài viết
     */
    @Test
    @DisplayName("Đếm số lượng comment")
    void count_shouldReturnCorrectNumber() {
        // Arrange
        for (int i = 0; i < 3; i++) {
            Comment comment = new Comment();
            comment.setContent("Comment " + i);
            comment.setPost(post);
            comment.setUser(user);
            comment.setCreationDate(new Date());
            entityManager.persist(comment);
        }
        entityManager.flush();
        entityManager.clear();

        // Act
        long count = commentRepository.count();

        // Assert
        assertThat(count).isEqualTo(3);
    }

    /**
     * Test Case: Boundary - Nội dung comment rất dài
     * Kịch bản: Tạo comment với content max length
     * Kết quả mong đợi: Lưu thành công
     *
     * NGHIỆP VỤ: Kiểm tra giới hạn độ dài comment
     */
    @Test
    @DisplayName("Lưu comment với nội dung dài")
    void save_shouldHandleLongContent() {
        // Arrange
        String longContent = "A".repeat(500);
        Comment comment = new Comment();
        comment.setContent(longContent);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());

        // Act
        Comment saved = commentRepository.save(comment);
        entityManager.flush();

        // Assert
        assertThat(saved.getCommentID()).isGreaterThan(0);
        assertThat(saved.getContent()).hasSize(500);
    }
}
