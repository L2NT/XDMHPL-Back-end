package com.example.XDMHPL_Back_end.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.XDMHPL_Back_end.DTO.ChatBoxDTO;

public interface ChatBoxRepository extends JpaRepository<ChatBoxDTO, Integer> {
    Optional<ChatBoxDTO> findById(Integer chatBoxID);
}


