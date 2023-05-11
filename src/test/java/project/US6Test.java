package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

public class US6Test {
    @Test 
    void testAnswer() throws IOException, InterruptedException {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new MockChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        ChatList chatList = testFrame.getChatList();

        String question = qHandler.peekQuestion();

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
        
        String answer_part = chatGPT.ask(question);
        assertTrue(answer1.getDialogueText().
            equals(answer_part));
    }
}
