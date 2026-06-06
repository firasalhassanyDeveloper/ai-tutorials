package nl.mandq.ragstorage.controller;


import nl.mandq.ragstorage.service.RagChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final RagChatService ragChatService;

    public ChatController(RagChatService ragChatService) {

        this.ragChatService = ragChatService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        logger.info("incoming chat request {}", prompt);
        return ragChatService.ask(prompt);
    }
}