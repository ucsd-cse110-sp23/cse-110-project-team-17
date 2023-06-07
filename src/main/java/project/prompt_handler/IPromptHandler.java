package project.prompt_handler;

import java.io.IOException;

public interface IPromptHandler {
    // Method to obtain question from some file
    public String getPrompt(String filename) throws IOException;

    //Method to discrimiate voice command
    public String getCommand(String prompt) throws IOException;
}