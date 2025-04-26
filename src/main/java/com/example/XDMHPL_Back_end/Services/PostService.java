package com.example.XDMHPL_Back_end.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.XDMHPL_Back_end.DTO.PostDTO;
import com.example.XDMHPL_Back_end.Repositories.LikeRepository;
import com.example.XDMHPL_Back_end.Repositories.PostMediaRepository;
import com.example.XDMHPL_Back_end.Repositories.PostRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.Like;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.PostMedia;
import com.example.XDMHPL_Back_end.model.Users;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    private final String IMAGE_UPLOAD_PATH = "src/main/resources/static/uploads/postimage/";
    private final String VIDEO_UPLOAD_PATH = "src/main/resources/static/uploads/postvideo/";

    public List<Post> getAllPost() {
        return postRepository.findByHide(0);
    }

    public PostDTO createPost(int userID, String content, String type,
            List<String> mediaTypes, List<MultipartFile> mediaFiles) throws IOException {

        // Tạo một đối tượng Post mới
        Users user = userService.getUserById(userID);
        Post post = new Post();
        post.setUser(user);
        ;
        post.setContent(content);
        post.setCreationDate(new Date());
        post.setPriorityScore(0); // Giá trị mặc định
        post.setHide(0); // Không ẩn bài đăng

        // Lưu post trước để có postID
        Post savedPost = postRepository.save(post);

        // Xử lý và lưu các file media
        List<PostMedia> mediaList = new ArrayList<>();

        if (mediaFiles != null && !mediaFiles.isEmpty()) {
            for (int i = 0; i < mediaFiles.size(); i++) {
                MultipartFile file = mediaFiles.get(i);
                String mediaType = mediaTypes.get(i);

                // Xác định đường dẫn lưu trữ dựa vào loại media
                String uploadPath;
                if ("image".equals(mediaType)) {
                    uploadPath = IMAGE_UPLOAD_PATH;
                } else if ("video".equals(mediaType)) {
                    uploadPath = VIDEO_UPLOAD_PATH;
                } else {
                    continue; // Bỏ qua nếu loại không hợp lệ
                }

                // Tạo thư mục nếu chưa tồn tại
                File directory = new File(uploadPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Tạo tên file duy nhất
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID().toString() + fileExtension;
                String filePath = uploadPath + newFilename;

                // Lưu file
                Path path = Paths.get(filePath);
                Files.write(path, file.getBytes());

                // Lưu thông tin media vào database
                PostMedia postMedia = new PostMedia();
                postMedia.setType(mediaType);
                // Lưu URL tương đối để sử dụng trong ứng dụng web
                String relativeUrl = "/uploads/" +
                        (mediaType.equals("image") ? "postimage/" : "postvideo/") +
                        newFilename;
                postMedia.setMediaURL(relativeUrl);
                postMedia.setPost(savedPost);

                mediaList.add(postMediaRepository.save(postMedia));
            }
        }

        savedPost.setMediaList(mediaList);

        // Chuyển đổi sang DTO và trả về
        return PostDTO.fromEntity(savedPost);
    }

    public void likePost(int postId, int userId) {
        // Tìm bài đăng theo ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài đăng với ID: " + postId));

        // Tìm người dùng theo ID
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId));

        // Kiểm tra xem người dùng đã thích bài đăng này chưa
        Like existingLike = likeRepository.findByPostAndUser(post, user);

        if (existingLike != null) {
            // Nếu đã thích rồi, xóa like đi
            likeRepository.delete(existingLike);
        } else {
            // Nếu chưa thích, tạo like mới
            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setUser(user);
            likeRepository.save(newLike);
        }
    }

    public Post getPostByID(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public PostDTO updatePost(int userID, int postId, String content, String type,
            List<Integer> keepMediaIds, List<String> mediaTypes,
            List<MultipartFile> mediaFiles) throws IOException {

        // Lấy post hiện tại
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đăng"));

        // Kiểm tra quyền của người dùng
        if (post.getUser().getUserID() != userID) {
            throw new RuntimeException("Không có quyền cập nhật bài đăng này");
        }

        // Cập nhật nội dung
        post.setContent(content);

        if (type == "post") {
            // Xử lý media
            List<PostMedia> currentMedia = post.getMediaList();
            List<PostMedia> mediaToKeep = new ArrayList<>();
            List<PostMedia> mediaToDelete = new ArrayList<>();

            // Phân loại media: giữ lại hay xóa
            for (PostMedia media : currentMedia) {
                if (keepMediaIds != null && keepMediaIds.contains(media.getPostMediaID())) {
                    mediaToKeep.add(media);
                } else {
                    mediaToDelete.add(media);
                }
            }

            // Xóa các media không còn cần thiết
            for (PostMedia media : mediaToDelete) {
                // Xóa file vật lý
                String filePath = getAbsolutePathFromRelativeUrl(media.getMediaURL());
                File fileToDelete = new File(filePath);

                if (postMediaRepository.existsById(media.getPostMediaID())) {
                    postMediaRepository.delete(media);
                } else {
                    System.out
                            .println("PostMedia với ID " + media.getPostMediaID()
                                    + " không tồn tại trong cơ sở dữ liệu.");
                }

                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }

            }

            // Thêm media mới nếu có
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                for (int i = 0; i < mediaFiles.size(); i++) {
                    MultipartFile file = mediaFiles.get(i);
                    // Kiểm tra file có rỗng không
                    if (file.isEmpty()) {
                        continue;
                    }

                    String mediaType = mediaTypes.get(i);

                    // Xử lý và lưu file giống như khi tạo post
                    String uploadPath;
                    if ("image".equals(mediaType)) {
                        uploadPath = IMAGE_UPLOAD_PATH;
                    } else if ("video".equals(mediaType)) {
                        uploadPath = VIDEO_UPLOAD_PATH;
                    } else {
                        continue;
                    }

                    // Tạo thư mục nếu chưa tồn tại
                    File directory = new File(uploadPath);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String newFilename = UUID.randomUUID().toString() + fileExtension;
                    String filePath = uploadPath + newFilename;

                    Path path = Paths.get(filePath);
                    Files.write(path, file.getBytes());

                    PostMedia postMedia = new PostMedia();
                    postMedia.setType(mediaType);
                    String relativeUrl = "/uploads/" +
                            (mediaType.equals("image") ? "postimage/" : "postvideo/") +
                            newFilename;
                    postMedia.setMediaURL(relativeUrl);
                    postMedia.setPost(post);

                    mediaToKeep.add(postMediaRepository.save(postMedia));
                }
            }

            // Cập nhật danh sách media cho post
            post.getMediaList().clear();
            post.getMediaList().addAll(mediaToKeep);
        }
        Post savedPost = postRepository.save(post);

        return PostDTO.fromEntity(savedPost);
    }

    public void deletePost(int postId) {
        // Tìm bài đăng theo ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài đăng với ID: " + postId));

        // Xóa các media liên quan đến bài đăng
        post.setHide(1);

        postRepository.save(post);
    }

    // để lấy đường dẫn tuyệt đối từ URL tương đối
    private String getAbsolutePathFromRelativeUrl(String relativeUrl) {
        // Loại bỏ phần "/uploads/" từ relativeUrl
        String relativePath = relativeUrl.replace("/uploads/", "");

        // Xác định đường dẫn gốc tùy thuộc vào loại media
        String basePath;
        if (relativePath.startsWith("postimage/")) {
            basePath = IMAGE_UPLOAD_PATH;
            relativePath = relativePath.replace("postimage/", "");
        } else if (relativePath.startsWith("postvideo/")) {
            basePath = VIDEO_UPLOAD_PATH;
            relativePath = relativePath.replace("postvideo/", "");
        } else {
            basePath = ""; // Mặc định
        }

        return basePath + relativePath;
    }
}
