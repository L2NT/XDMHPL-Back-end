package com.example.XDMHPL_Back_end.DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.XDMHPL_Back_end.model.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int userID;
    private String fullName;
    private String username;
    private String email;
    private String avatarURL;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String coverPhotoUrl;
    private String bio;
    private String role;
    private String password;
    private boolean hide;
    // Danh sách bạn bè
    private List<FriendDTO> friends;
    private List<FriendDTO> friendOf;

    // Danh sách người theo dõi và đang theo dõi
    // private List<Integer> followers;
    // private List<Integer> followings;

    // Danh sách session
    // private List<String> sessions;

    // Constructors, getters, setters

    // Phương thức chuyển đổi từ Entity sang DTO
    public static UserDTO fromEntity(Users user) {
        UserDTO dto = new UserDTO();
        dto.setUserID(user.getUserID());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setAvatarURL(user.getAvatar());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());
        dto.setCoverPhotoUrl(user.getCoverPhotoURL());
        dto.setBio(user.getBio());
        dto.setRole(user.getRole());
        dto.setHide(user.getHide());
        dto.setPassword(user.getPassword());

        // Chuyển đổi danh sách friends
        dto.setFriends(user.getFriends().stream()
                .map(friend -> new FriendDTO(
                        friend.getFriendID(),
                        friend.getStatus().toString(),
                        friend.getCreatedAt(),
                        friend.getFriendUser().getUserID(),
                        friend.getFriendUser().getFullName(),
                        friend.getFriendUser().getUserName(),
                        friend.getFriendUser().getEmail()))
                .collect(Collectors.toList()));

        // Chuyển đổi danh sách friendOf
        dto.setFriendOf(user.getFriendOf().stream()
                .map(friend -> new FriendDTO(
                        friend.getFriendID(),
                        friend.getStatus().toString(),
                        friend.getCreatedAt(),
                        friend.getUser().getUserID(),
                        friend.getUser().getFullName(),
                        friend.getUser().getUserName(),
                        friend.getUser().getEmail()))
                .collect(Collectors.toList()));

        return dto;
    }
}
