package com.example.XDMHPL_Back_end.Controller;

import com.example.XDMHPL_Back_end.Controller.AuthController;
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
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean private UserService userService;
    @MockBean private SessionService sessionService;

    @Configuration
    @Import(AuthController.class)
    static class TestConfig {}

    @Test
    void login_shouldReturnOk_whenCredentialsValid() throws Exception {
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

    @Test
    void checkUserSession_shouldReturnOk_whenSessionExists() throws Exception {
        Users mockUser = new Users();
        mockUser.setUserID(1);

        Session mockSession = new Session();
        mockSession.setSessionID("mock-session-id");

        when(userService.getUserById(1)).thenReturn(mockUser);
        when(sessionService.hasActiveSession(mockUser)).thenReturn(Collections.singletonList(mockSession));

        mockMvc.perform(get("/auth/check-session/1"))
                .andExpect(status().isOk());
    }

    @Test
    void checkUserSession_shouldReturnNotFound_whenNoSessionExists() throws Exception {
        Users mockUser = new Users();
        mockUser.setUserID(1);

        when(userService.getUserById(1)).thenReturn(mockUser);
        when(sessionService.hasActiveSession(mockUser)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/auth/check-session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSessionID_shouldReturnOk_whenSessionUpdated() throws Exception {
        Users mockUser = new Users();
        mockUser.setUserID(1);

        when(userService.updateSessionID(1, "new-session-id")).thenReturn(mockUser);

        mockMvc.perform(put("/auth/update-session/1")
                        .param("newSessionID", "new-session-id"))
                .andExpect(status().isOk());
    }

    @Test
    void logout_shouldReturnOk_whenSessionValid() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .header("SessionID", "mock-session-id"))
                .andExpect(status().isOk());
    }

    @Test
    void forgotPassword_shouldReturnOk_whenEmailSent() throws Exception {
        mockMvc.perform(post("/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_shouldReturnOk_whenPasswordReset() throws Exception {
        mockMvc.perform(post("/auth/reset-password/mock-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"newPassword123\"}"))
                .andExpect(status().isOk());
    }
}
