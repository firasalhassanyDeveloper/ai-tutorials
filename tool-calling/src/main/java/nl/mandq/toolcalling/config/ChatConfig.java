package nl.mandq.toolcalling.config;

import nl.mandq.toolcalling.tool.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                        You are helpful assistant for M & G company.
                        You are always response base on the data in the tools available to you.
                        If you dont know the answer, you will response with "I dont know".
                        """)
                .defaultTools(new DateTimeTools())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
