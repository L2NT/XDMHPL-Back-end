package com.example.XDMHPL_Back_end.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.XDMHPL_Back_end.model.Session;
import com.example.XDMHPL_Back_end.model.Users;

import java.awt.desktop.UserSessionEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Session> findBySessionIDAndExpiresAtAfter(String sessionID, LocalDateTime now);
    List<Session> findByUser(Users user);
}