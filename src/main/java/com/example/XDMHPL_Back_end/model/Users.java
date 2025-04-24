package com.example.XDMHPL_Back_end.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userID;

	@Column(name = "`FullName`", nullable = true, unique = false)
	private String fullName;

	@Column(name = "UserName", nullable = true, unique = true)
	private String userName;

	@Column(name = "Email", nullable = true, unique = true)
	private String email;

	@Column(name = "Password", nullable = true)
	private String password;

	@Column(name = "AvatarURL", nullable = true)
	private String avatar;
	
	@Column(name = "PhoneNumber", nullable = true)
	private String phoneNumber;
	
	@Column(name = "DateOfBirth", nullable = true)
	private LocalDate dateOfBirth;
	
	@Column(name = "Gender", nullable = true)
	private String gender;
	
	@Column(name = "CoverPhotoURL", nullable = true)
	private String coverPhotoURL;
	
	@Column(name = "SessionID", nullable = true)
	private String sessionID;

	@Column(name ="Bio", nullable = true)
	private String Bio;
	
	@Column(name = "Role", nullable = true)
	private String role;
	
	@Column(name = "Hide", nullable = true)
	private boolean hide;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Notification> notifications; // Danh sách thông báo nhận được

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> sentNotifications; // Danh sách thông báo đã gửi

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Friend> friends; // Danh sách bạn bè

	@OneToMany(mappedBy = "friendUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Friend> friendOf; // Danh sách người mà user là bạn bè

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Follower> followers; // Danh sách người theo dõi

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Following> followings; // Danh sách người đang theo dõi

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Session> sessions; // Danh sách đăng nhập trên thiết bị

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ChatBoxDetail> chatBoxDetails; // Danh sách các ChatBoxDetail liên kết với User

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Like> Likes; // Danh sách các bài đã likes của User

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Comment> Comments; // Danh sách các bài đã likes của User
}
