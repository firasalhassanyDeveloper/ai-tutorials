package nl.mandq.springaimodelrunner;

import nl.mandq.springaimodelrunner.controller.ChatController;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ChatController.class,
        excludeAutoConfiguration = {
                ChatClientAutoConfiguration.class
        }
)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatClient chatClient;

    @Test
    void chat_returnsResponse() throws Exception {

        var requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        var responseSpec = mock(ChatClient.CallResponseSpec.class);

        when(chatClient.prompt("Hello AI")).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn("Hi human");

        mockMvc.perform(post("/chat")
                        .content("Hello AI")
                        .contentType("text/plain"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hi human"));
    }
}