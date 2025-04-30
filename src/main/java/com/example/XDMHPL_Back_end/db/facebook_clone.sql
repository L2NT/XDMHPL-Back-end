-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 18, 2025 lúc 06:41 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

DROP DATABASE IF EXISTS facebook_clone;
CREATE DATABASE facebook_clone;
USE `facebook_clone`;
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
  `ChatBoxID` int(11) NOT NULL,
  `ImageURL` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ChatBoxName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `Mute` tinyint(4) DEFAULT NULL,
  `Block` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chatboxdetail`
--

CREATE TABLE `chatboxdetail` (
  `UserID` int(11) NOT NULL,
  `ChatBoxID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `comments`
--

CREATE TABLE `comments` (
  `CommentID` int(11) NOT NULL,
  `CreationDate` date DEFAULT NULL,
  `UserID` int(11) DEFAULT NULL,
  `PostID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `followers`
--

CREATE TABLE `followers` (
  `FollowerID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `following`
--

CREATE TABLE `following` (
  `FollowingID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `friends`
--


-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `likes`
--

CREATE TABLE `likes` (
  `LikeID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL,
  `PostID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message`
--

CREATE TABLE `message` (
  `MessageID` int(11) NOT NULL,
  `Text` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `Time` datetime DEFAULT NULL,
  `Seen` tinyint(4) DEFAULT NULL,
  `Display` tinyint(4) DEFAULT NULL,
  `ChatBoxID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `messagemedia`
--

CREATE TABLE `messagemedia` (
  `MessageMediaID` int(11) NOT NULL,
  `MediaType` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MediaURL` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MessageID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notification`
--

CREATE TABLE `notification` (
  `NotificationID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL,
  `SenderID` int(11) DEFAULT NULL,
  `Type` enum('LIKE','COMMENT','MESSAGE','FRIEND_REQUEST','FOLLOW') DEFAULT NULL,
  `PostID` int(11) DEFAULT NULL,
  `CommentID` int(11) DEFAULT NULL,
  `MessageID` int(11) DEFAULT NULL,
  `Content` text DEFAULT NULL,
  `CreatedAt` datetime(3) DEFAULT NULL,
  `IsReadFlag` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `post`
--

CREATE TABLE `post` (
  `PostID` int(11) NOT NULL,
  `CreationDate` date DEFAULT NULL,
  `Type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `UserID` int(11) DEFAULT NULL,
  `Content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `PriorityScore` int(11) DEFAULT 0,
  `Hide` tinyint (4) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `post`
--

INSERT INTO `post` (`PostID`, `CreationDate`, `Type`, `UserID`, `Content`, `PriorityScore`) VALUES
(1, '2025-04-16', 'image', 1, 'dìa dia', 0),
(2, '2025-04-16', 'video', 1, 'hehe', 0),
(3, '2025-04-16', 'video', 1, 'hehe', 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `postmedia`
--

CREATE TABLE `postmedia` (
  `PostMediaID` int(11) NOT NULL,
  `Type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `MediaURL` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `PostID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `postmedia`
--

INSERT INTO `postmedia` (`PostMediaID`, `Type`, `MediaURL`, `PostID`) VALUES
(1, 'image', NULL, 1),
(2, 'Video', NULL, 2),
(3, 'Video', NULL, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `postshare`
--

CREATE TABLE `postshare` (
  `ShareID` int(11) NOT NULL,
  `OriginalPostID` int(11) DEFAULT NULL,
  `SharedByUserID` int(11) DEFAULT NULL,
  `ShareDate` date DEFAULT NULL,
  `Content` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `postshare`
--

INSERT INTO `postshare` (`ShareID`, `OriginalPostID`, `SharedByUserID`, `ShareDate`, `Content`) VALUES
(1, 1, 1, '2025-04-16', 'aaaa'),
(2, 1, 1, '2025-04-16', 'aaaa');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sessions`
--

CREATE TABLE `sessions` (
  `SessionID` varchar(255) NOT NULL,
  `UserID` int(11) NOT NULL,
  `CreatedAt` datetime DEFAULT current_timestamp(),
  `ExpiresAt` datetime DEFAULT NULL,
  `DeviceInfo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `UserID` int(11) NOT NULL UNIQUE,
  `FullName` mediumtext DEFAULT NULL,
  `Username` varchar(50) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `AvatarURL` varchar(200) DEFAULT NULL,
  `PhoneNumber` varchar(10) DEFAULT NULL,
  `DateOfBirth` date DEFAULT NULL,
  `Gender` varchar(10) DEFAULT NULL,
  `CoverPhotoUrl` varchar(200) DEFAULT NULL,
  `SessionID` varchar(255) DEFAULT NULL,
  `Bio` varchar(255) DEFAULT NULL,
  `Hide` tinyint (4) DEFAULT 0,
  `Role` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `friends` (
    FriendID int(11) AUTO_INCREMENT primary key,
    UserID int(11) NOT NULL,
    FriendUserID int(11) NOT NULL,
    Status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (UserID) REFERENCES users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (FriendUserID) REFERENCES users(UserID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`UserID`, `FullName`, `Username`, `Password`, `Email`, `AvatarURL`, `PhoneNumber`, `DateOfBirth`, `Gender`, `CoverPhotoUrl`, `Bio`, `SessionID`, `Role`) VALUES
(1, 'Tống Thành Đạt', 'tongthanhdat001', 'matkhau123', 'a@gmail.com', NULL, '0395632026', '1990-01-01', 'Nam', NULL, '🌟 "Sống là không chờ đợi"', 'NULL', 'user'),
(42, 'TốngThành Đạt', 'tongthanhdat009', '$2a$10$0cZlYxjf4/fS1VczEYHyTOHLQ0RdWZFkrWxxKyOEixIYd3fMBqACq', 'gamingthanhdat@gmail.com', NULL, '0395632027', '2000-04-17', 'Nam', NULL, '🌍 "Yêu thích công nghệ và khám phá"', NULL, 'user');


--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chatbox`
--
ALTER TABLE `chatbox`
  ADD PRIMARY KEY (`ChatBoxID`);

--
-- Chỉ mục cho bảng `chatboxdetail`
--
ALTER TABLE `chatboxdetail`
  ADD PRIMARY KEY (`UserID`,`ChatBoxID`),
  ADD KEY `FK_ChatBoxDetail_ChatBox` (`ChatBoxID`);

--
-- Chỉ mục cho bảng `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`CommentID`),
  ADD KEY `FK_Comments_Post` (`PostID`),
  ADD KEY `FK_Comments_Users` (`UserID`);

--
-- Chỉ mục cho bảng `followers`
--
ALTER TABLE `followers`
  ADD PRIMARY KEY (`FollowerID`),
  ADD KEY `FK_Followers_Users` (`UserID`);

--
-- Chỉ mục cho bảng `following`
--
ALTER TABLE `following`
  ADD PRIMARY KEY (`FollowingID`),
  ADD KEY `FK_Following_Users` (`UserID`);

--

--
-- Chỉ mục cho bảng `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`LikeID`),
  ADD KEY `FK_Likes_Post` (`PostID`),
  ADD KEY `FK_Likes_Users` (`UserID`);

--
-- Chỉ mục cho bảng `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`MessageID`),
  ADD KEY `FK_Message_ChatBox` (`ChatBoxID`);

--
-- Chỉ mục cho bảng `messagemedia`
--
ALTER TABLE `messagemedia`
  ADD PRIMARY KEY (`MessageMediaID`),
  ADD KEY `FK_MessageMedia_Message` (`MessageID`);

--
-- Chỉ mục cho bảng `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`NotificationID`),
  ADD KEY `FK_Notification_Comments` (`CommentID`),
  ADD KEY `FK_Notification_Message` (`MessageID`),
  ADD KEY `FK_Notification_Post` (`PostID`),
  ADD KEY `FK_Notification_Sender` (`SenderID`),
  ADD KEY `FK_Notification_Users` (`UserID`);

--
-- Chỉ mục cho bảng `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`PostID`),
  ADD KEY `FK_Post_Users` (`UserID`);

--
-- Chỉ mục cho bảng `postmedia`
--
ALTER TABLE `postmedia`
  ADD PRIMARY KEY (`PostMediaID`),
  ADD KEY `FK_PostMedia_Post` (`PostID`);

--
-- Chỉ mục cho bảng `postshare`
--
ALTER TABLE `postshare`
  ADD PRIMARY KEY (`ShareID`),
  ADD KEY `OriginalPostID` (`OriginalPostID`),
  ADD KEY `SharedByUserID` (`SharedByUserID`);

--
-- Chỉ mục cho bảng `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`SessionID`),
  ADD KEY `UserID` (`UserID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chatbox`
--
ALTER TABLE `chatbox`
  MODIFY `ChatBoxID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `comments`
--
ALTER TABLE `comments`
  MODIFY `CommentID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `followers`
--
ALTER TABLE `followers`
  MODIFY `FollowerID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `following`
--
ALTER TABLE `following`
  MODIFY `FollowingID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `friends`
--
ALTER TABLE `friends`
  MODIFY `FriendID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `likes`
--
ALTER TABLE `likes`
  MODIFY `LikeID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `message`
--
ALTER TABLE `message`
  MODIFY `MessageID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `messagemedia`
--
ALTER TABLE `messagemedia`
  MODIFY `MessageMediaID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `notification`
--
ALTER TABLE `notification`
  MODIFY `NotificationID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `post`
--
ALTER TABLE `post`
  MODIFY `PostID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `postmedia`
--
ALTER TABLE `postmedia`
  MODIFY `PostMediaID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `postshare`
--
ALTER TABLE `postshare`
  MODIFY `ShareID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chatboxdetail`
--
ALTER TABLE `chatboxdetail`
  ADD CONSTRAINT `FK_ChatBoxDetail_ChatBox` FOREIGN KEY (`ChatBoxID`) REFERENCES `chatbox` (`ChatBoxID`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_ChatBoxDetail_Users` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `FK_Comments_Post` FOREIGN KEY (`PostID`) REFERENCES `post` (`PostID`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_Comments_Users` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `followers`
--
ALTER TABLE `followers`
  ADD CONSTRAINT `FK_Followers_Users` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `following`
--
ALTER TABLE `following`
  ADD CONSTRAINT `FK_Following_Users` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `friends`
--
ALTER TABLE `friends`
  ADD CONSTRAINT `FK_Friends_Users` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `likes`
--
ALTER TABLE `likes`
  ADD CONSTRAINT `FK_Likes_Post` FOREIGN KEY (`PostID`) REFERENCES `post` (`PostID`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_Likes_Users` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FK_Message_ChatBox` FOREIGN KEY (`ChatBoxID`) REFERENCES `chatbox` (`ChatBoxID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `messagemedia`
--
ALTER TABLE `messagemedia`
  ADD CONSTRAINT `FK_MessageMedia_Message` FOREIGN KEY (`MessageID`) REFERENCES `message` (`MessageID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `FK_Notification_Comments` FOREIGN KEY (`CommentID`) REFERENCES `comments` (`CommentID`) ON DELETE SET NULL,
  ADD CONSTRAINT `FK_Notification_Message` FOREIGN KEY (`MessageID`) REFERENCES `message` (`MessageID`) ON DELETE SET NULL,
  ADD CONSTRAINT `FK_Notification_Post` FOREIGN KEY (`PostID`) REFERENCES `post` (`PostID`) ON DELETE SET NULL,
  ADD CONSTRAINT `FK_Notification_Sender` FOREIGN KEY (`SenderID`) REFERENCES `users` (`UserID`) ON DELETE SET NULL,
  ADD CONSTRAINT `FK_Notification_Users` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `FK_Post_Users` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `postmedia`
--
ALTER TABLE `postmedia`
  ADD CONSTRAINT `FK_PostMedia_Post` FOREIGN KEY (`PostID`) REFERENCES `post` (`PostID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `postshare`
--
ALTER TABLE `postshare`
  ADD CONSTRAINT `postshare_ibfk_1` FOREIGN KEY (`OriginalPostID`) REFERENCES `post` (`PostID`),
  ADD CONSTRAINT `postshare_ibfk_2` FOREIGN KEY (`SharedByUserID`) REFERENCES `users` (`UserID`);

--
-- Các ràng buộc cho bảng `sessions`
--
ALTER TABLE `sessions`
  ADD CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;