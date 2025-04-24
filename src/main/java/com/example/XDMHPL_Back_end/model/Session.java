package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Sessions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Session {
    @Id
    private String sessionID;
    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String deviceInfo;
}