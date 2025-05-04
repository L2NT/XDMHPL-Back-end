package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.Message;
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
     // Lấy tin nhắn theo chatBoxID và sắp xếp theo thời gian
     List<Message> findByChatBoxChatBoxIDOrderByTimeAsc(int chatBoxId);
    
     // Lấy tin nhắn chưa đọc trong một chatbox
     List<Message> findByChatBoxChatBoxIDAndSeen(int chatBoxId, int seen);
     
     // Lấy tin nhắn giữa hai người dùng dựa vào chatbox
     @Query("SELECT m FROM Message m JOIN m.chatBox c JOIN c.chatBoxDetails d1 JOIN c.chatBoxDetails d2 " +
       "WHERE d1.id.userID = :userId1 AND d2.id.userID = :userId2 " +
       "AND SIZE(c.chatBoxDetails) = 2 ORDER BY m.time ASC")
     List<Message> findMessagesBetweenUsers(@Param("userId1") int userId1, @Param("userId2") int userId2);
}
