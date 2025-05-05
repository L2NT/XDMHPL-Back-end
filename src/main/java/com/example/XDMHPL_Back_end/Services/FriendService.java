package com.example.XDMHPL_Back_end.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.DTO.NotificationDTO;
import com.example.XDMHPL_Back_end.Repositories.FriendRepository;
import com.example.XDMHPL_Back_end.Repositories.NotificationRepository;
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

    @Autowired
    private NotificationRepository notificationRepository;

    // Constructor...

    public List<Users> getUserOnline(int userId) {
        // Kiểm tra user tồn tại
        userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Lấy danh sách user online, ngoại trừ userId
        return userRepository.findAllByUserIDNotAndRoleNot(userId,"admin");
    }

    public NotificationDTO sentFriendRequest(int senderID, int receiverID) {
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
        return notificationService.createNotification(receiver.getUserID(), sender.getUserID(),
                NotificationStatus.FRIEND_REQUEST, null, null, null, "Đã gửi lời mời kết bạn");

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
        Optional<Friend> friendshipOpt = friendRepository.findByUserAndFriendUser(sender, receiver);

        if (friendshipOpt.isPresent()) {
            // Xóa thông báo FriendRequest nếu có
            notificationRepository.deleteByUser_UserIDAndSender_UserIDAndType(
                    receiver.getUserID(), sender.getUserID(), NotificationStatus.FRIEND_REQUEST);

            // Xóa mối quan hệ
            friendRepository.delete(friendshipOpt.get());

        } else {
            // Thử tìm theo chiều ngược lại
            friendshipOpt = friendRepository.findByUserAndFriendUser(receiver, sender);

            if (friendshipOpt.isPresent()) {
                notificationRepository.deleteByUser_UserIDAndSender_UserIDAndType(
                        sender.getUserID(), receiver.getUserID(), NotificationStatus.FRIEND_REQUEST);

                friendRepository.delete(friendshipOpt.get());
            } else {
                throw new IllegalArgumentException("Không tìm thấy mối quan hệ bạn bè giữa hai người dùng");
            }
        }
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