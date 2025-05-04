-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 04, 2025 lúc 01:51 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `facebook_clone`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chatbox`
--

CREATE TABLE `chatbox` (
  `chatBoxID` int(11) NOT NULL,
  `Block` bit(1) DEFAULT NULL,
  `ChatBoxName` varchar(50) DEFAULT NULL,
  `ImageURL` varchar(200) DEFAULT NULL,
  `Mute` bit(1) DEFAULT NULL,
  `isGroup` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chatboxdetail`
--

CREATE TABLE `chatboxdetail` (
  `chatboxdetailId` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `ChatBoxID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `comments`
--

CREATE TABLE `comments` (
  `commentID` int(11) NOT NULL,
  `CreationDate` datetime(6) DEFAULT NULL,
  `PostID` int(11) DEFAULT NULL,
  `UserID` int(11) DEFAULT NULL,
  `Content` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `comments`
--

INSERT INTO `comments` (`commentID`, `CreationDate`, `PostID`, `UserID`, `Content`) VALUES
(1, '2025-04-25 15:12:47.309000', 2, 1, 'chịu'),
(2, '2025-04-26 09:33:29.784000', 10, 1, 'ông thương ?'),
(3, '2025-04-26 09:37:11.783000', 5, 1, 'hi');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `followers`
--

CREATE TABLE `followers` (
  `followerID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `following`
--

CREATE TABLE `following` (
  `followingID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `friends`
--

CREATE TABLE `friends` (
  `friendID` int(11) NOT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `Status` enum('ACCEPTED','PENDING','REJECTED') DEFAULT NULL,
  `FriendUserID` int(11) DEFAULT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `friends`
--

INSERT INTO `friends` (`friendID`, `CreatedAt`, `Status`, `FriendUserID`, `UserID`) VALUES
(1, '2025-05-04 01:56:39.000000', 'PENDING', 1, 7);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `likes`
--

CREATE TABLE `likes` (
  `likeID` int(11) NOT NULL,
  `PostID` int(11) DEFAULT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `likes`
--

INSERT INTO `likes` (`likeID`, `PostID`, `UserID`) VALUES
(11, 10, 1),
(15, 5, 1),
(16, 2, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message`
--

CREATE TABLE `message` (
  `messageID` int(11) NOT NULL,
  `Display` bit(1) DEFAULT NULL,
  `Seen` bit(1) DEFAULT NULL,
  `Text` varchar(500) DEFAULT NULL,
  `Time` datetime(6) DEFAULT NULL,
  `ChatBoxID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `messagemedia`
--

CREATE TABLE `messagemedia` (
  `messageMediaID` int(11) NOT NULL,
  `MediaType` varchar(255) DEFAULT NULL,
  `MediaURL` varchar(255) DEFAULT NULL,
  `MessageID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notification`
--

CREATE TABLE `notification` (
  `notificationID` int(11) NOT NULL,
  `Content` varchar(255) DEFAULT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `IsReadFlag` int(11) DEFAULT NULL,
  `Type` enum('COMMENT','FRIEND_REQUEST','LIKE','MESSAGE') DEFAULT NULL,
  `CommentID` int(11) DEFAULT NULL,
  `MessageID` int(11) DEFAULT NULL,
  `PostID` int(11) DEFAULT NULL,
  `SenderID` int(11) DEFAULT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `post`
--

CREATE TABLE `post` (
  `post_type` varchar(31) NOT NULL,
  `postID` int(11) NOT NULL,
  `Content` varchar(255) NOT NULL,
  `CreationDate` datetime(6) NOT NULL,
  `Hide` int(11) NOT NULL,
  `PriorityScore` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `post`
--

INSERT INTO `post` (`post_type`, `postID`, `Content`, `CreationDate`, `Hide`, `PriorityScore`, `UserID`) VALUES
('Post', 2, 'nice?', '2025-04-24 12:03:38.040000', 0, 0, 1),
('Post', 3, 'nicexu', '2025-04-24 12:03:38.040000', 0, 0, 2),
('Post', 4, 'hay', '2025-04-24 12:03:38.040000', 1, 0, 3),
('SHARE', 5, 'hay', '2025-04-25 09:45:58.250000', 0, 0, 2),
('Post', 6, 'ádsa', '2025-04-25 10:58:16.763000', 0, 0, 1),
('SHARE', 7, 'nice', '2025-04-25 11:05:37.941000', 0, 0, 1),
('SHARE', 8, '??', '2025-04-25 12:51:40.148000', 0, 0, 1),
('SHARE', 9, 'wtf ?', '2025-04-25 12:52:28.507000', 0, 0, 1),
('SHARE', 10, 'tạm', '2025-04-25 13:43:15.942000', 0, 0, 2),
('SHARE', 11, 'khá', '2025-04-25 13:44:24.560000', 0, 0, 1),
('Post', 13, 'hay', '2025-04-26 06:33:44.954000', 0, 0, 1),
('SHARE', 14, '??', '2025-04-26 10:58:47.860000', 1, 0, 1),
('SHARE', 16, 'jjbn', '2025-04-27 13:06:58.618000', 0, 0, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `postmedia`
--

CREATE TABLE `postmedia` (
  `postMediaID` int(11) NOT NULL,
  `MediaURL` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  `PostID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `postmedia`
--

INSERT INTO `postmedia` (`postMediaID`, `MediaURL`, `Type`, `PostID`) VALUES
(3, '/uploads/postvideo/23f355e5-1cdc-40a7-bf7b-cf78fb601007.mp4', 'video', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sessions`
--

CREATE TABLE `sessions` (
  `sessionID` varchar(255) NOT NULL,
  `createdAt` datetime(6) DEFAULT NULL,
  `deviceInfo` varchar(255) DEFAULT NULL,
  `expiresAt` datetime(6) DEFAULT NULL,
  `UserID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sessions`
--

INSERT INTO `sessions` (`sessionID`, `createdAt`, `deviceInfo`, `expiresAt`, `UserID`) VALUES
('40584006-1ae6-4007-996c-40db6324d92e', '2025-05-04 01:43:53.000000', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36 Edg/136.0.0.0', '2025-05-04 03:43:53.000000', 6);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sharepost`
--

CREATE TABLE `sharepost` (
  `OriginalPostID` int(11) NOT NULL,
  `ParentShareID` int(11) DEFAULT NULL,
  `postID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `sharepost`
--

INSERT INTO `sharepost` (`OriginalPostID`, `ParentShareID`, `postID`) VALUES
(2, 2, 5),
(4, 4, 7),
(4, 7, 8),
(4, 8, 9),
(2, 2, 10),
(2, 10, 11),
(2, 5, 14),
(2, 11, 16);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `userID` int(11) NOT NULL,
  `Bio` varchar(255) DEFAULT NULL,
  `AvatarURL` varchar(255) DEFAULT NULL,
  `CoverPhotoURL` varchar(255) DEFAULT NULL,
  `DateOfBirth` date DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `FullName` varchar(255) DEFAULT NULL,
  `Gender` varchar(255) DEFAULT NULL,
  `Hide` bit(1) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `PhoneNumber` varchar(255) DEFAULT NULL,
  `Role` varchar(255) DEFAULT NULL,
  `SessionID` varchar(255) DEFAULT NULL,
  `UserName` varchar(255) DEFAULT NULL,
  `is_online` bit(1) NOT NULL,
  `Token` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`userID`, `Bio`, `AvatarURL`, `CoverPhotoURL`, `DateOfBirth`, `Email`, `FullName`, `Gender`, `Hide`, `Password`, `PhoneNumber`, `Role`, `SessionID`, `UserName`, `is_online`, `Token`) VALUES
(1, NULL, NULL, NULL, '2000-04-22', 'huynhsmash2468@gmail.com', 'HuỳnhTuấn', 'Nam', b'0', '$2a$10$uODsvkYz9zK9L5Bjnk5Z1.vV6ASaSesjNHg0xhAmF395zQqbxojNe', '0938124402', 'user', NULL, 'tuan2810', b'0', ''),
(2, NULL, NULL, NULL, '2000-04-21', 'abc468@gmail.com', 'Huỳnh A', 'Nam', b'0', '$2a$10$uODsvkYz9zK9L5Bjnk5Z1.vV6ASaSesjNHg0xhAmF395zQqbxojNe', '0938124321', 'user', NULL, 'abc123', b'0', ''),
(3, NULL, NULL, NULL, '2000-03-22', 'abc123@gmail.com', 'Huỳnh C', 'Nam', b'0', '$2a$10$uODsvkYz9zK9L5Bjnk5Z1.vV6ASaSesjNHg0xhAmF395zQqbxojNe', '0939124402', 'user', NULL, 'zeus2810', b'0', ''),
(4, NULL, NULL, NULL, '2000-03-22', 'bcd123@gmail.com', 'Luffy', 'Nam', b'1', '$2a$10$uODsvkYz9zK9L5Bjnk5Z1.vV6ASaSesjNHg0xhAmF395zQqbxojNe', '0932111122', 'user', NULL, 'zac2810', b'0', ''),
(5, NULL, NULL, NULL, '2000-04-27', 'tuan123@gmail.com', 'HuỳnhTuấn', 'Nam', b'0', '$2a$10$EMcvUCQXy64Hgw/uuqux9eM043ara31pOmYmrQO.XaC3Rh3w/2Xuy', '0938124567', 'user', NULL, 'yassuo', b'0', ''),
(6, NULL, '/avatars/default.jpg', '/covers/default.jpg', '2000-02-08', 'gamingthanhdat@gmail.com', 'TốngThành Đạt', 'Nam', b'0', '$2a$10$6WOd.eG9Q2vAigQD22YMBubtaZtQP4ueLLZWTxIE/s/chci4YVrz2', '0395632222', 'admin', NULL, 'tongthanhdat009', b'0', ''),
(7, NULL, '/avatars/default.jpg', '/covers/default.jpg', '2000-02-08', 'toiladat20041405@gmail.com', 'TốngThành Đạt', 'Nam', b'0', '$2a$10$v2/3xNYxuXsip0y70IC96.ibPspuQaqQdjUbfYPtDOybU6VG5WE6O', '0395632027', 'user', NULL, 'tongthanhdat145', b'0', '7f16bdb2-9c30-4cd7-b775-e9d244c02708');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chatbox`
--
ALTER TABLE `chatbox`
  ADD PRIMARY KEY (`chatBoxID`);

--
-- Chỉ mục cho bảng `chatboxdetail`
--
ALTER TABLE `chatboxdetail`
  ADD PRIMARY KEY (`chatboxdetailId`),
  ADD KEY `UserID` (`UserID`),
  ADD KEY `ChatBoxID` (`ChatBoxID`);

--
-- Chỉ mục cho bảng `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`commentID`),
  ADD KEY `FK3vha5epni7fedios55i119wj7` (`PostID`),
  ADD KEY `FKci4467lbd1ge8iaa0honrhn7h` (`UserID`);

--
-- Chỉ mục cho bảng `followers`
--
ALTER TABLE `followers`
  ADD PRIMARY KEY (`followerID`),
  ADD KEY `FK4mk1n1rf1vwrod7s9gp3uonwc` (`UserID`);

--
-- Chỉ mục cho bảng `following`
--
ALTER TABLE `following`
  ADD PRIMARY KEY (`followingID`),
  ADD KEY `FK30whdl5kql2hr2chdmrn3pm1w` (`UserID`);

--
-- Chỉ mục cho bảng `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`friendID`),
  ADD KEY `FK8tul4frvv417a09mondr8tyvn` (`FriendUserID`),
  ADD KEY `FKktwqd1uwxw1995yckx1ra2pcw` (`UserID`);

--
-- Chỉ mục cho bảng `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`likeID`),
  ADD KEY `FKrueukm07fd66xgb6ru56p5fh1` (`PostID`),
  ADD KEY `FK3oynqv7hs92poyw59hhv3bjbw` (`UserID`);

--
-- Chỉ mục cho bảng `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`messageID`),
  ADD KEY `FKlciebyh9giq4qrp6s8bj7c41v` (`ChatBoxID`);

--
-- Chỉ mục cho bảng `messagemedia`
--
ALTER TABLE `messagemedia`
  ADD PRIMARY KEY (`messageMediaID`),
  ADD KEY `FKcvikldynqnuj0sb0qq3bc2vfc` (`MessageID`);

--
-- Chỉ mục cho bảng `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notificationID`),
  ADD KEY `FKpinrnlea73rk2rfpfv6wy9whe` (`CommentID`),
  ADD KEY `FKc41vv7fokhy0phawxaoqvip1c` (`MessageID`),
  ADD KEY `FKgly4dnes9blid58otr5ukcc2t` (`PostID`),
  ADD KEY `FKe172brtfk3u3neag9qkh3xxpt` (`SenderID`),
  ADD KEY `FKmvtsv8c5vqmh5vr140eongy92` (`UserID`);

--
-- Chỉ mục cho bảng `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`postID`),
  ADD KEY `FKt9vxvrvsgc7f3s4njg2flsda4` (`UserID`);

--
-- Chỉ mục cho bảng `postmedia`
--
ALTER TABLE `postmedia`
  ADD PRIMARY KEY (`postMediaID`),
  ADD KEY `FKqylfcrstf2uhi4lbtoeuus886` (`PostID`);

--
-- Chỉ mục cho bảng `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`sessionID`),
  ADD KEY `FKk2cpl048i8votugqaw0iu9qr9` (`UserID`);

--
-- Chỉ mục cho bảng `sharepost`
--
ALTER TABLE `sharepost`
  ADD PRIMARY KEY (`postID`),
  ADD KEY `FKdwnelng3xtvx2xn5xe2yt9gck` (`OriginalPostID`),
  ADD KEY `FKa94wv3q4ll427r7f9dyclk2mr` (`ParentShareID`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `UKgnfv1k6flrriv6a9jh5cja03x` (`Email`),
  ADD UNIQUE KEY `UKcbbki2qc009wc9fa641ww99pm` (`UserName`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chatbox`
--
ALTER TABLE `chatbox`
  MODIFY `chatBoxID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `chatboxdetail`
--
ALTER TABLE `chatboxdetail`
  MODIFY `chatboxdetailId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `comments`
--
ALTER TABLE `comments`
  MODIFY `commentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `followers`
--
ALTER TABLE `followers`
  MODIFY `followerID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `following`
--
ALTER TABLE `following`
  MODIFY `followingID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `friends`
--
ALTER TABLE `friends`
  MODIFY `friendID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `likes`
--
ALTER TABLE `likes`
  MODIFY `likeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT cho bảng `message`
--
ALTER TABLE `message`
  MODIFY `messageID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `messagemedia`
--
ALTER TABLE `messagemedia`
  MODIFY `messageMediaID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `notification`
--
ALTER TABLE `notification`
  MODIFY `notificationID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `post`
--
ALTER TABLE `post`
  MODIFY `postID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT cho bảng `postmedia`
--
ALTER TABLE `postmedia`
  MODIFY `postMediaID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chatboxdetail`
--
ALTER TABLE `chatboxdetail`
  ADD CONSTRAINT `chatboxdetail_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `chatboxdetail_ibfk_2` FOREIGN KEY (`ChatBoxID`) REFERENCES `chatbox` (`chatBoxID`);

--
-- Các ràng buộc cho bảng `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `FK3vha5epni7fedios55i119wj7` FOREIGN KEY (`PostID`) REFERENCES `post` (`postID`),
  ADD CONSTRAINT `FKci4467lbd1ge8iaa0honrhn7h` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Các ràng buộc cho bảng `followers`
--
ALTER TABLE `followers`
  ADD CONSTRAINT `FK4mk1n1rf1vwrod7s9gp3uonwc` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Các ràng buộc cho bảng `following`
--
ALTER TABLE `following`
  ADD CONSTRAINT `FK30whdl5kql2hr2chdmrn3pm1w` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Các ràng buộc cho bảng `friends`
--
ALTER TABLE `friends`
  ADD CONSTRAINT `FK8tul4frvv417a09mondr8tyvn` FOREIGN KEY (`FriendUserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKktwqd1uwxw1995yckx1ra2pcw` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Các ràng buộc cho bảng `likes`
--
ALTER TABLE `likes`
  ADD CONSTRAINT `FK3oynqv7hs92poyw59hhv3bjbw` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKrueukm07fd66xgb6ru56p5fh1` FOREIGN KEY (`PostID`) REFERENCES `post` (`postID`);

--
-- Các ràng buộc cho bảng `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FKlciebyh9giq4qrp6s8bj7c41v` FOREIGN KEY (`ChatBoxID`) REFERENCES `chatbox` (`chatBoxID`);

--
-- Các ràng buộc cho bảng `messagemedia`
--
ALTER TABLE `messagemedia`
  ADD CONSTRAINT `FKcvikldynqnuj0sb0qq3bc2vfc` FOREIGN KEY (`MessageID`) REFERENCES `message` (`messageID`);

--
-- Các ràng buộc cho bảng `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `FKc41vv7fokhy0phawxaoqvip1c` FOREIGN KEY (`MessageID`) REFERENCES `message` (`messageID`),
  ADD CONSTRAINT `FKe172brtfk3u3neag9qkh3xxpt` FOREIGN KEY (`SenderID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKgly4dnes9blid58otr5ukcc2t` FOREIGN KEY (`PostID`) REFERENCES `post` (`postID`),
  ADD CONSTRAINT `FKmvtsv8c5vqmh5vr140eongy92` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKpinrnlea73rk2rfpfv6wy9whe` FOREIGN KEY (`CommentID`) REFERENCES `comments` (`commentID`);

--
-- Các ràng buộc cho bảng `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `FKt9vxvrvsgc7f3s4njg2flsda4` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Các ràng buộc cho bảng `postmedia`
--
ALTER TABLE `postmedia`
  ADD CONSTRAINT `FKqylfcrstf2uhi4lbtoeuus886` FOREIGN KEY (`PostID`) REFERENCES `post` (`postID`);

--
-- Các ràng buộc cho bảng `sessions`
--
ALTER TABLE `sessions`
  ADD CONSTRAINT `FKk2cpl048i8votugqaw0iu9qr9` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Các ràng buộc cho bảng `sharepost`
--
ALTER TABLE `sharepost`
  ADD CONSTRAINT `FKa94wv3q4ll427r7f9dyclk2mr` FOREIGN KEY (`ParentShareID`) REFERENCES `post` (`postID`),
  ADD CONSTRAINT `FKdwnelng3xtvx2xn5xe2yt9gck` FOREIGN KEY (`OriginalPostID`) REFERENCES `post` (`postID`),
  ADD CONSTRAINT `FKih9sn19cnon2hbo3a4etdsum0` FOREIGN KEY (`postID`) REFERENCES `post` (`postID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
