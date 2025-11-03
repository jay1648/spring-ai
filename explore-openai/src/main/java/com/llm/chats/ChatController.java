package com.llm.chats;

import com.llm.dto.UserInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

  private static final Logger log = LoggerFactory.getLogger(ChatController.class);

  private final ChatClient chatClient;

  public ChatController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @PostMapping("/v1/chats")
  public Object chat(@RequestBody UserInput userInput) {
    log.info("userInput message : {} ", userInput);
    var requestSpec = chatClient.prompt().user(userInput.prompt());
    log.info("requestSpec : {} ", requestSpec);
    var responseSpec = requestSpec.call();
    log.info("responseSpec : {} ", responseSpec);
    var resp = responseSpec.content();
    log.info("responseSpec content : {} ", resp);
    return resp;
  }

  @PostMapping("/v2/chats")
  public Object chatV2(@RequestBody UserInput userInput) {
    log.info("userInput message : {} ", userInput);
    var systemMessage =
        """
        You are a helpful assistant, who can answer java based questions.
        For any other questions, please respond with I don't know in a funny way!
        """;
    var requestSpec =
        chatClient
            .prompt()
            .user(userInput.prompt())
            .system(systemMessage)
            .options(ChatOptions.builder().temperature(0.0).build());
    log.info("requestSpec : {} ", requestSpec);
    var responseSpec = requestSpec.call();
    log.info("responseSpec : {} ", responseSpec);
    var resp = responseSpec.content();
    log.info("responseSpec content : {} ", resp);
    return resp;
  }
}
