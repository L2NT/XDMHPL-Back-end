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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.XDMHPL_Back_end.DTO.CommentDTO;
import com.example.XDMHPL_Back_end.DTO.NotificationDTO;
import com.example.XDMHPL_Back_end.Services.CommentService;
import com.example.XDMHPL_Back_end.model.Comment;




@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/{userId}")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO comment, @PathVariable("postId") Integer postId, @PathVariable("userId") Integer userId )
    {
        try {
            System.out.println(postId);
		 	NotificationDTO commentDTO = commentService.createComment(comment, postId, userId);
			return new ResponseEntity<>(commentDTO, HttpStatus.OK);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Không thể tương tác bài đăng: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @PutMapping("/api/comments/like/{commentId}")
    public Comment LikeComment( 
        @RequestHeader("Authorization") String jwt, @PathVariable("commentId") Integer commentId) throws Exception
    {
        // User user=userService.findUserByJwt(jwt);
        // Comment likedComment = commentService.likeComment(commentId, user.getId());
        // return likedComment;
        return null;
    }


    @PutMapping("/update/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO comment, @PathVariable("commentId") Integer commentId)
    {
        try {
           commentService.updateComment(comment, commentId);
            return new ResponseEntity<>("Đã thay đổi bình luận:", HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Không thể tương tác bài đăng: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>("Bình luận đã xóa thành công", HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Không thể xóa bài đăng: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
