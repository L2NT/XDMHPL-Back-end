package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.MessageMediaModel;


@Repository
public interface MessageMediaRepository extends JpaRepository<MessageMediaModel, Integer> {
    
    // Truy vấn để lấy các media của chatBox với các loại media khác nhau
    @Query("SELECT m FROM MessageMediaModel m WHERE m.message.chatBox.chatBoxID = :chatBoxID AND " +
       "(m.mediaType LIKE 'image%' OR m.mediaType LIKE 'video%' OR m.mediaType LIKE 'audio%')")
    List<MessageMediaModel> findMediaByChatBoxID(@Param("chatBoxID") Integer chatBoxID);

    // Tìm các media liên quan đến một message cụ thể
    List<MessageMediaModel> findByMessage_MessageId(Integer messageId);
    
    // Tìm các media theo messageId và loại media
    List<MessageMediaModel> findByMessage_MessageIdAndMediaType(Integer messageId, String mediaType);
}
