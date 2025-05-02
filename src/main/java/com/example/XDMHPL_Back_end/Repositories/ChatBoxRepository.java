package com.example.XDMHPL_Back_end.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.XDMHPL_Back_end.DTO.ChatBox;

public interface ChatBoxRepository extends JpaRepository<ChatBox, Integer> {
    Optional<ChatBox> findById(Integer chatBoxID);
}


