package com.example.XDMHPL_Back_end.Services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.Repositories.PostRepository;
import com.example.XDMHPL_Back_end.Repositories.PostShareRepository;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.PostShare;
import com.example.XDMHPL_Back_end.model.Users;

@Service
public class PostShareService {
    @Autowired
    private PostShareRepository postShareRepository;

    @Autowired
    private PostRepository postRepository;

    public List<PostShare> getAllPostShare() {
        return postShareRepository.findAll();
    }

    public PostShare createPostShare(Integer originalPostID, Integer parentShareID, String caption,
            Users sharedByUser) {
        // Kiểm tra bài đăng gốc (originalPost) có tồn tại
        Post originalPost = postRepository.findById(originalPostID)
                .orElseThrow(() -> new IllegalArgumentException("Original post not found with ID: " + originalPostID));

        // Tạo biến mới để lưu giá trị parentShareID thực tế
        Integer actualParentShareID = parentShareID;
        // Nếu có parentShareID, kiểm tra bài đăng cha (parentPost) có tồn tại
        Post parentPost = null;
        if (parentShareID != null) {
            parentPost = postRepository.findById(parentShareID)
                    .orElseThrow(() -> new IllegalArgumentException("Parent post not found with ID: " + parentShareID));
        } else {
            // Nếu không có parentShareID, bài đăng gốc chính là bài đăng cha
            parentPost = originalPost;
            actualParentShareID = originalPostID;
        }

        // Tạo đối tượng PostShare
        PostShare postShare = new PostShare();
        postShare.setOriginalPostID(originalPostID);
        postShare.setParentShareID(actualParentShareID);
        postShare.setContent(caption); // Nội dung chia sẻ
        postShare.setUser(sharedByUser); // Người dùng chia sẻ
        postShare.setCreationDate(new Date(System.currentTimeMillis())); // Ngày chia sẻ hiện tại
        postShare.setPriorityScore(0); // Mặc định priority score = 0
        postShare.setHide(0); // Mặc định không ẩn

        // Gán các mối quan hệ
        postShare.setOriginalPost(originalPost);
        postShare.setParentPost(parentPost); // Đã đổi tên từ parentShare -> parentPost

        // Lưu PostShare vào cơ sở dữ liệu
        return postShareRepository.save(postShare);
    }

    public PostShare getPostShareByID(Integer id) {
        return postShareRepository.findById(id).orElse(null);
    }
}
