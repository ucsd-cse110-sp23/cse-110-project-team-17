package project.question_handler;

import java.io.IOException;

public interface IQuestionHandler {
    public String getQuestion(String filename) throws IOException;
}