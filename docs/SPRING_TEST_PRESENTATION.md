# BÁO CÁO KIỂM THỬ - DỰ ÁN XDMHPL-BACK-END
Nhóm 25
Lê Nguyễn Nhất Tâm - 3122410369
Lưu Hồng Phúc - 3121410384
Đoàn Tuấn Tài - 3122410365

## 1. TỔNG QUAN

**Spring Test base trên JUnit 5**, mở rộng thêm:
- Quản lý ApplicationContext tự động
- Dependency Injection trong tests
- Transaction Management (rollback tự động)
- Test Utilities (MockMvc, TestEntityManager)

**Test Levels trong project:**
- **Unit Test**: Service layer (mock tất cả dependencies)
- **Slice Test**: Repository (@DataJpaTest), Controller (@WebMvcTest)

---

## 2. CÁC FILE TEST TRONG PROJECT

### 2.1. CommentRepositoryTest.java (Repository Layer)

**Công nghệ:**
- `@DataJpaTest` - Slice test cho JPA
- `TestEntityManager` - Quản lý entities
- `H2 Database` - In-memory thay MySQL

**9 test cases:**
1. findByPostAndUser - tồn tại
2. findByPostAndUser - không tồn tại  
3. findByCommentID - tồn tại
4. findByCommentID - không tồn tại
5. save - lưu comment mới
6. delete - xóa comment
7. update - cập nhật content
8. count - đếm số lượng
9. save - content dài (boundary)

### 2.2. CommentServiceUnitTest.java (Service Layer)

**Công nghệ:**
- `@ExtendWith(MockitoExtension.class)`
- `@Mock` - Mock repositories/services
- `@InjectMocks` - Inject vào CommentService

**8 test cases:**
1. createComment - thành công
2. createComment - post không tồn tại
3. createComment - user không tồn tại
4. getCommentById - tồn tại
5. getCommentById - không tồn tại
6. updateComment - cập nhật thành công
7. deleteComment - xóa thành công
8. deleteComment - không tồn tại

### 2.3. AuthControllerSliceTest.java (Controller Layer)

**Công nghệ:**
- `@WebMvcTest` - Slice test cho Web
- `MockMvc` - Giả lập HTTP requests
- `@MockBean` - Mock services

**8 test cases:**
1. login - credentials hợp lệ → 200 OK
2. login - password sai → 401 Unauthorized
3. checkUserSession - có session → 200 OK
4. checkUserSession - không có session → 404 Not Found
5. updateSessionID - thành công → 200 OK
6. logout - thành công → 200 OK
7. forgotPassword - gửi email → 200 OK
8. resetPassword - đổi password → 200 OK

---

## 3. SO SÁNH 3 LOẠI TESTS

| | Repository | Service | Controller |
|---|---|---|---|
| **Annotation** | @DataJpaTest | @ExtendWith(Mockito) | @WebMvcTest |
| **Database** | H2 in-memory | Mocked | Mocked |
| **Tools** | TestEntityManager | Mockito | MockMvc |
| **Test** | Query methods | Business logic | HTTP endpoints |
| **Type** | Slice Test | Pure Unit Test | Slice Test |

---

## 4. HƯỚNG DẪN CHẠY TEST

**Chạy tất cả:**
```bash
mvn test
```

**Chạy từng file:**
```bash
mvn -Dtest=CommentRepositoryTest test
mvn -Dtest=CommentServiceUnitTest test
mvn -Dtest=AuthControllerSliceTest test
```

**Chạy 1 method:**
```bash
mvn -Dtest=CommentRepositoryTest#findByPostAndUser_shouldReturnComment_whenExists test
```

---

## 5. DEMO (15 PHÚT)

**Repository Test (5 phút):**
- Chạy `CommentRepositoryTest`
- Quan sát H2 khởi tạo + SQL logs
- Highlight: `persistAndFlush()`, `clear()`, rollback

**Service Test (5 phút):**
- Chạy `CommentServiceUnitTest`  
- Highlight: `when().thenReturn()`, `verify()`
- Demo exception test

**Controller Test (5 phút):**
- Chạy `AuthControllerSliceTest`
- Highlight: `mockMvc.perform()`, `andExpect()`
- Demo HTTP status codes

---

## 6. KẾT LUẬN

**Tổng kết:** 3 file test, 25 test cases
- CommentRepositoryTest: 9 tests (JPA + H2)
- CommentServiceUnitTest: 8 tests (Mockito)
- AuthControllerSliceTest: 8 tests (MockMvc)

**Coverage:** Happy paths + negative cases + boundary conditions

**Dependencies:** spring-boot-starter-test, h2 database (đã có sẵn)
