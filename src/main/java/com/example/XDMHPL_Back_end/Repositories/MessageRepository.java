package com.example.XDMHPL_Back_end.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.XDMHPL_Back_end.model.MessageModel;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageModel, Integer> {
    public List<MessageModel> findByChatBox_ChatBoxID(Integer chatBoxId);


}
