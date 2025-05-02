package com.example.XDMHPL_Back_end.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.DTO.UserDTO;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.Users;


@Service
public class UserService {
	@Autowired
    private UserRepository usersRepository;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Users> getAllUser() {
        return usersRepository.findAll();
    }

    
    public Users createUser(String fullName, String userName, String password, String email, String avatar, String phoneNumber, LocalDate dateOfBirth, String gender
			, String coverPhotoURL, String sessionID, String role) {
        String encryptedPassword = passwordEncoder.encode(password);
    	Users user = new Users();
        user.setFullName(fullName);
        user.setUserName(userName);
        user.setPassword(encryptedPassword);
        user.setEmail(email);
        user.setAvatar(avatar);
        user.setPhoneNumber(phoneNumber);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(gender);
        user.setCoverPhotoURL(coverPhotoURL);
        user.setSessionID(sessionID);
        user.setRole(role);
        user.setHide(false); // Default value for hide
        user.setBio(null); // Default value for bio
        return usersRepository.save(user);
    }
    
    public Users getUserById(Integer id) {
        return usersRepository.findById(id).orElse(null);
    }
    
    public Users getUserByUsername(String userName){
    	return usersRepository.findByUserName(userName);
    }
    
    public Users getUserByPhoneNumber(String phoneNumber){
    	return usersRepository.findByPhoneNumber(phoneNumber);
    }
    
    public Users getUserByEmail(String email) {
    	return usersRepository.findByEmail(email);
    }
    
    public Users loginValidate(String userIdentifier, String password, String role) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        userIdentifier = userIdentifier.trim();

        Users user = userIdentifier.matches(emailRegex)
            ? usersRepository.findByEmail(userIdentifier)
            : usersRepository.findByUserName(userIdentifier);

        if (user == null) {
            return null;
        }

        if (passwordEncoder.matches(password, user.getPassword()) && user.getRole().equalsIgnoreCase(role)) {
            return user;
        }

        return null;
    }

    // Hàm để cập nhật avatar
    public Users updateUserAvatar(Integer id, String avatarPath) {
        Users user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAvatar(avatarPath);
        return usersRepository.save(user);
    }
    // Hàm để cập nhật ảnh bìa
    public void updateUserCoverPhoto(Integer userId, String coverPhotoPath) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setCoverPhotoURL(coverPhotoPath);
        usersRepository.save(user);
    }

    public Users updateBio(int userID, String newBio) {
        // Tìm người dùng theo userID
        Users user = usersRepository.findById(userID).orElse(null);
        if (user != null) {
            // Cập nhật tiểu sử mới
            user.setBio(newBio);
            return usersRepository.save(user);
        }
        return null;
    }
    public Users updateProfile(int userID, Users userDetails) {
        // Tìm người dùng theo userID
        Users user = usersRepository.findById(userID).orElse(null);
        if (user != null) {
            // Cập nhật thông tin người dùng mới
            user.setFullName(userDetails.getFullName());
            user.setEmail(userDetails.getEmail());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setDateOfBirth(userDetails.getDateOfBirth());
            user.setGender(userDetails.getGender());
            return usersRepository.save(user);
        }
        return null;
    }
    public boolean hideUserById(int userId) {
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setHide(true);
            usersRepository.save(user);
            return true;
        }
        return false;
    }

    public List<Users> findUsersByHideFalse() {
        return usersRepository.findByHideFalse();
    }

    public Users updateUserRole(int userId, String role) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return usersRepository.save(user);
    }


    



    //current user
    public UserDTO getCurrentUser(int userId) {
        Users user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            return UserDTO.fromEntity(user);
        }
        return null;
    }






    //user online
    public void updateOnlineStatus(int userId, boolean isOnline) {
        Users user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setIsOnline(isOnline);
            usersRepository.save(user);
        }
    }
}
