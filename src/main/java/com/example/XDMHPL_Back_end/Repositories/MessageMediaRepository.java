package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.MessageMedia;

@Repository
public interface MessageMediaRepository extends JpaRepository<MessageMedia, Integer> {

    // Lấy tất cả media của một tin nhắn
    List<MessageMedia> findByMessageMessageID(int messageId);

    // Lấy media theo loại
    List<MessageMedia> findByMessageMessageIDAndMediaType(int messageId, String mediaType);
}