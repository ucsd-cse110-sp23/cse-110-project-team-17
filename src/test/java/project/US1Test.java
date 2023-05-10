package project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JTextArea;

public class US1Test {
    @Test 
    void testHistoryDefault() {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new MockChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        List historyList = testFrame.getHistoryList();
        assertTrue(historyList.getComponents()[1] 
            instanceof JTextArea);
    }

    @Test
    void testHistoryStory() {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new MockChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        List historyList = testFrame.getHistoryList();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        HistoryQuestion question1 = 
            (HistoryQuestion) historyList.getComponents()[1];
        HistoryQuestion question2 = 
            (HistoryQuestion) historyList.getComponents()[2];
        assertTrue(question1.getQuestionText().
            equals("Who is Louis Braille?"));
        assertTrue(question2.getQuestionText().
            equals("What did Louis Braille do?"));
        testFrame.SelectButtonHandler(question1);
        ChatList chatList = testFrame.getChatList();
        ChatBox chatQuestion = 
            (ChatBox) chatList.getComponents()[0];
        ChatBox chatAnswer = 
            (ChatBox) chatList.getComponents()[1];
        assertEquals(chatQuestion.getLabel(), ("Question"));
        assertEquals(chatAnswer.getLabel(), ("Answer"));
        assertEquals(chatQuestion.getDialogueText(), 
            ("Who is Louis Braille?"));
        System.out.println(chatAnswer.getDialogueText());
        assertEquals(chatAnswer.getDialogueText(), 
            ("Mock answer to the following prompt:\n" + 
            "Who is Louis Braille?"));
    }
}
