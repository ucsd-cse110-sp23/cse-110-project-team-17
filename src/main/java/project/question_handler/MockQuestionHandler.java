package project.question_handler;

import java.io.IOException;

public class MockQuestionHandler implements IQuestionHandler {

    // Empty Constructor
    public MockQuestionHandler() {}

    // Returns a mock "question" String, obtained by extracting the 
    // filename without the extension
    public String getQuestion(String filename) throws IOException {
        String question = filename.split("[.]")[0];
        String prompt = "Question: What is " + question + "?";
        return prompt;
    }

    public String getCommand(String prompt) {

        String cmd = "";

        if (prompt.length() >= 8) {
            cmd = prompt.substring(0, 8);
            if (cmd.toUpperCase().equals("QUESTION")) {
                return cmd;
            }
        }
        if (prompt.length() >= 6) {
            cmd = prompt.substring(0, 6);
            if (cmd.toUpperCase().equals("DELETE")) {
                return cmd;
            }
        }
        if (prompt.length() >= 5) {
            cmd = prompt.substring(0, 5);
            if (cmd.toUpperCase().equals("CLEAR")) {
                return cmd;
            }
        }
        return "invalid";
    }
}