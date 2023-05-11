package project.question_handler;

public class MockQuestion implements IQuestionHandler {
    int index;
    String[] options = new String[5];

    public MockQuestion() {
        index = 0;
        options[0] = "Who is Louis Braille?";
        options[1] = "What did Louis Braille do?";
        options[2] = "How did Louis Braille invent Braille?";
        options[3] = "When was Louis Braille born?";
        options[4] = "Where did Louis Braille live?";
    }

    public void startQuestion() {}

    public String peekQuestion() {
        return options[index];
    }

    public String getQuestion() {
        String toReturn = options[index];
        index = (index + 1) % 5;
        return toReturn;
    }
}