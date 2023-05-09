package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class US6Test {
    @Test 
    void testAnswer() {
        AppFrame testFrame = new AppFrame();
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
