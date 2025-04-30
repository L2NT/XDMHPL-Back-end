package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.DTO.ChatBox;
import com.example.XDMHPL_Back_end.DTO.ChatBoxDetail;

@Repository
public interface ChatBoxDetailRepository extends JpaRepository<ChatBoxDetail, Integer> {

    /**
     * Tìm chi tiết chat box theo ID
     */
    Optional<ChatBoxDetail> findById(Integer chatboxdetailId);

    /**
     * Tìm chi tiết chat box theo userId và chatBoxId
     */
    Optional<ChatBoxDetail> findByUser_UserIDAndChatBox_ChatBoxID(Integer userId, Integer chatBoxId);

    /**
     * Lấy danh sách chi tiết chat box theo chatBoxId
     */
    List<ChatBoxDetail> findByChatBox_ChatBoxID(Integer chatBoxId);

    /**
     * Lấy tất cả các ChatBox mà user tham gia
     */
    @Query("SELECT c.chatBox FROM ChatBoxDetail c WHERE c.user.userID = :userId")
    List<ChatBox> findChatBoxesByUserId(@Param("userId") Integer userId);

    /**
     * Kiểm tra xem user đã tham gia vào chat box chưa
     */
    boolean existsByUser_UserIDAndChatBox_ChatBoxID(Integer userId, Integer chatBoxId);
}
