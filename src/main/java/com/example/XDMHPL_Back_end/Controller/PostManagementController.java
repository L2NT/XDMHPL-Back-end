package com.example.XDMHPL_Back_end.Controller;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.UrlResource;

import com.example.XDMHPL_Back_end.DTO.PostDTO;
import com.example.XDMHPL_Back_end.Repositories.PostRepository;
import com.example.XDMHPL_Back_end.Services.PostManagementService;
import com.example.XDMHPL_Back_end.model.Post;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
@RestController
@RequestMapping("/posts-management")
public class PostManagementController {

	private final PostRepository postRepository;
	private final PostManagementService postManagementService;

	public PostManagementController(PostRepository postRepository, PostManagementService postManagementService) {
		this.postRepository = postRepository;
		this.postManagementService = postManagementService;
	}

	@GetMapping("/get-all")
	public List<PostDTO> getAllPosts() {
		List<Post> posts = postRepository.findAll();
		return posts.stream().map(PostDTO::fromEntity).toList();
	}

	@GetMapping("/visibility/{postID}/{hide}")
	public ResponseEntity<String> updatePostVisibility(@PathVariable int postID, @PathVariable int hide) {
	    // Chuyển đổi hide từ int sang boolean
	    boolean result = postManagementService.updatePostVisibility(postID, hide);

	    if (result) {
	        // Trả về kết quả thành công
	        return ResponseEntity.ok("Post visibility updated: postID = " + postID + ", hide = " + hide);
	    } else {
	        // Nếu không tìm thấy bài viết, trả về HTTP 404
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Post with ID " + postID + " not found.");
	    }
	}
	
	@DeleteMapping("/delete/{postID}")
    public ResponseEntity<String> deletePost(@PathVariable int postID) {
        boolean isDeleted = postManagementService.deletePost(postID);

        if (isDeleted) {
            return ResponseEntity.ok("Post with ID " + postID + " has been successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post with ID " + postID + " not found.");
        }
    }
	
	@DeleteMapping("/delete-comment/{commentID}")
	public ResponseEntity<String> deleteComment(@PathVariable int commentID){
		boolean isDeleted = postManagementService.deleteComment(commentID);
		if (isDeleted) {
            return ResponseEntity.ok("Comment with ID " + commentID + " has been successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Comment with ID " + commentID + " not found.");
        }
		
	}
	
	
	private final String VIDEO_DIRECTORY = "src/main/resources/static/uploads/postvideo/";
    private final String IMAGE_DIRECTORY = "src/main/resources/static/uploads/postimage/";
    private final String AVATAR = "src/main/resources/static/uploads/avatars/";
    
    @GetMapping("/media/{type}/{filename}")
    public ResponseEntity<Resource> getMedia(@PathVariable String type, @PathVariable String filename) {
        try {
            // Xác định thư mục và MIME type dựa trên loại media (video hoặc image)
            String directory;
            String contentType;

            if (type.equalsIgnoreCase("video")) {
                directory = VIDEO_DIRECTORY;
                contentType = getVideoContentType(filename);
            } else if (type.equalsIgnoreCase("image")) {
                directory = IMAGE_DIRECTORY;
                contentType = getImageContentType(filename);
            } else if (type.equalsIgnoreCase("avatar")) {
            	directory = AVATAR;
                contentType = getImageContentType(filename);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Loại không hợp lệ
            }

            // Xây dựng đường dẫn tới file
            Path filePath = Paths.get(directory).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Kiểm tra file có tồn tại và đọc được không
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, contentType) // Đặt Content-Type phù hợp
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Không tìm thấy file
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Lỗi khi tải file
        }
    }

    // Phương thức xác định Content-Type cho video
    private String getVideoContentType(String filename) {
        if (filename.toLowerCase().endsWith(".mp4")) {
            return "video/mp4";
        } else if (filename.toLowerCase().endsWith(".avi")) {
            return "video/x-msvideo";
        } else if (filename.toLowerCase().endsWith(".mkv")) {
            return "video/x-matroska";
        } else if (filename.toLowerCase().endsWith(".mov")) {
            return "video/quicktime";
        } else if (filename.toLowerCase().endsWith(".wmv")) {
            return "video/x-ms-wmv";
        }
        return "application/octet-stream"; // Mặc định cho loại không xác định
    }

    // Phương thức xác định Content-Type cho ảnh
    private String getImageContentType(String filename) {
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (filename.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (filename.toLowerCase().endsWith(".bmp")) {
            return "image/bmp";
        } else if (filename.toLowerCase().endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream"; // Mặc định cho loại không xác định
    }
}