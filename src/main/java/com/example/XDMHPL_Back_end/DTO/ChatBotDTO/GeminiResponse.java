package com.example.XDMHPL_Back_end.DTO.ChatBotDTO;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Bỏ qua các trường không cần thiết
public class GeminiResponse {
    private List<Candidate> candidates;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        private Content content;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private List<Part> parts;
        private String role; // Thường là "model"
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        private String text;
    }

    // Helper method để lấy text trả về đầu tiên
    public String getFirstCandidateText() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate firstCandidate = candidates.get(0);
            if (firstCandidate != null && firstCandidate.getContent() != null &&
                firstCandidate.getContent().getParts() != null && !firstCandidate.getContent().getParts().isEmpty()) {
                Part firstPart = firstCandidate.getContent().getParts().get(0);
                if (firstPart != null) {
                    return firstPart.getText();
                }
            }
        }
        return null; // Hoặc trả về chuỗi rỗng/thông báo lỗi
    }
}