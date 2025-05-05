package com.example.XDMHPL_Back_end.DTO.ChatBotDTO;

import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class GeminiRequest {
    private List<Content> contents;
    // Có thể thêm generationConfig nếu cần tùy chỉnh thêm
    // private GenerationConfig generationConfig;

    @Data
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @AllArgsConstructor
    public static class Part {
        private String text;
    }
}
