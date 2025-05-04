package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.Friend;
import com.example.XDMHPL_Back_end.model.Users;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    Optional<Friend> findByUserAndFriendUser(Users user, Users friendUser);
    Optional<Friend> findByUser_UserIDAndFriendUser_UserID(int userID, int friendUserID);
}

