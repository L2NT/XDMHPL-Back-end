package com.example.XDMHPL_Back_end.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.XDMHPL_Back_end.Repositories.SessionRepository;
import com.example.XDMHPL_Back_end.model.Session;
import com.example.XDMHPL_Back_end.model.Users;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws IOException {
        String sessionId = req.getHeader("SessionID");
        if (sessionId == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing SessionID");
            return false;
        }
        boolean valid = sessionRepository
            .findBySessionIDAndExpiresAtAfter(sessionId, LocalDateTime.now())
            .isPresent();
        if (!valid) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired session");
            return false;
        }
        // gán userID nếu cần
        int userID = sessionRepository
            .findById(sessionId)
            .map(Session::getUser) // Lấy đối tượng Users
            .map(Users::getUserID) // Lấy userID từ Users
            .orElse(null);
        req.setAttribute("userID", userID);
        return true;
    }
}