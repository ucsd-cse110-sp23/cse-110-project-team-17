package project.question_handler;

import java.io.IOException;

public class MockQuestionHandler implements IQuestionHandler {

    public MockQuestionHandler() {}

    public String getQuestion(String filename) throws IOException {
        String question = filename.split("[.]")[0];
        String prompt = "What is " + question + "?";
        return prompt;
    }
}