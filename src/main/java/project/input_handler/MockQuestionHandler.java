package project.input_handler;

import java.io.IOException;

public class MockQuestionHandler implements IInputHandler {

    // Empty Constructor
    public MockQuestionHandler() {}

    // Returns a mock "question" String, obtained by extracting the 
    // filename without the extension
    public String getInput(String filename) throws IOException {
        String question = filename.split("[.]")[0];
        String prompt = "What is " + question + "?";
        return prompt;
    }
}