package com.example.XDMHPL_Back_end.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.Repositories.SessionRepository;
import com.example.XDMHPL_Back_end.model.Session;
import com.example.XDMHPL_Back_end.model.Users;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserService userService;

    public String createSession(int userID, String deviceInfo) {
        Users user = userService.getUserById(userID);
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Session session = new Session(
            sessionId,
            user,
            now,
            now.plusHours(2),
            deviceInfo
        );
        sessionRepository.save(session);
        return sessionId;
    }

    public void logout(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}