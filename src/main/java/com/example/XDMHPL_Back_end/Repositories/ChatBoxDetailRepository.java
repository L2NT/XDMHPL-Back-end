package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.DTO.ChatBoxDTO;
import com.example.XDMHPL_Back_end.DTO.ChatBoxDetailDTO;

@Repository
public interface ChatBoxDetailRepository extends JpaRepository<ChatBoxDetailDTO, Integer> {

    /**
     * Tìm chi tiết chat box theo ID
     */
    Optional<ChatBoxDetailDTO> findById(Integer chatboxdetailId);

    /**
     * Tìm chi tiết chat box theo userId và chatBoxId
     */
    Optional<ChatBoxDetailDTO> findByUser_UserIDAndChatBox_ChatBoxID(Integer userId, Integer chatBoxId);

    /**
     * Lấy danh sách chi tiết chat box theo chatBoxId
     */
    List<ChatBoxDetailDTO> findByChatBox_ChatBoxID(Integer chatBoxId);

    /**
     * Lấy tất cả các ChatBox mà user tham gia
     */
    @Query("SELECT c.chatBox FROM ChatBoxDetail c WHERE c.user.userID = :userId")
    List<ChatBoxDTO> findChatBoxesByUserId(@Param("userId") Integer userId);

    /**
     * Kiểm tra xem user đã tham gia vào chat box chưa
     */
    boolean existsByUser_UserIDAndChatBox_ChatBoxID(Integer userId, Integer chatBoxId);
}
