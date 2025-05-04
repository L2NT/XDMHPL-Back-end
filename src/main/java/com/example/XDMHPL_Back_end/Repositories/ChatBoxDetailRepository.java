package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.ChatBoxDetail;
import com.example.XDMHPL_Back_end.model.ChatBoxDetailId;

@Repository
public interface ChatBoxDetailRepository extends JpaRepository<ChatBoxDetail, ChatBoxDetailId> {
     // Tìm tất cả ChatBoxDetail của một người dùng
     List<ChatBoxDetail> findByIdUserID(int userId);
    
     // Tìm ChatBoxDetail theo cả userId và chatBoxId
     Optional<ChatBoxDetail> findByIdUserIDAndIdChatBoxID(int userId, int chatBoxId);
}
