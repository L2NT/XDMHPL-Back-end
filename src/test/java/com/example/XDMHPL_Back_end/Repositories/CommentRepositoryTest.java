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
 * ============================================================================
 * UNIT TEST CHO COMMENTREPOSITORY - REPOSITORY LAYER
 * ============================================================================
 *
 * CÂU HỎI 1: Test này thuộc Test Level nào? Test Type nào?
 * TRÁ LỜI:
 * - Test Level: UNIT TEST - Repository Layer (Kiểm thử đơn vị ở tầng Repository)
 * - Test Type: FUNCTIONAL TEST (Kiểm thử chức năng)
 * - Đây KHÔNG phải Integration Test vì chỉ test Repository layer riêng lẻ
 * - Đây KHÔNG phải Security Test hay Performance Test
 *
 * CÂU HỎI 2: Tại sao sử dụng @DataJpaTest? Đây có phải kiểm thử lát cắt không?
 * TRÁ LỜI:
 * - @DataJpaTest là KIỂM THỬ LÁT CẮT (Slice Testing)
 * - Chỉ load các JPA components: Repositories, EntityManager, DataSource
 * - KHÔNG load: Controllers, Services, Security, Web layer
 * - Tự động cấu hình H2 in-memory database
 * - Tự động rollback sau mỗi test (@Transactional mặc định)
 *
 * CÂU HỎI 3: JDBC Testing Support được sử dụng như thế nào?
 * TRÁ LỜI:
 * - Sử dụng H2 Embedded Database (In-memory database)
 * - TestEntityManager: Lớp hỗ trợ quản lý entities trong test
 * - entityManager.persistAndFlush(): Lưu và commit ngay vào database
 * - entityManager.clear(): Xóa cache, force fetch từ database
 * - Không cần JDBC TestUtils vì Spring Data JPA đã abstract hóa
 *
 * CÂU HỎI 4: Spring Test Context Framework được sử dụng như thế nào?
 * TRÁ LỜI:
 * - Test Context Manager: Tự động quản lý lifecycle của test
 * - Test Context: Lưu trữ ApplicationContext và test state
 * - Context Loader: @DataJpaTest sử dụng DataJpaTestContextBootstrapper
 * - Test Execution Listener: Lắng nghe các sự kiện (beforeTestMethod, afterTestMethod)
 * - DependencyInjectionTestExecutionListener: Inject dependencies (@Autowired)
 * - TransactionalTestExecutionListener: Quản lý transactions
 *
 * CÂU HỎI 5: Dependencies cần thiết trong pom.xml?
 * TRÁ LỜI:
 * - spring-boot-starter-test: JUnit 5, Mockito, AssertJ, Hamcrest
 * - spring-boot-starter-data-jpa: JPA testing support
 * - h2: H2 in-memory database
 * - Tất cả đã có sẵn khi dùng spring-boot-starter-test
 *
 * CÂU HỎI 6: Tại sao cần Test Repository layer?
 * TRÁ LỜI:
 * - Đảm bảo query methods hoạt động đúng
 * - Verify entity mapping (JPA annotations)
 * - Test cascade operations (delete, update)
 * - Kiểm tra constraints (unique, not null, foreign key)
 * - Đảm bảo transaction management
 */
@DataJpaTest // Slice Test: Chỉ load JPA components
@ActiveProfiles("test") // Sử dụng application-test.properties
@TestPropertySource(properties = {
    // Force H2 Dialect để tránh conflict với MySQL
    "spring.jpa.hibernate.ddl-auto=create-drop", // Tạo schema mới mỗi lần test
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.show-sql=true", // Hiển thị SQL để debug
    "spring.jpa.properties.hibernate.format_sql=true" // Format SQL cho dễ đọc
})
@DisplayName("CommentRepository Unit Tests")
class CommentRepositoryTest {

    /**
     * CÂU HỎI: TestEntityManager là gì? Khác gì với EntityManager thường?
     * TRÁ LỜI:
     * - TestEntityManager: Wrapper của EntityManager dành riêng cho test
     * - Cung cấp các method tiện lợi: persistAndFlush(), clear()
     * - Tự động quản lý transaction trong test
     * - Giúp test data được commit ngay để verify
     */
    @Autowired
    private TestEntityManager entityManager;

    /**
     * CÂU HỎI: Tại sao không Mock CommentRepository?
     * TRÁ LỜI:
     * - Đây là Repository Layer Test, cần test THẬT repository
     * - Mock chỉ dùng ở Service/Controller Layer Test
     * - Cần verify query methods hoạt động với database thật (H2)
     */
    @Autowired
    private CommentRepository commentRepository;

    private Users user;
    private Post post;

    /**
     * CÂU HỎI: @BeforeEach có vai trò gì?
     * TRÁ LỜI:
     * - Chạy TRƯỚC MỖI test method
     * - Đảm bảo mỗi test có môi trường sạch, độc lập
     * - Tránh test này ảnh hưởng test kia
     * - Tương tự @Before trong JUnit 4
     */
    @BeforeEach
    void setUp() {
        // JDBC Testing Support: Xóa dữ liệu cũ
        commentRepository.deleteAll();
        entityManager.clear(); // Xóa Persistence Context (cache)

        // Tạo test data: User
        user = new Users();
        user.setUserName("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setPhoneNumber("0123456789");
        user.setRole("USER");
        user = entityManager.persistAndFlush(user); // Lưu và commit ngay

        // Tạo test data: Post
        post = new Post();
        post.setUser(user);
        post.setContent("Test post content");
        post.setCreationDate(new Date());
        post = entityManager.persistAndFlush(post);
    }

    /**
     * ========================================================================
     * TEST CASE 1: HAPPY PATH - Tìm comment theo post và user (khi tồn tại)
     * ========================================================================
     *
     * CÂU HỎI: Test case này test cái gì?
     * TRÁ LỜI:
     * - NGHIỆP VỤ: Kiểm tra xem user đã comment vào post chưa
     * - Use Case: Hiển thị comment của user trong post detail
     * - Query Method: findByPostAndUser(Post post, Users user)
     *
     * CÂU HỎI: Tại sao cần entityManager.clear()?
     * TRÁ LỜI:
     * - Xóa cache của Persistence Context
     * - Force JPA fetch data từ database, không phải cache
     * - Đảm bảo test đúng với database thật
     *
     * CÂU HỎI: Arrange-Act-Assert pattern là gì?
     * TRÁ LỜI:
     * - Arrange: Chuẩn bị test data và môi trường
     * - Act: Thực hiện hành động cần test
     * - Assert: Kiểm tra kết quả có đúng mong đợi không
     * - Đây là best practice trong Unit Testing
     */
    @Test
    @DisplayName("Tìm comment theo post và user - Khi đã tồn tại")
    void findByPostAndUser_shouldReturnComment_whenExists() {
        // Arrange: Chuẩn bị test data
        Comment comment = new Comment();
        comment.setContent("Test comment");
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationDate(new Date());
        comment = entityManager.persistAndFlush(comment); // Lưu vào H2 database

        int expectedCommentId = comment.getCommentID();
        entityManager.clear(); // Xóa cache để test fetch thật

        // Act: Gọi method cần test
        Comment found = commentRepository.findByPostAndUser(post, user);

        // Assert: Verify kết quả
        assertThat(found).isNotNull(); // Phải tìm thấy
        assertThat(found.getContent()).isEqualTo("Test comment");
        assertThat(found.getUser().getUserID()).isEqualTo(user.getUserID());
        assertThat(found.getPost().getPostID()).isEqualTo(post.getPostID());
        assertThat(found.getCommentID()).isEqualTo(expectedCommentId);
    }

    /**
     * ========================================================================
     * TEST CASE 2: NEGATIVE CASE - Tìm comment không tồn tại
     * ========================================================================
     *
     * CÂU HỎI: Tại sao cần test trường hợp không tồn tại?
     * TRÁ LỜI:
     * - Đảm bảo method xử lý đúng khi không có data
     * - Tránh NullPointerException trong production
     * - Verify business logic: user chưa comment vào post này
     */
    @Test
    @DisplayName("Tìm comment theo post và user - Khi chưa tồn tại")
    void findByPostAndUser_shouldReturnNull_whenNotExists() {
        // Act: Không có Arrange vì không cần tạo data
        Comment found = commentRepository.findByPostAndUser(post, user);

        // Assert: Phải null vì chưa có comment
        assertThat(found).isNull();
    }

    /**
     * ========================================================================
     * TEST CASE 3: Tìm theo ID - Happy Path
     * ========================================================================
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
        assertThat(found).isPresent(); // Optional có value
        assertThat(found.get().getContent()).isEqualTo("Test comment by ID");
        assertThat(found.get().getCommentID()).isEqualTo(commentId);
    }

    /**
     * ========================================================================
     * TEST CASE 4: BOUNDARY CONDITION - ID không tồn tại
     * ========================================================================
     *
     * CÂU HỎI: Boundary Condition là gì?
     * TRÁ LỜI:
     * - Test các giá trị biên, cực trị
     * - VD: ID = 0, ID = 9999 (không tồn tại), ID âm
     * - Đảm bảo system xử lý đúng các edge cases
     */
    @Test
    @DisplayName("Tìm comment theo ID - Khi không tồn tại")
    void findByCommentID_shouldReturnEmpty_whenNotExists() {
        // Act
        Optional<Comment> found = commentRepository.findByCommentID(9999);

        // Assert
        assertThat(found).isEmpty(); // Optional không có value
    }

    /**
     * ========================================================================
     * TEST CASE 5: CREATE OPERATION - Test lưu comment mới
     * ========================================================================
     *
     * CÂU HỎI: Test này verify gì?
     * TRÁ LỜI:
     * - JPA save() method hoạt động đúng
     * - Auto-increment ID được generate
     * - Data được persist vào H2 database
     * - Entity mapping đúng (annotations)
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
        entityManager.flush(); // Force commit transaction
        entityManager.clear(); // Clear cache

        // Assert
        assertThat(saved.getCommentID()).isGreaterThan(0); // ID được generate

        // Verify từ database
        Comment fromDb = entityManager.find(Comment.class, saved.getCommentID());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getContent()).isEqualTo("New comment");
    }

    /**
     * ========================================================================
     * TEST CASE 6: DELETE OPERATION - Test xóa comment
     * ========================================================================
     *
     * CÂU HỏI: Tại sao cần test delete?
     * TRÁ LỜI:
     * - Verify cascade delete nếu có
     * - Đảm bảo không còn tồn tại trong database
     * - Test foreign key constraints
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
        assertThat(fromDb).isNull(); // Không còn trong database

        Optional<Comment> found = commentRepository.findByCommentID(commentId);
        assertThat(found).isEmpty(); // Không tìm thấy qua repository
    }

    /**
     * ========================================================================
     * TEST CASE 7: UPDATE OPERATION - Test cập nhật comment
     * ========================================================================
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
     * ========================================================================
     * TEST CASE 8: COUNT OPERATION - Test đếm số lượng
     * ========================================================================
     */
    @Test
    @DisplayName("Đếm số lượng comment")
    void count_shouldReturnCorrectNumber() {
        // Arrange: Tạo 3 comments
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
     * ========================================================================
     * TEST CASE 9: BOUNDARY CONDITION - Nội dung dài
     * ========================================================================
     *
     * CÂU HỎI: Tại sao test với content dài?
     * TRÁ LỜI:
     * - Verify column type (TEXT) có đủ lớn không
     * - Test varchar length limit
     * - Đảm bảo không bị truncate data
     */
    @Test
    @DisplayName("Lưu comment với nội dung dài")
    void save_shouldHandleLongContent() {
        // Arrange
        String longContent = "A".repeat(500); // 500 ký tự
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

/**
 * ============================================================================
 * TÓM TẮT REPOSITORY LAYER TEST
 * ============================================================================
 *
 * 1. TEST LEVEL: Unit Test - Repository Layer
 * 2. TEST TYPE: Functional Test
 * 3. SLICE TESTING: @DataJpaTest (chỉ load JPA components)
 * 4. EMBEDDED DATABASE: H2 in-memory database
 * 5. JDBC TESTING SUPPORT: TestEntityManager
 * 6. TEST CONTEXT FRAMEWORK: Tự động quản lý lifecycle và transactions
 * 7. ROLLBACK: Mỗi test tự động rollback, không ảnh hưởng database
 *
 * 8. CÁC TEST CASE PATTERNS:
 *    - Happy Path: Test trường hợp thành công
 *    - Negative Case: Test trường hợp thất bại
 *    - Boundary Condition: Test giá trị biên
 *    - CRUD Operations: Create, Read, Update, Delete
 *
 * 9. KHI NÀO DỪNG VIẾT TEST?
 *    - Đã cover tất cả public methods
 *    - Đã test happy path và negative cases
 *    - Đã test boundary conditions
 *    - Code coverage >= 80%
 *
 * 10. DEPENDENCIES CẦN THIẾT:
 *     - spring-boot-starter-test
 *     - spring-boot-starter-data-jpa
 *     - h2 database
 */
