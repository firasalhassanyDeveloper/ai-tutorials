package nl.mandq.toolcalling.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    record Input(@NotNull String text) {
    }

    @PostMapping("/chat")
    public String chat(@RequestBody Input input) {
        if (input == null || input.text() == null || input.text().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Prompt cannot be empty");
        }

        logger.info("Incoming chat request {}, Length={}", input.text(),
                input.text().length());

        try {
            return chatClient.prompt()
                    .user(input.text)
                    .call()
                    .content();
        } catch (Exception e) {
            logger.error("Chat request failed", e);
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "AI service unavailable");
        }
    }
}

