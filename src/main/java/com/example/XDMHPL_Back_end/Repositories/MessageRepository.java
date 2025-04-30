package com.example.XDMHPL_Back_end.Repositories;

import com.example.XDMHPL_Back_end.DTO.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    public List<Message> findByChatBox_ChatBoxID(Integer chatBoxId);


}
