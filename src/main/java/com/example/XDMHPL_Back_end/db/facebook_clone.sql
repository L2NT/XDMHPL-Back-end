-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 27, 2025 at 01:35 PM
-- Server version: 8.0.41
-- PHP Version: 8.3.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `facebook_clone`
--

-- --------------------------------------------------------

--
-- Table structure for table `chatbox`
--

CREATE TABLE `chatbox` (
  `chatBoxID` int NOT NULL,
  `Block` int DEFAULT NULL,
  `ChatBoxName` varchar(255) DEFAULT NULL,
  `ImageURL` varchar(255) DEFAULT NULL,
  `Mute` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chatboxdetail`
--

CREATE TABLE `chatboxdetail` (
  `ChatBoxID` int NOT NULL,
  `UserID` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `commentID` int NOT NULL,
  `CreationDate` datetime(6) DEFAULT NULL,
  `PostID` int DEFAULT NULL,
  `UserID` int DEFAULT NULL,
  `Content` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`commentID`, `CreationDate`, `PostID`, `UserID`, `Content`) VALUES
(1, '2025-04-25 15:12:47.309000', 2, 1, 'chịu'),
(2, '2025-04-26 09:33:29.784000', 10, 1, 'ông thương ?'),
(3, '2025-04-26 09:37:11.783000', 5, 1, 'hi'),
(4, '2025-04-27 12:53:18.107000', 2, 1, '??');

-- --------------------------------------------------------

--
-- Table structure for table `followers`
--

CREATE TABLE `followers` (
  `followerID` int NOT NULL,
  `UserID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `following`
--

CREATE TABLE `following` (
  `followingID` int NOT NULL,
  `UserID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `friendID` int NOT NULL,
  `CreatedAt` date DEFAULT NULL,
  `Status` enum('ACCEPTED','PENDING') DEFAULT NULL,
  `FriendUserID` int DEFAULT NULL,
  `UserID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `friends`
--

INSERT INTO `friends` (`friendID`, `CreatedAt`, `Status`, `FriendUserID`, `UserID`) VALUES
(1, '2025-04-25', 'ACCEPTED', 2, 1),
(2, '2025-04-18', 'PENDING', 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `likes`
--

CREATE TABLE `likes` (
  `likeID` int NOT NULL,
  `PostID` int DEFAULT NULL,
  `UserID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `likes`
--

INSERT INTO `likes` (`likeID`, `PostID`, `UserID`) VALUES
(11, 10, 1),
(15, 5, 1),
(16, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `messageID` int NOT NULL,
  `Display` int DEFAULT NULL,
  `Seen` int DEFAULT NULL,
  `Text` varchar(255) DEFAULT NULL,
  `Time` date DEFAULT NULL,
  `ChatBoxID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `messagemedia`
--

CREATE TABLE `messagemedia` (
  `messageMediaID` int NOT NULL,
  `MediaType` varchar(255) DEFAULT NULL,
  `MediaURL` varchar(255) DEFAULT NULL,
  `MessageID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `notificationID` int NOT NULL,
  `Content` varchar(255) DEFAULT NULL,
  `CreatedAt` date DEFAULT NULL,
  `IsReadFlag` int DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  `CommentID` int DEFAULT NULL,
  `MessageID` int DEFAULT NULL,
  `PostID` int DEFAULT NULL,
  `SenderID` int DEFAULT NULL,
  `UserID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE `post` (
  `post_type` varchar(31) NOT NULL,
  `postID` int NOT NULL,
  `Content` varchar(255) NOT NULL,
  `CreationDate` datetime(6) NOT NULL,
  `Hide` int NOT NULL,
  `PriorityScore` int NOT NULL,
  `UserID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `post`
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
-- Table structure for table `postmedia`
--

CREATE TABLE `postmedia` (
  `postMediaID` int NOT NULL,
  `MediaURL` varchar(255) DEFAULT NULL,
  `Type` varchar(255) DEFAULT NULL,
  `PostID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `postmedia`
--

INSERT INTO `postmedia` (`postMediaID`, `MediaURL`, `Type`, `PostID`) VALUES
(3, '/uploads/postvideo/23f355e5-1cdc-40a7-bf7b-cf78fb601007.mp4', 'video', 2);

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
  `sessionID` varchar(255) NOT NULL,
  `createdAt` datetime(6) DEFAULT NULL,
  `deviceInfo` varchar(255) DEFAULT NULL,
  `expiresAt` datetime(6) DEFAULT NULL,
  `UserID` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sessions`
--

INSERT INTO `sessions` (`sessionID`, `createdAt`, `deviceInfo`, `expiresAt`, `UserID`) VALUES
('480b4b8c-f6bc-4162-836b-ef568925b2a5', '2025-04-27 09:35:43.321117', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36', '2025-04-27 11:35:43.321117', 2),
('f53d584a-5259-4109-8f84-31662d8b8ed2', '2025-04-27 11:56:38.190022', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0', '2025-04-27 13:56:38.190022', 2);

-- --------------------------------------------------------

--
-- Table structure for table `sharepost`
--

CREATE TABLE `sharepost` (
  `OriginalPostID` int NOT NULL,
  `ParentShareID` int DEFAULT NULL,
  `postID` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sharepost`
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
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userID` int NOT NULL,
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
  `UserName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `Bio`, `AvatarURL`, `CoverPhotoURL`, `DateOfBirth`, `Email`, `FullName`, `Gender`, `Hide`, `Password`, `PhoneNumber`, `Role`, `SessionID`, `UserName`) VALUES
(1, NULL, NULL, NULL, '2000-04-22', 'huynhsmash2468@gmail.com', 'HuỳnhTuấn', 'Nam', b'0', '$2a$10$uODsvkYz9zK9L5Bjnk5Z1.vV6ASaSesjNHg0xhAmF395zQqbxojNe', '0938124402', 'user', NULL, 'tuan2810'),
(2, NULL, NULL, NULL, '2000-04-21', 'abc468@gmail.com', 'Huỳnh A', 'Nam', b'0', '$2a$10$uODsvkYz9zK9L5Bjnk5Z1.vV6ASaSesjNHg0xhAmF395zQqbxojNe', '0938124321', 'user', NULL, 'abc123'),
(3, NULL, NULL, NULL, '2000-03-22', 'abc123@gmail.com', 'Huỳnh C', 'Nam', b'0', '$2a$10$uODsvkYz9zK9L5Bjnk5Z1.vV6ASaSesjNHg0xhAmF395zQqbxojNe', '0939124402', 'user', NULL, 'zeus2810');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chatbox`
--
ALTER TABLE `chatbox`
  ADD PRIMARY KEY (`chatBoxID`);

--
-- Indexes for table `chatboxdetail`
--
ALTER TABLE `chatboxdetail`
  ADD PRIMARY KEY (`ChatBoxID`,`UserID`),
  ADD KEY `FKchec4y1agno5s9dr5hk416aty` (`UserID`);

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`commentID`),
  ADD KEY `FK3vha5epni7fedios55i119wj7` (`PostID`),
  ADD KEY `FKci4467lbd1ge8iaa0honrhn7h` (`UserID`);

--
-- Indexes for table `followers`
--
ALTER TABLE `followers`
  ADD PRIMARY KEY (`followerID`),
  ADD KEY `FK4mk1n1rf1vwrod7s9gp3uonwc` (`UserID`);

--
-- Indexes for table `following`
--
ALTER TABLE `following`
  ADD PRIMARY KEY (`followingID`),
  ADD KEY `FK30whdl5kql2hr2chdmrn3pm1w` (`UserID`);

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD PRIMARY KEY (`friendID`),
  ADD KEY `FK8tul4frvv417a09mondr8tyvn` (`FriendUserID`),
  ADD KEY `FKktwqd1uwxw1995yckx1ra2pcw` (`UserID`);

--
-- Indexes for table `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`likeID`),
  ADD KEY `FKrueukm07fd66xgb6ru56p5fh1` (`PostID`),
  ADD KEY `FK3oynqv7hs92poyw59hhv3bjbw` (`UserID`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`messageID`),
  ADD KEY `FKlciebyh9giq4qrp6s8bj7c41v` (`ChatBoxID`);

--
-- Indexes for table `messagemedia`
--
ALTER TABLE `messagemedia`
  ADD PRIMARY KEY (`messageMediaID`),
  ADD KEY `FKcvikldynqnuj0sb0qq3bc2vfc` (`MessageID`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notificationID`),
  ADD KEY `FKpinrnlea73rk2rfpfv6wy9whe` (`CommentID`),
  ADD KEY `FKc41vv7fokhy0phawxaoqvip1c` (`MessageID`),
  ADD KEY `FKgly4dnes9blid58otr5ukcc2t` (`PostID`),
  ADD KEY `FKe172brtfk3u3neag9qkh3xxpt` (`SenderID`),
  ADD KEY `FKmvtsv8c5vqmh5vr140eongy92` (`UserID`);

--
-- Indexes for table `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`postID`),
  ADD KEY `FKt9vxvrvsgc7f3s4njg2flsda4` (`UserID`);

--
-- Indexes for table `postmedia`
--
ALTER TABLE `postmedia`
  ADD PRIMARY KEY (`postMediaID`),
  ADD KEY `FKqylfcrstf2uhi4lbtoeuus886` (`PostID`);

--
-- Indexes for table `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`sessionID`),
  ADD KEY `FKk2cpl048i8votugqaw0iu9qr9` (`UserID`);

--
-- Indexes for table `sharepost`
--
ALTER TABLE `sharepost`
  ADD PRIMARY KEY (`postID`),
  ADD KEY `FKdwnelng3xtvx2xn5xe2yt9gck` (`OriginalPostID`),
  ADD KEY `FKa94wv3q4ll427r7f9dyclk2mr` (`ParentShareID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`),
  ADD UNIQUE KEY `UKgnfv1k6flrriv6a9jh5cja03x` (`Email`),
  ADD UNIQUE KEY `UKcbbki2qc009wc9fa641ww99pm` (`UserName`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chatbox`
--
ALTER TABLE `chatbox`
  MODIFY `chatBoxID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `commentID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `followers`
--
ALTER TABLE `followers`
  MODIFY `followerID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `following`
--
ALTER TABLE `following`
  MODIFY `followingID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `friends`
--
ALTER TABLE `friends`
  MODIFY `friendID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `likes`
--
ALTER TABLE `likes`
  MODIFY `likeID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `message`
--
ALTER TABLE `message`
  MODIFY `messageID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `messagemedia`
--
ALTER TABLE `messagemedia`
  MODIFY `messageMediaID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `notificationID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `post`
--
ALTER TABLE `post`
  MODIFY `postID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `postmedia`
--
ALTER TABLE `postmedia`
  MODIFY `postMediaID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chatboxdetail`
--
ALTER TABLE `chatboxdetail`
  ADD CONSTRAINT `FKchec4y1agno5s9dr5hk416aty` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKqvy1fppspkt4u9l0yrf35c9on` FOREIGN KEY (`ChatBoxID`) REFERENCES `chatbox` (`chatBoxID`);

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `FK3vha5epni7fedios55i119wj7` FOREIGN KEY (`PostID`) REFERENCES `post` (`postID`),
  ADD CONSTRAINT `FKci4467lbd1ge8iaa0honrhn7h` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `followers`
--
ALTER TABLE `followers`
  ADD CONSTRAINT `FK4mk1n1rf1vwrod7s9gp3uonwc` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `following`
--
ALTER TABLE `following`
  ADD CONSTRAINT `FK30whdl5kql2hr2chdmrn3pm1w` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `friends`
--
ALTER TABLE `friends`
  ADD CONSTRAINT `FK8tul4frvv417a09mondr8tyvn` FOREIGN KEY (`FriendUserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKktwqd1uwxw1995yckx1ra2pcw` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `likes`
--
ALTER TABLE `likes`
  ADD CONSTRAINT `FK3oynqv7hs92poyw59hhv3bjbw` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKrueukm07fd66xgb6ru56p5fh1` FOREIGN KEY (`PostID`) REFERENCES `post` (`postID`);

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FKlciebyh9giq4qrp6s8bj7c41v` FOREIGN KEY (`ChatBoxID`) REFERENCES `chatbox` (`chatBoxID`);

--
-- Constraints for table `messagemedia`
--
ALTER TABLE `messagemedia`
  ADD CONSTRAINT `FKcvikldynqnuj0sb0qq3bc2vfc` FOREIGN KEY (`MessageID`) REFERENCES `message` (`messageID`);

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `FKc41vv7fokhy0phawxaoqvip1c` FOREIGN KEY (`MessageID`) REFERENCES `message` (`messageID`),
  ADD CONSTRAINT `FKe172brtfk3u3neag9qkh3xxpt` FOREIGN KEY (`SenderID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKgly4dnes9blid58otr5ukcc2t` FOREIGN KEY (`PostID`) REFERENCES `post` (`postID`),
  ADD CONSTRAINT `FKmvtsv8c5vqmh5vr140eongy92` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `FKpinrnlea73rk2rfpfv6wy9whe` FOREIGN KEY (`CommentID`) REFERENCES `comments` (`commentID`);

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `FKt9vxvrvsgc7f3s4njg2flsda4` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `postmedia`
--
ALTER TABLE `postmedia`
  ADD CONSTRAINT `FKqylfcrstf2uhi4lbtoeuus886` FOREIGN KEY (`PostID`) REFERENCES `post` (`postID`);

--
-- Constraints for table `sessions`
--
ALTER TABLE `sessions`
  ADD CONSTRAINT `FKk2cpl048i8votugqaw0iu9qr9` FOREIGN KEY (`UserID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `sharepost`
--
ALTER TABLE `sharepost`
  ADD CONSTRAINT `FKa94wv3q4ll427r7f9dyclk2mr` FOREIGN KEY (`ParentShareID`) REFERENCES `post` (`postID`),
  ADD CONSTRAINT `FKdwnelng3xtvx2xn5xe2yt9gck` FOREIGN KEY (`OriginalPostID`) REFERENCES `post` (`postID`),
  ADD CONSTRAINT `FKih9sn19cnon2hbo3a4etdsum0` FOREIGN KEY (`postID`) REFERENCES `post` (`postID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
