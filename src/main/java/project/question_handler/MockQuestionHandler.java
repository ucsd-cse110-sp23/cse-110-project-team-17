package project.question_handler;

import java.io.IOException;

public class MockQuestionHandler implements IQuestionHandler {

    // Empty Constructor
    public MockQuestionHandler() {}

    // Returns a mock "question" String, obtained by extracting the 
    // filename without the extension
    public String getQuestion(String filename) throws IOException {
        String question = filename.split("[.]")[0];
        String prompt = "What is " + question + "?";
        return prompt;
    }

    public String[] getCommand(String prompt) {
        String[] command = {"test"};
        return command;
    }
}