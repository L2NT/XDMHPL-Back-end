package com.example.XDMHPL_Back_end.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.XDMHPL_Back_end.Services.PostShareService;
import com.example.XDMHPL_Back_end.Services.UserService;
import com.example.XDMHPL_Back_end.model.PostShare;
import com.example.XDMHPL_Back_end.model.Users;

@RestController
@RequestMapping("/postshare")
public class PostShareController {
	@Autowired
	private PostShareService postShareService;
	@Autowired
	private UserService userService;

	@PostMapping("/create")
	public ResponseEntity<?> addPost(@RequestBody Map<String, Object> values) {
		Integer originalPostID = (Integer) values.get("originalPostID");
		Integer userID = (Integer) values.get("userID");
		Integer parentShareID = (Integer) values.get("parentShareID");
		String caption = (String) values.get("caption");
	
		Users sharedByUser = userService.getUserById(userID);
		try {
			postShareService.createPostShare(originalPostID, parentShareID, caption, sharedByUser);
			return new ResponseEntity<>("Bài đăng đã tạo:", HttpStatus.CREATED);
		} catch (IllegalArgumentException e) { 
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể tạo bài đăng: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Đã xảy ra lỗi: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public PostShare getPostShareByID(@PathVariable Integer id) {
		return postShareService.getPostShareByID(id);
	}

}
