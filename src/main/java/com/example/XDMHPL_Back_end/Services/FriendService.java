package com.example.XDMHPL_Back_end.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.FriendStatus;
import com.example.XDMHPL_Back_end.model.Users;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FriendService {
    
	@Autowired
    private UserRepository userRepository;
    
    // Constructor...
    
    public List<Users> getAcceptedFriends(int userId) {
        Users user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));;
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        List<Users> acceptedFriends = new ArrayList<>();
        
        // Lấy danh sách bạn bè người dùng đã kết bạn (user gửi lời mời)
        user.getFriends().stream()
            .filter(friend -> friend.getStatus() == FriendStatus.ACCEPTED)
            .forEach(friend -> acceptedFriends.add(friend.getFriendUser()));
        
        // Lấy danh sách người đã kết bạn với user (user nhận lời mời)
        user.getFriendOf().stream()
            .filter(friend -> friend.getStatus() == FriendStatus.ACCEPTED)
            .forEach(friend -> acceptedFriends.add(friend.getUser()));
        if(acceptedFriends.isEmpty()) {
            throw new UsernameNotFoundException("No accepted friends found for user with ID: " + userId);
        } else {
            System.out.println("Accepted friends for user " + userId );
        }
        return acceptedFriends;
    }
}