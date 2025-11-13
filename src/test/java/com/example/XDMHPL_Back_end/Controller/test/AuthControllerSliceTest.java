package com.example.XDMHPL_Back_end.Controller;

import com.example.XDMHPL_Back_end.DTO.LoginRequest;
import com.example.XDMHPL_Back_end.Services.SessionService;
import com.example.XDMHPL_Back_end.Services.UserService;
import com.example.XDMHPL_Back_end.model.Session;
import com.example.XDMHPL_Back_end.model.Users;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test slice cho AuthController — không load toàn bộ ApplicationContext
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Chỉ mock các dependency mà AuthController autowire
    @MockBean private UserService userService;
    @MockBean private SessionService sessionService;

    // Cấu hình giả để tránh Spring Boot quét các @Service/@Repository thực tế
    @Configuration
    @Import(AuthController.class)
    static class TestConfig {}

    @Test
    void login_shouldReturnOk_whenCredentialsValid() throws Exception {
        // Mock user
        Users mockUser = new Users();
        mockUser.setUserID(1);
        mockUser.setUserName("testuser");
        mockUser.setPassword("password123");
        mockUser.setRole("USER");

        // Mock session
        Session mockSession = new Session();
        mockSession.setSessionID("mock-session-id");
        mockSession.setDeviceInfo("JUnitTest");
        mockSession.setCreatedAt(LocalDateTime.now());
        mockSession.setExpiresAt(LocalDateTime.now().plusHours(2));

        // Giả lập hành vi service
        when(userService.loginValidate(anyString(), anyString(), anyString()))
                .thenReturn(mockUser);
        when(sessionService.hasActiveSession(any(Users.class)))
                .thenReturn(Collections.emptyList());
        when(sessionService.createSession(anyInt(), anyString()))
                .thenReturn("mock-session-id");

        LoginRequest req = new LoginRequest();
        req.setUserIdentifier("testuser");
        req.setPassword("password123");
        req.setRole("USER");
        req.setDeviceInfo("JUnitTest");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void login_shouldReturnUnauthorized_whenPasswordInvalid() throws Exception {
        // loginValidate trả về null nếu sai thông tin
        when(userService.loginValidate(anyString(), anyString(), anyString()))
                .thenReturn(null);

        LoginRequest req = new LoginRequest();
        req.setUserIdentifier("testuser");
        req.setPassword("wrongpassword");
        req.setRole("USER");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
