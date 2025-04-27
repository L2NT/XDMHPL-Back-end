package com.example.XDMHPL_Back_end.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.XDMHPL_Back_end.Repositories.FriendRepository;
import com.example.XDMHPL_Back_end.Services.FriendService;

@RestController
@RequestMapping("/friendrequests")
public class FriendController {
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private FriendService friendService;


    @PostMapping("/{senderId}/{receiverId}")
	public ResponseEntity<?> sentRequestFriend(@PathVariable int receiverId, @PathVariable int senderId) {
		try {
			friendService.sentFriendRequest(senderId, receiverId);
			return new ResponseEntity<>("Đã thay đổi tương tác bạn bè:", HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể tương tác bạn bè: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


    @PutMapping("/{senderId}/{receiverId}")
	public ResponseEntity<?> acceptFriend(@PathVariable int receiverId, @PathVariable int senderId) {
		try {
			friendService.acceptFriend(senderId, receiverId);
			return new ResponseEntity<>("Đã thay đổi tương tác bạn bè:", HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể tương tác bạn bè: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


    @DeleteMapping("/{senderId}/{receiverId}")
	public ResponseEntity<?> deleteFriend(@PathVariable int receiverId, @PathVariable int senderId) {
		try {
			friendService.deleteFriend(senderId, receiverId);
			return new ResponseEntity<>("Đã thay đổi tương tác bạn bè:", HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể tương tác bạn bè: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
