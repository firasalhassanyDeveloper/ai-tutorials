package nl.mandq.ragstorage.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RagChatService {
    private final ChatClient chatClient;
    public RagChatService( ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    public String ask(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}