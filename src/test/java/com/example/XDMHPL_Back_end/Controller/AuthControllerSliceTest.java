package com.example.XDMHPL_Back_end.Controller;

import com.example.XDMHPL_Back_end.Controller.AuthController;
import com.example.XDMHPL_Back_end.DTO.LoginRequest;
import com.example.XDMHPL_Back_end.Services.SessionService;
import com.example.XDMHPL_Back_end.Services.UserService;
import com.example.XDMHPL_Back_end.model.Session;
import com.example.XDMHPL_Back_end.model.Users;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * ============================================================================
 * UNIT TEST CHO AUTHCONTROLLER - CONTROLLER LAYER (SLICE TEST)
 * ============================================================================
 *
 * CÂU HỎI 1: Test này thuộc Test Level nào? Test Type nào?
 * TRÁ LỜI:
 * - Test Level: UNIT TEST - Controller Layer (Slice Test)
 * - Test Type: FUNCTIONAL TEST + SECURITY TEST
 * - Đây là KIỂM THỬ LÁT CẮT (Slice Testing)
 * - Test HTTP endpoints, request/response, validation
 *
 * CÂU HỎI 2: Tại sao sử dụng @WebMvcTest? Đây có phải kiểm thử lát cắt không?
 * TRÁ LỜI:
 * - @WebMvcTest là KIỂM THỬ LÁT CẮT (Slice Testing) cho Web Layer
 * - Chỉ load các Web components: Controllers, MockMvc, Jackson
 * - KHÔNG load: Repositories, Services (phải mock), JPA, Database
 * - Tự động cấu hình MockMvc để test HTTP requests
 * - Nhanh hơn @SpringBootTest vì chỉ load một phần context
 *
 * CÂU HỎI 3: @AutoConfigureMockMvc(addFilters = false) làm gì?
 * TRÁ LỜI:
 * - Tự động cấu hình MockMvc
 * - addFilters = false: TẮT tất cả Security Filters
 * - Lý do: Để test controller logic mà không cần xác thực
 * - Nếu muốn test Security, set addFilters = true
 *
 * CÂU HỎI 4: MockMvc là gì? Khác gì với RestTemplate?
 * TRÁ LỜI:
 * - MockMvc: Framework test HTTP trong Spring MVC
 * - Giả lập HTTP requests mà KHÔNG cần start server thật
 * - Nhanh hơn, không cần port, không cần network
 * - RestTemplate/TestRestTemplate: Cần start server thật (Integration Test)
 *
 * CÂU HỎI 5: @MockBean khác gì với @Mock?
 * TRÁ LỜI:
 * - @MockBean: Tạo mock bean trong Spring ApplicationContext
 * - @Mock: Tạo mock object thuần Mockito (không có Spring)
 * - @MockBean được inject vào Controller qua Spring DI
 * - Chỉ dùng @MockBean trong Spring Test (@WebMvcTest, @SpringBootTest)
 *
 * CÂU HỎI 6: Tại sao cần test Controller Layer?
 * TRÁ LỜI:
 * - Test HTTP endpoints: URL mapping, HTTP methods
 * - Test request/response: JSON serialization/deserialization
 * - Test validation: @Valid, @RequestBody, @PathVariable
 * - Test HTTP status codes: 200 OK, 400 Bad Request, 401 Unauthorized...
 * - Test error handling: Exception handling, error messages
 * - KHÔNG test business logic (đã test ở Service layer)
 *
 * CÂU HỎI 7: Dependencies trong pom.xml?
 * TRÁ LỜI:
 * - spring-boot-starter-test: MockMvc, JUnit 5
 * - spring-boot-starter-web: Spring MVC
 * - jackson: JSON processing
 * - Tất cả đã có sẵn khi dùng spring-boot-starter-test
 */
@WebMvcTest(controllers = AuthController.class) // Chỉ load AuthController
@AutoConfigureMockMvc(addFilters = false) // Tắt Security filters để test dễ dàng
@ActiveProfiles("test") // Sử dụng application-test.properties
@DisplayName("AuthController Slice Tests")
class AuthControllerSliceTest {

    /**
     * CÂU HỎI: MockMvc được inject như thế nào?
     * TRÁ LỜI:
     * - @WebMvcTest tự động cấu hình MockMvc bean
     * - Inject bằng @Autowired
     * - Sử dụng để perform HTTP requests
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * CÂU HỎI: ObjectMapper làm gì?
     * TRÁ LỜI:
     * - Jackson ObjectMapper: Convert Object <-> JSON
     * - writeValueAsString(): Object -> JSON string
     * - Dùng để tạo JSON request body
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * CÂU HỎI: Tại sao mock UserService và SessionService?
     * TRÁ LỜI:
     * - Controller cần dependencies này để hoạt động
     * - Không test Service logic, chỉ test Controller logic
     * - Mock để giả lập behavior mong muốn
     * - @MockBean tạo mock bean trong Spring context
     */
    @MockBean
    private UserService userService;

    @MockBean
    private SessionService sessionService;

    /**
     * CÂU HỎI: @Configuration và @Import làm gì?
     * TRÁ LỜI:
     * - Đôi khi cần import thêm configuration
     * - Ở đây import AuthController để đảm bảo được load
     * - Có thể bỏ nếu @WebMvcTest đã auto-detect
     */
    @Configuration
    @Import(AuthController.class)
    static class TestConfig {}

    /**
     * ========================================================================
     * TEST CASE 1: HAPPY PATH - Login thành công
     * ========================================================================
     *
     * CÂU HỎI: Test này test cái gì?
     * TRÁ LỜI:
     * - NGHIỆP VỤ: User login với credentials hợp lệ
     * - HTTP Method: POST
     * - Endpoint: /auth/login
     * - Request Body: LoginRequest JSON
     * - Expected: HTTP 200 OK, có sessionId trong response
     *
     * CÂU HỎI: mockMvc.perform() làm gì?
     * TRÁ LỜI:
     * - Giả lập một HTTP request
     * - post("/auth/login"): POST request tới /auth/login
     * - contentType(): Set Content-Type header
     * - content(): Set request body
     * - andExpect(): Verify response
     *
     * CÂU HỎI: Tại sao cần mock UserService.loginValidate()?
     * TRÁ LỜI:
     * - Controller gọi service.loginValidate() để xác thực
     * - Mock để return user giả lập
     * - Không test logic xác thực (đã test ở Service layer)
     * - Chỉ test Controller nhận request và trả response đúng
     */
    @Test
    @DisplayName("Login thành công với credentials hợp lệ")
    void login_shouldReturnOk_whenCredentialsValid() throws Exception {
        // Arrange: Chuẩn bị mock data
        Users mockUser = new Users();
        mockUser.setUserID(1);
        mockUser.setUserName("testuser");
        mockUser.setPassword("password123");
        mockUser.setRole("USER");
        mockUser.setFriends(new ArrayList<>());
        mockUser.setFriendOf(new ArrayList<>());

        Session mockSession = new Session();
        mockSession.setSessionID("mock-session-id");
        mockSession.setDeviceInfo("JUnitTest");
        mockSession.setCreatedAt(LocalDateTime.now());
        mockSession.setExpiresAt(LocalDateTime.now().plusHours(2));

        // MOCK: Giả lập UserService.loginValidate() trả về user
        when(userService.loginValidate(anyString(), anyString(), anyString()))
                .thenReturn(mockUser);

        // MOCK: Giả lập SessionService.hasActiveSession() trả về empty
        when(sessionService.hasActiveSession(any(Users.class)))
                .thenReturn(Collections.emptyList());

        // MOCK: Giả lập SessionService.createSession() trả về sessionId
        when(sessionService.createSession(anyInt(), anyString()))
                .thenReturn("mock-session-id");

        // Arrange: Tạo request body
        LoginRequest req = new LoginRequest();
        req.setUserIdentifier("testuser");
        req.setPassword("password123");
        req.setRole("USER");
        req.setDeviceInfo("JUnitTest");

        // Act & Assert: Gọi API và verify response
        mockMvc.perform(post("/auth/login") // POST /auth/login
                        .contentType(MediaType.APPLICATION_JSON) // Content-Type: application/json
                        .content(objectMapper.writeValueAsString(req))) // Request body
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(jsonPath("$.sessionId").exists()); // Response có sessionId
    }

    /**
     * ========================================================================
     * TEST CASE 2: SECURITY TEST - Login thất bại với password sai
     * ========================================================================
     *
     * CÂU HỎI: Đây có phải Security Test không?
     * TRÁ LỜI:
     * - CÓ, đây là Security Test
     * - Test xác thực (Authentication)
     * - Verify hệ thống từ chối password sai
     * - Expected: HTTP 401 Unauthorized
     */
    @Test
    @DisplayName("Login thất bại khi password không đúng")
    void login_shouldReturnUnauthorized_whenPasswordInvalid() throws Exception {
        // Arrange: Mock service trả về null (login fail)
        when(userService.loginValidate(anyString(), anyString(), anyString()))
                .thenReturn(null);

        LoginRequest req = new LoginRequest();
        req.setUserIdentifier("testuser");
        req.setPassword("wrongpassword");
        req.setRole("USER");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized()); // HTTP 401
    }

    /**
     * ========================================================================
     * TEST CASE 3: Check session tồn tại - Happy Path
     * ========================================================================
     *
     * CÂU HỎI: Test này test endpoint nào?
     * TRÁ LỜI:
     * - GET /auth/check-session/{userId}
     * - Kiểm tra user có session đang hoạt động không
     * - PathVariable: userId
     */
    @Test
    @DisplayName("Check session - Trả về OK khi có session")
    void checkUserSession_shouldReturnOk_whenSessionExists() throws Exception {
        // Arrange
        Users mockUser = new Users();
        mockUser.setUserID(1);

        Session mockSession = new Session();
        mockSession.setSessionID("mock-session-id");

        when(userService.getUserById(1)).thenReturn(mockUser);
        when(sessionService.hasActiveSession(mockUser))
                .thenReturn(Collections.singletonList(mockSession));

        // Act & Assert
        mockMvc.perform(get("/auth/check-session/1")) // GET /auth/check-session/1
                .andExpect(status().isOk()); // HTTP 200
    }

    /**
     * ========================================================================
     * TEST CASE 4: NEGATIVE CASE - Không có session
     * ========================================================================
     */
    @Test
    @DisplayName("Check session - Trả về 404 khi không có session")
    void checkUserSession_shouldReturnNotFound_whenNoSessionExists() throws Exception {
        // Arrange
        Users mockUser = new Users();
        mockUser.setUserID(1);

        when(userService.getUserById(1)).thenReturn(mockUser);
        when(sessionService.hasActiveSession(mockUser))
                .thenReturn(Collections.emptyList()); // Không có session

        // Act & Assert
        mockMvc.perform(get("/auth/check-session/1"))
                .andExpect(status().isNotFound()); // HTTP 404
    }

    /**
     * ========================================================================
     * TEST CASE 5: Update session ID
     * ========================================================================
     *
     * CÂU HỎI: Test request param như thế nào?
     * TRÁ LỜI:
     * - Sử dụng .param("key", "value")
     * - Test @RequestParam annotation
     */
    @Test
    @DisplayName("Update session ID thành công")
    void updateSessionID_shouldReturnOk_whenSessionUpdated() throws Exception {
        // Arrange
        Users mockUser = new Users();
        mockUser.setUserID(1);

        when(userService.updateSessionID(1, "new-session-id"))
                .thenReturn(mockUser);

        // Act & Assert
        mockMvc.perform(put("/auth/update-session/1") // PUT request
                        .param("newSessionID", "new-session-id")) // Request param
                .andExpect(status().isOk());
    }

    /**
     * ========================================================================
     * TEST CASE 6: Logout - Test header
     * ========================================================================
     *
     * CÂU HỎI: Test header như thế nào?
     * TRÁ LỜI:
     * - Sử dụng .header("headerName", "value")
     * - Test @RequestHeader annotation
     */
    @Test
    @DisplayName("Logout thành công")
    void logout_shouldReturnOk_whenSessionValid() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/logout")
                        .header("SessionID", "mock-session-id")) // Request header
                .andExpect(status().isOk());
    }

    /**
     * ========================================================================
     * TEST CASE 7: Forgot password - Test JSON request
     * ========================================================================
     */
    @Test
    @DisplayName("Forgot password - Gửi email thành công")
    void forgotPassword_shouldReturnOk_whenEmailSent() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}")) // JSON trực tiếp
                .andExpect(status().isOk());
    }

    /**
     * ========================================================================
     * TEST CASE 8: Reset password - Test PathVariable
     * ========================================================================
     *
     * CÂU HỎI: Test @PathVariable như thế nào?
     * TRÁ LỜI:
     * - Truyền trực tiếp trong URL path
     * - /auth/reset-password/{token}
     * - token = mock-token
     */
    @Test
    @DisplayName("Reset password thành công")
    void resetPassword_shouldReturnOk_whenPasswordReset() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/auth/reset-password/mock-token") // PathVariable
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"newPassword123\"}"))
                .andExpect(status().isOk());
    }
}

/**
 * ============================================================================
 * TÓM TẮT CONTROLLER LAYER TEST (SLICE TEST)
 * ============================================================================
 *
 * 1. TEST LEVEL: Unit Test - Controller Layer (Slice Test)
 * 2. TEST TYPE: Functional Test + Security Test
 * 3. ANNOTATION: @WebMvcTest (Slice Testing cho Web Layer)
 * 4. MOCKMVC: Giả lập HTTP requests mà không cần server
 * 5. @MockBean: Mock Spring beans (Services)
 *
 * 6. TEST CÁC THÀNH PHẦN:
 *    - HTTP Methods: GET, POST, PUT, DELETE
 *    - Request: Body, Params, Headers, PathVariable
 *    - Response: Status codes, JSON structure
 *    - Validation: @Valid, constraints
 *    - Error handling: Exception -> HTTP status
 *
 * 7. SO SÁNH VỚI CÁC LAYER KHÁC:
 *    Controller Test         | Service Test           | Repository Test
 *    -----------------------|------------------------|------------------
 *    @WebMvcTest            | @ExtendWith(Mockito)   | @DataJpaTest
 *    MockMvc                | Mock all dependencies  | H2 database
 *    Test HTTP endpoints    | Test business logic    | Test queries
 *    Mock services          | Pure unit test         | TestEntityManager
 *    Slice Test             | Pure Unit Test         | Slice Test
 *
 * 8. CÁC TEST CASE PATTERNS:
 *    - Happy Path: Request hợp lệ -> 200 OK
 *    - Validation Error: Request không hợp lệ -> 400 Bad Request
 *    - Authentication: Credentials sai -> 401 Unauthorized
 *    - Authorization: Không có quyền -> 403 Forbidden
 *    - Not Found: Resource không tồn tại -> 404 Not Found
 *    - Server Error: Exception -> 500 Internal Server Error
 *
 * 9. KHI NÀO DỪNG VIẾT TEST?
 *    - Tất cả endpoints đã được test
 *    - Happy path + error cases
 *    - Validation rules
 *    - Security aspects (nếu có)
 *    - Code coverage >= 80%
 *
 * 10. LỢI ÍCH CỦA SLICE TEST:
 *     - Nhanh: Chỉ load Web layer
 *     - Focused: Test riêng Controller logic
 *     - Isolated: Không phụ thuộc Service/Repository
 *     - MockMvc: Không cần start server
 *
 * 11. DEPENDENCIES CẦN THIẾT:
 *     - spring-boot-starter-test (MockMvc, JUnit 5)
 *     - spring-boot-starter-web (Spring MVC)
 *     - jackson (JSON processing)
 */
