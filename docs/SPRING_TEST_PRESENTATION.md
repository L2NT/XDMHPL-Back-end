# BÁO CÁO SPRING TEST - KIỂM THỬ TỰ ĐỘNG

## 1. TỔNG QUAN VỀ SPRING TEST

### 1.1. Mối quan hệ giữa JUnit và Spring Test
- **JUnit**: Framework kiểm thử cơ bản cho Java
- **Spring Test**: Xây dựng trên nền tảng JUnit, cung cấp thêm:
  - Quản lý ApplicationContext
  - Dependency Injection trong test
  - Transaction management cho test
  - Mock objects hỗ trợ

### 1.2. Test Levels (Các cấp độ kiểm thử)

```
┌─────────────────────────────────────┐
│     Integration Test (E2E)          │ ← Test toàn bộ hệ thống
├─────────────────────────────────────┤
│     Integration Test                │ ← Test nhiều components
├─────────────────────────────────────┤
│     Unit Test                       │ ← Test từng đơn vị nhỏ
└─────────────────────────────────────┘
```

**Spring Test hỗ trợ**:
- ✅ **Unit Test**: Test từng layer riêng lẻ
- ✅ **Integration Test**: Test tích hợp các components
- ✅ **Slice Test**: Test một phần của application (Controller, Repository, Service)

### 1.3. Test Types (Các loại kiểm thử)

| Test Type | Mô tả | Ví dụ trong project |
|-----------|-------|---------------------|
| **Functional Test** | Test chức năng nghiệp vụ | CommentServiceUnitTest |
| **Integration Test** | Test tích hợp components | CommentIntegrationTest |
| **Security Test** | Test bảo mật | AuthControllerSliceTest |
| **Performance Test** | Test hiệu năng | (Chưa implement) |

---

## 2. TEST Ở CÁC LAYER

### 2.1. Repository Layer Test

**Mục đích**: Kiểm tra query methods, tương tác với database

**Ví dụ**: `CommentRepositoryTest.java`

```java
@DataJpaTest  // Chỉ load JPA components
@ActiveProfiles("test")  // Sử dụng H2 database
@TestPropertySource(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class CommentRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Test
    void findByPostAndUser_shouldReturnComment_whenExists() {
        // Arrange: Tạo test data
        Comment comment = new Comment();
        comment.setContent("Test comment");
        comment = entityManager.persistAndFlush(comment);
        entityManager.clear();
        
        // Act: Gọi method cần test
        Comment found = commentRepository.findByPostAndUser(post, user);
        
        // Assert: Kiểm tra kết quả
        assertThat(found).isNotNull();
        assertThat(found.getContent()).isEqualTo("Test comment");
    }
}
```

**Đặc điểm**:
- Sử dụng `@DataJpaTest` → slice test
- Tự động sử dụng H2 in-memory database
- Tự động rollback sau mỗi test
- Sử dụng `TestEntityManager` để tạo test data

### 2.2. Service Layer Test

**Mục đích**: Kiểm tra business logic, không phụ thuộc database

**Ví dụ**: `CommentServiceUnitTest.java`

```java
@ExtendWith(MockitoExtension.class)  // Pure unit test với Mockito
class CommentServiceUnitTest {
    
    @Mock  // Giả lập dependencies
    private CommentRepository commentRepository;
    
    @Mock
    private NotificationService notificationService;
    
    @InjectMocks  // Inject các mock vào service
    private CommentService commentService;
    
    @Test
    void createComment_shouldReturnNotificationDTO_whenValidInput() {
        // Arrange: Setup mock behavior
        when(postRepository.findById(10)).thenReturn(Optional.of(post));
        when(commentRepository.save(any())).thenReturn(savedComment);
        
        // Act: Gọi method cần test
        NotificationDTO result = commentService.createComment(dto, 10, 1);
        
        // Assert: Verify kết quả và interactions
        assertNotNull(result);
        verify(commentRepository, times(1)).save(any());
        verify(notificationService, times(1)).createNotification(...);
    }
}
```

**Đặc điểm**:
- Mock toàn bộ dependencies
- Không cần database, không cần ApplicationContext
- Nhanh, tập trung vào logic
- Sử dụng Mockito framework

### 2.3. Controller Layer Test (Slice Test)

**Mục đích**: Test HTTP endpoints, request/response mapping

**Ví dụ**: `AuthControllerSliceTest.java`

```java
@WebMvcTest(controllers = AuthController.class)  // Chỉ load Controller layer
@AutoConfigureMockMvc(addFilters = false)  // Tắt Security filters
class AuthControllerSliceTest {
    
    @Autowired
    private MockMvc mockMvc;  // Giả lập HTTP requests
    
    @MockBean  // Mock Spring beans
    private UserService userService;
    
    @Test
    void login_shouldReturnOk_whenCredentialsValid() throws Exception {
        // Arrange: Setup mock
        when(userService.loginValidate(...)).thenReturn(mockUser);
        
        // Act & Assert: Gọi API và verify response
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sessionId").exists());
    }
}
```

**Đặc điểm**:
- Sử dụng `@WebMvcTest` → slice test cho web layer
- Sử dụng `MockMvc` để test HTTP
- Mock services, không test business logic
- Test validation, error handling

---

## 3. PHÂN LOẠI CÁC TEST TRONG PROJECT

### 3.1. CommentRepositoryTest
- **Test Level**: Unit Test - Repository Layer (Slice Test)
- **Test Type**: Functional Test
- **Annotation**: `@DataJpaTest`
- **Đặc điểm**:
  - Kiểm thử lát cắt (chỉ load JPA components)
  - Sử dụng H2 in-memory database
  - Test query methods
  - Tự động rollback

### 3.2. CommentServiceUnitTest
- **Test Level**: Unit Test - Service Layer
- **Test Type**: Functional Test
- **Annotation**: `@ExtendWith(MockitoExtension.class)`
- **Đặc điểm**:
  - Pure unit test
  - Mock tất cả dependencies
  - Test business logic isolated
  - Không cần database

### 3.3. AuthControllerSliceTest
- **Test Level**: Unit Test - Controller Layer (Slice Test)
- **Test Type**: Functional Test + Security Test
- **Annotation**: `@WebMvcTest`
- **Đặc điểm**:
  - Kiểm thử lát cắt (chỉ load web layer)
  - Mock services
  - Test HTTP endpoints
  - Test security aspects

---

## 4. MOCKITO FRAMEWORK

### 4.1. Mock là gì?

**Mock Object**: Đối tượng giả lập, thay thế dependencies thật trong test

**Tại sao cần Mock?**
- ✅ Test nhanh (không cần database, network)
- ✅ Tập trung vào logic cần test
- ✅ Kiểm soát được test data
- ✅ Test các trường hợp khó tái tạo (exception, timeout...)

### 4.2. Dependencies trong pom.xml

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.14.2</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-inline</artifactId>
    <version>5.2.0</version>
    <scope>test</scope>
</dependency>
```

### 4.3. Cách sử dụng Mockito

```java
// 1. Tạo mock object
@Mock
private CommentRepository commentRepository;

// 2. Define behavior
when(commentRepository.findById(1))
    .thenReturn(Optional.of(comment));

// 3. Verify interactions
verify(commentRepository, times(1)).save(any());
verify(commentRepository, never()).delete(any());

// 4. Argument Captor
ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
verify(repository).save(captor.capture());
assertEquals("content", captor.getValue().getContent());
```

---

## 5. JDBC TESTING SUPPORT

### 5.1. Embedded Databases - H2

**Cấu hình trong application-test.properties**:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_UPPER=false
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

**pom.xml**:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 6. KHI NÀO DỪNG VIẾT UNIT TEST?

### 6.1. Test Coverage Guidelines

```
Minimum Coverage:
- Critical paths: 100%
- Business logic: 80-90%
- Simple getters/setters: 0% (không cần test)
```

### 6.2. Các trường hợp cần test (Test Cases Pattern)

**1. Happy Path (Đường đi thành công)**
```java
@Test
void createComment_shouldSuccess_whenValidInput() { ... }
```

**2. Validation Errors**
```java
@Test
void createComment_shouldThrowException_whenPostNotFound() { ... }

@Test
void createComment_shouldThrowException_whenUserNotFound() { ... }
```

**3. Boundary Conditions (Điều kiện biên)**
```java
@Test
void findComments_shouldReturnEmpty_whenNoComments() { ... }

@Test
void save_shouldHandleLongContent() { ... }
```

**4. Exception Handling**
```java
@Test
void createComment_shouldRollback_whenDatabaseError() { ... }
```

**5. Business Rules**
```java
@Test
void createComment_shouldSendNotification_whenCommentCreated() { ... }
```

### 6.3. Mẫu viết Unit Test chuẩn

```java
/**
 * Test Case: [Tên test case]
 * Kịch bản: [Mô tả kịch bản test]
 * Kết quả mong đợi: [Kết quả cần verify]
 */
@Test
@DisplayName("[Tên hiển thị dễ hiểu]")
void methodName_shouldExpectedBehavior_whenCondition() {
    // Arrange: Chuẩn bị dữ liệu test
    CommentDTO dto = new CommentDTO();
    dto.setContent("Test content");
    
    // Act: Thực hiện hành động cần test
    Comment result = commentService.createComment(dto, postId, userId);
    
    // Assert: Kiểm tra kết quả
    assertNotNull(result);
    assertEquals("Test content", result.getContent());
    
    // Verify: Kiểm tra interactions (nếu có mock)
    verify(commentRepository, times(1)).save(any());
}
```

---

## 7. TÓM TẮT

### ✅ Những gì đã implement trong project:

1. **Repository Layer Test** (`CommentRepositoryTest`)
   - Sử dụng `@DataJpaTest`
   - Test query methods
   - Sử dụng H2 database
   - 10 test cases

2. **Service Layer Unit Test** (`CommentServiceUnitTest`)
   - Sử dụng Mockito
   - Mock toàn bộ dependencies
   - Test business logic
   - 10 test cases

3. **Controller Slice Test** (`AuthControllerSliceTest`)
   - Sử dụng `@WebMvcTest`
   - Test HTTP endpoints
   - Mock services
   - 8 test cases

### 📋 Dependencies cần thiết:

```xml
<!-- Spring Boot Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.14.2</version>
    <scope>test</scope>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

### 🎯 Chạy test:

```bash
# Chạy tất cả tests
mvn test

# Chạy một test class
mvn test -Dtest=CommentRepositoryTest

# Chạy một test method
mvn test -Dtest=CommentRepositoryTest#findByPostAndUser_shouldReturnComment_whenExists
```

