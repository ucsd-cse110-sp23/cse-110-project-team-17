package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class US6Test {
    @Test 
    void testAnswer() {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new ChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        ChatList chatList = testFrame.getChatList();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        ChatBox question1 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer1 = (ChatBox)
            chatList.getComponents()[1];
        assertTrue(question1.getLabel().equals("Question"));
        assertTrue(answer1.getLabel().equals("Answer"));
        assertTrue(question1.getDialogueText().
            equals("Who is Louis Braille?"));
        
        String answer_part = "Mock answer to the following prompt:\n";
        String question_part = "Who is Louis Braille?";
        assertTrue(answer1.getDialogueText().
            equals(answer_part + question_part));
    }
}
