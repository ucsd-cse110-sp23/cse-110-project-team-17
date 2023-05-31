package project.question_handler;

import java.io.IOException;

public interface IQuestionHandler {
    // Method to obtain question from some file
    public String getQuestion(String filename) throws IOException;

    //Method to discrimiate voice command
    public String[] getCommand(String prompt) throws IOException;
}