package project.chat_gpt;

import java.io.IOException;


public interface IChatGPT {
    // Method that returns answer to the input String prompt
    public String ask(String prompt) throws IOException, InterruptedException;
}