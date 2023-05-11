package project.chat_gpt;

import java.io.IOException;


public interface IChatGPT {
    public String ask(String prompt) throws IOException, InterruptedException;
}