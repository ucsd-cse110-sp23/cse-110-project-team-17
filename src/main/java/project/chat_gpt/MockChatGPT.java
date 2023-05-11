package project.chat_gpt;

import java.io.IOException;

public class MockChatGPT implements IChatGPT {
  
    public MockChatGPT() {}

    public String ask(String prompt) throws IOException, InterruptedException {
        return "Mock answer to the following prompt:\n" + prompt;
    }
}