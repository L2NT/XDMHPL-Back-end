package com.example.XDMHPL_Back_end.Repositories;

<<<<<<< HEAD
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.XDMHPL_Back_end.model.ChatBox;



public interface ChatBoxRepository extends JpaRepository<ChatBox, Integer> {
    Optional<ChatBox> findById(Integer chatBoxID);
}


=======
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.ChatBox;

@Repository
public interface ChatBoxRepository extends JpaRepository<ChatBox, Integer> {
    // Tìm chatbox có ít nhất một message
    @Query("SELECT DISTINCT c FROM ChatBox c JOIN c.messages m")
    List<ChatBox> findAllWithMessages();
    
    // Tìm chatbox theo tên (có thể dùng cho tìm kiếm)
    List<ChatBox> findByChatBoxNameContaining(String keyword);
}
>>>>>>> tuan
