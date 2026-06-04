package nl.mandq.prompttemplates.controller;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatClient chatClient;
    private final Resource scrumMasterResources;

    public ChatController(ChatClient chatClient,@Value("classpath:/prompt/scrum-master-system.st") Resource scrumMasterResources) {
        this.chatClient = chatClient;
        this.scrumMasterResources = scrumMasterResources;
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

        SystemMessage systemMessage = new SystemMessage(
                "You are a friendly and helpful assistant, always responding with sarcasm.");

        UserMessage userMessage = new UserMessage(input.text());

        Prompt promptRequest = new Prompt(
                List.of(systemMessage, userMessage));

        try {
            return chatClient.prompt(promptRequest)
                    .call()
                    .content();
        } catch (Exception e) {
            logger.error("Chat request failed", e);
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "AI service unavailable");
        }
    }

    record SuggestedPlan(@NotNull String topic, @NotNull int numberOfPoints) {
    }

    @PostMapping("/suggest-plan")
    public String suggestPlan(@RequestBody SuggestedPlan plan) throws IOException {
        if (plan == null || plan.topic() == null || plan.topic().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "plan cannot be empty");
        }

        String scrumSystem = scrumMasterResources.getContentAsString(StandardCharsets.UTF_8);
        Message message = getMessage(plan);
        Prompt prompt = new Prompt(List.of(message, new SystemMessage(scrumSystem)));
        try {
            return chatClient.prompt(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            logger.error("Chat request failed", e);
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "AI service unavailable");
        }
    }

    private static Message getMessage(SuggestedPlan plan){
        PromptTemplate promptTemplate = new PromptTemplate("""
                I would like to have a meeting about the following topic:
                
                {topic}
                
                Generate {count} discussion points for this meeting.
                
                Requirements:
                - Each point must be relevant to the meeting topic.
                - Each point should be a short, concise sentence.
                - Avoid duplicates.
                - Return only the list of discussion points.
                """);
        Map<String, Object> vars = Map.of("topic", plan.topic, "count", plan.numberOfPoints);
        return promptTemplate.createMessage(vars);
    }
}

