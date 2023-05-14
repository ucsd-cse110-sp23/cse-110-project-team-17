package project.chat_gpt;

import java.io.IOException;

public class MockChatGPT implements IChatGPT {
  
    // Empty constructor
    public MockChatGPT() {}

    // Returns a mock answer to the given prompt (Appends an introduction to the input
    // and returns the resulting String)
    public String ask(String prompt) throws IOException, InterruptedException {
        return "Mock answer to the following prompt: " + prompt;
    }
}