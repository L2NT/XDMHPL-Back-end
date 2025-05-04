package com.example.XDMHPL_Back_end.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.Repositories.FriendRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.Friend;
import com.example.XDMHPL_Back_end.model.FriendStatus;
import com.example.XDMHPL_Back_end.model.NotificationStatus;
import com.example.XDMHPL_Back_end.model.Users;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private NotificationService notificationService;

    // Constructor...

    public List<Users> getAcceptedFriends(int userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ;
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
        if (acceptedFriends.isEmpty()) {
            throw new UsernameNotFoundException("No accepted friends found for user with ID: " + userId);
        }
        return acceptedFriends;
    }

    public void sentFriendRequest(int senderID, int receiverID) {
        // Tìm bài đăng theo ID
        Users sender = userRepository.findById(senderID)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài đăng với ID: " + senderID));

        // Tìm người dùng theo ID
        Users receiver = userRepository.findById(receiverID)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + receiverID));

        Friend friend = new Friend();
        friend.setUser(sender);
        friend.setFriendUser(receiver);
        friend.setStatus(FriendStatus.PENDING);
        friend.setCreatedAt(new Date());
        sender.getFriendOf().add(friend);
        receiver.getFriends().add(friend);

        friendRepository.save(friend);
        // notificationService.createNotification(receiver,sender,NotificationStatus.FRIEND_REQUEST,
        // null, null, null, "Đã gửi cho bạn lời mời kết bạn");
    }

    public void deleteFriend(int senderID, int receiverID) {
        // Tìm người gửi (sender)
        Users sender = userRepository.findById(senderID)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + senderID));

        // Tìm người nhận (receiver)
        Users receiver = userRepository.findById(receiverID)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + receiverID));

        // Tìm mối quan hệ bạn bè (có thể là từ sender -> receiver hoặc receiver ->
        // sender)
        Friend friendship = friendRepository.findByUserAndFriendUser(sender, receiver)
                .orElseGet(() -> friendRepository.findByUserAndFriendUser(receiver, sender)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Không tìm thấy mối quan hệ bạn bè giữa hai người dùng")));
        // Xóa mối quan hệ bạn bè
        friendRepository.delete(friendship);
    }

    public void acceptFriend(int senderID, int receiverID) {
        // Tìm người gửi (sender)
        Users sender = userRepository.findById(senderID)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + senderID));

        // Tìm người nhận (receiver)
        Users receiver = userRepository.findById(receiverID)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + receiverID));

        // Tìm mối quan hệ bạn bè giữa sender và receiver
        Friend friendship = friendRepository.findByUserAndFriendUser(sender, receiver)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu kết bạn giữa hai người dùng"));

        // Cập nhật trạng thái thành ACCEPTED
        friendship.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friendship);
    }

    public String getFriendStatus(Integer userID, Integer friendUserID) {
        Optional<Friend> relation = friendRepository.findByUser_UserIDAndFriendUser_UserID(userID, friendUserID);

        if (relation.isPresent()) {
            FriendStatus status = relation.get().getStatus();
            if (status == FriendStatus.ACCEPTED) {
                return "bạn bè";
            } else if (status == FriendStatus.PENDING) {
                return "đang chờ";
            }
        }

        // Kiểm tra xem người kia đã gửi lời mời đến mình chưa
        Optional<Friend> reverse = friendRepository.findByUser_UserIDAndFriendUser_UserID(friendUserID, userID);
        if (reverse.isPresent() && reverse.get().getStatus() == FriendStatus.PENDING) {
            return "chờ xác nhận";
        } else if (reverse.isPresent() && reverse.get().getStatus() == FriendStatus.ACCEPTED) {
            return "bạn bè";
        }

        return "kết bạn";
    }
}