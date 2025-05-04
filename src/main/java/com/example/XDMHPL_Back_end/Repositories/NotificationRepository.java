package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // Tìm thông báo theo userID và trạng thái đọc
    @Query("SELECT n FROM Notification n WHERE n.user.userID = :userID AND n.isReadFlag = :isReadFlag")
    List<Notification> findByUserIdAndReadStatus(@Param("userID") int userID, @Param("isReadFlag") int isReadFlag);
    
    // Tìm tất cả thông báo của một user
    @Query("SELECT n FROM Notification n WHERE n.user.userID = :userID AND n.user.userID != n.sender.userID")
    List<Notification> findByUserId(@Param("userID") int userID);
    
}
