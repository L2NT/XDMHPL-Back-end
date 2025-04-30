package com.example.XDMHPL_Back_end.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final String UPLOAD_DIR = "uploads/";

    static {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            System.err.println("❌ Lỗi khi tạo thư mục upload: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Kiểm tra file rỗng
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Không có tệp nào được tải lên."));
            }

            // 2. Kiểm tra loại file (ở đây cho phép hình ảnh, có thể mở rộng)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Chỉ cho phép tải lên tệp hình ảnh."));
            }

            // 3. Kiểm tra kích thước file (tối đa 5MB)
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Tệp quá lớn. Giới hạn là 5MB."));
            }

            // 4. Tạo tên file duy nhất và lưu
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
            Files.write(filePath, file.getBytes());

            // 5. Tạo URL phản hồi
            String fileUrl = "http://localhost:8080/uploads/" + fileName;

            return ResponseEntity.ok(Map.of(
               
                "fileName", fileName
               
            ));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Lỗi xảy ra khi xử lý tệp tải lên."));
        }
    }
}
