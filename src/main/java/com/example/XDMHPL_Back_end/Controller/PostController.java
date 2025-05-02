package com.example.XDMHPL_Back_end.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.XDMHPL_Back_end.DTO.PostDTO;
import com.example.XDMHPL_Back_end.DTO.RequestNotificationDTO;
import com.example.XDMHPL_Back_end.Services.PostService;
import com.example.XDMHPL_Back_end.model.Post;

@RestController
@RequestMapping("/posts")
public class PostController {
	@Autowired
	private PostService postService;

	@GetMapping
	public List<PostDTO> getAllPosts() {
		List<Post> posts = postService.getAllPost();
		return posts.stream().map(PostDTO::fromEntity).toList();
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
			return new ResponseEntity<>("Bài đăng đã tạo:", HttpStatus.CREATED);
		} catch (IOException e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể tạo bài đăng: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<?> updatePost(
			@RequestParam("userId") int userId,
			@RequestParam("postId") int postId,
			@RequestParam("content") String content,
			@RequestParam("type") String type,
			@RequestParam(value = "keepMediaIds", required = false) List<Integer> keepMediaIds,
			@RequestParam(value = "mediaTypes", required = false) List<String> mediaTypes,
			@RequestParam(value = "mediaFiles", required = false) List<MultipartFile> mediaFiles) {

		try {
			PostDTO updatedPost = postService.updatePost(userId, postId, content, type, keepMediaIds, mediaTypes,
					mediaFiles);
			return new ResponseEntity<>("Bài đăng đã cập nhật thành công", HttpStatus.OK);
		} catch (IOException e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể cập nhật bài đăng: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/like/{postId}/{userId}")
	public ResponseEntity<?> likePost(@PathVariable int postId, @PathVariable int userId) {
		try {
			RequestNotificationDTO liekDto =postService.likePost(postId, userId);
			return new ResponseEntity<>(liekDto, HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể tương tác bài đăng: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public Post getPostByID(@PathVariable Integer id) {
		return postService.getPostByID(id);
	}


	@DeleteMapping("delete/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable int postId) {
		try {
			postService.deletePost(postId);
			return new ResponseEntity<>("Bài đăng đã xóa thành công", HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể xóa bài đăng: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
