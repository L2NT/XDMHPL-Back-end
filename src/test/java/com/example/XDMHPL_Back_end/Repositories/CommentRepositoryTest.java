package com.example.XDMHPL_Back_end.Repositories;

import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Test cho CommentRepository
 * Mục đích: Kiểm tra các query methods của repository
 * Test Level: Unit Test - Repository Layer
 * Test Type: Functional Test
 */
@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private Users user;
    private Post post;

    @BeforeEach
    void setUp() {
        // Tạo user test
        user = new Users();
        user.setUserName("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setRole("USER");
        entityManager.persist(user);

        // Tạo post test
        post = new Post();
        post.setUser(user);
        post.setContent("Test post content");
        post.setCreationDate(new Date());
        entityManager.persist(post);

        entityManager.flush();
    }

    /**
     * Test Case: Tìm comment theo postID và userID
     * Kịch bản: User đã comment vào post
     * Kết quả mong đợi: Tìm thấy comment
     */
    @Test
    void findByPostAndUser_shouldReturnComment_whenExists() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("Test comment");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());
        entityManager.persist(comment);
        entityManager.flush();

        // Act
        Comment found = commentRepository.findByPostAndUser(post, user);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getContent()).isEqualTo("Test comment");
        assertThat(found.getUser().getUserID()).isEqualTo(user.getUserID());
        assertThat(found.getPost().getPostID()).isEqualTo(post.getPostID());
    }

    /**
     * Test Case: Tìm comment không tồn tại
     * Kịch bản: User chưa comment vào post
     * Kết quả mong đợi: Trả về null
     */
    @Test
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
     */
    @Test
    void findByCommentID_shouldReturnComment_whenExists() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("Test comment by ID");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());
        Comment saved = entityManager.persist(comment);
        entityManager.flush();

        // Act
        Optional<Comment> found = commentRepository.findByCommentID(saved.getCommentID());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("Test comment by ID");
    }

    /**
     * Test Case: Tìm comment với ID không tồn tại
     * Kịch bản: Comment ID không hợp lệ
     * Kết quả mong đợi: Optional empty
     */
    @Test
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
     */
    @Test
    void save_shouldPersistComment() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("New comment");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());

        // Act
        Comment saved = commentRepository.save(comment);

        // Assert
        assertThat(saved.getCommentID()).isGreaterThan(0);
        assertThat(entityManager.find(Comment.class, saved.getCommentID())).isNotNull();
    }

    /**
     * Test Case: Xóa comment
     * Kịch bản: Xóa comment đã tồn tại
     * Kết quả mong đợi: Comment bị xóa khỏi database
     */
    @Test
    void delete_shouldRemoveComment() {
        // Arrange
        Comment comment = new Comment();
        comment.setContent("Comment to delete");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());
        Comment saved = entityManager.persist(comment);
        entityManager.flush();
        int commentId = saved.getCommentID();

        // Act
        commentRepository.delete(saved);
        entityManager.flush();

        // Assert
        assertThat(entityManager.find(Comment.class, commentId)).isNull();
    }
}

