package com.example.XDMHPL_Back_end.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.XDMHPL_Back_end.DTO.ChatBotDTO.GeminiRequest;
import com.example.XDMHPL_Back_end.DTO.ChatBotDTO.GeminiResponse;

import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final String apiKey;
    private final String apiUrl;

    // Inject WebClient.Builder và các giá trị từ application.properties
    public GeminiService(WebClient.Builder webClientBuilder,
                         @Value("${gemini.api.key}") String apiKey,
                         @Value("${gemini.api.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.apiKey = apiKey;
        this.apiUrl = apiUrl; 
    }

    public Mono<String> generateContent(String userMessage) {
        String promptInVietnamese = "Hãy trả lời câu hỏi sau bằng tiếng Việt: " + userMessage;

        GeminiRequest.Part part = new GeminiRequest.Part(promptInVietnamese);

        GeminiRequest.Content content = new GeminiRequest.Content(List.of(part));
        GeminiRequest requestBody = new GeminiRequest(List.of(content));

        return this.webClient.post()
                .uri(apiUrl + "?key=" + apiKey) // Gửi request đến URL đầy đủ kèm key
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .map(response -> {
                    String text = response.getFirstCandidateText();
                    // Có thể thêm xử lý nếu text là null hoặc rỗng ở đây
                    return text != null ? text : "Không nhận được phản hồi hợp lệ từ AI.";
                })
                .doOnError(error -> System.err.println("Error calling Gemini API: " + error.getMessage()))
                .onErrorReturn("Xin lỗi, tôi không thể xử lý yêu cầu này ngay bây giờ.");
    }
}