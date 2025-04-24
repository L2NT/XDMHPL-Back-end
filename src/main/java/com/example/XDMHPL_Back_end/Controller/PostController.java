package com.example.XDMHPL_Back_end.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.XDMHPL_Back_end.DTO.PostDTO;
import com.example.XDMHPL_Back_end.Services.PostService;
import com.example.XDMHPL_Back_end.model.Post;

@RestController
@RequestMapping("/posts")
public class PostController {
	@Autowired
	private PostService postService;

	@GetMapping
	public List<Post> getCustomers() {
		return postService.getAllPost();
	}

	@PostMapping("/create")
	public ResponseEntity<?> createPost(
			@RequestParam("userId") int userId, // Sửa từ userID thành userId
			@RequestParam("content") String content,
			@RequestParam("type") String type,
			@RequestParam(value = "mediaTypes", required = false) List<String> mediaTypes,
			@RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles) {

		try {
			PostDTO createdPost = postService.createPost(userId, content, type, mediaTypes, mediaFiles);
			return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
		} catch (IOException e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể tạo bài đăng: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public Post getPostByID(@PathVariable Integer id) {
		return postService.getPostByID(id);
	}

}
