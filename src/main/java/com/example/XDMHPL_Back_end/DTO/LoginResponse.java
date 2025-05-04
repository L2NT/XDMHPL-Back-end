package com.example.XDMHPL_Back_end.DTO;

import java.util.List;

import com.example.XDMHPL_Back_end.model.ChatBoxDetail;
import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.Follower;
import com.example.XDMHPL_Back_end.model.Following;
import com.example.XDMHPL_Back_end.model.Friend;
import com.example.XDMHPL_Back_end.model.Like;
import com.example.XDMHPL_Back_end.model.Notification;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String sessionId;
    private UserDTO user;
    private String message;

}