package com.example.XDMHPL_Back_end.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.XDMHPL_Back_end.DTO.MessageMedia;
import com.example.XDMHPL_Back_end.Repositories.MessageMediaRepository;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private MessageMediaRepository messageMediaRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Lưu file lên thư mục uploads
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Lưu thông tin vào cơ sở dữ liệu
            MessageMedia media = new MessageMedia();
            media.setMediaURL("uploads/" + fileName);
            media.setMediaType(file.getContentType());
            messageMediaRepository.save(media);

            return ResponseEntity.ok("uploads/" + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi upload file");
        }
    }
}

