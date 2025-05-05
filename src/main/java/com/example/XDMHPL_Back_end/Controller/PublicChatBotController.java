// filepath: src/main/java/com/yourpackage/controller/PublicChatController.java
package com.example.XDMHPL_Back_end.Controller;
import com.example.XDMHPL_Back_end.DTO.ChatBotDTO.*;
import com.example.XDMHPL_Back_end.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin/send-chat") // Đường dẫn công khai
@CrossOrigin(origins = "http://localhost:5173") // Cho phép frontend gọi
public class PublicChatBotController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping
    public Mono<ResponseEntity<ChatBotResponse>> handlePublicChatMessage(@RequestBody ChatBotRequest chatRequest) {
        if (chatRequest == null || chatRequest.getMessage() == null || chatRequest.getMessage().trim().isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body(new ChatBotResponse("Message is required.")));
        }

        return geminiService.generateContent(chatRequest.getMessage())
                .map(reply -> ResponseEntity.ok(new ChatBotResponse(reply)))
                .defaultIfEmpty(ResponseEntity.status(500).body(new ChatBotResponse("Failed to get response from AI.")));
    }
}