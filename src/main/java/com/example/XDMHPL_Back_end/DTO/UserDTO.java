package com.example.XDMHPL_Back_end.DTO;

import java.sql.Date;
import java.time.LocalDate;

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
        return dto;
    }
    
    // Phương thức chuyển đổi từ DTO sang Entity
    public Users toEntity() {
        Users user = new Users();
        user.setUserID(this.userID);
        user.setFullName(this.fullName);
        user.setUserName(this.username);
        user.setEmail(this.email);
        user.setAvatar(this.avatarURL);
        user.setPhoneNumber(this.phoneNumber);
        user.setDateOfBirth(this.dateOfBirth);
        user.setGender(this.gender);
        user.setCoverPhotoURL(this.coverPhotoUrl);
        user.setBio(this.bio);
        user.setRole(this.role);
        return user;
    }
}
