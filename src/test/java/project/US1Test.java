package project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JTextArea;

public class US1Test {
    @Test 
    void testHistoryDefault() {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new ChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        List historyList = testFrame.getHistoryList();
        assertTrue(historyList.getComponents()[0] 
            instanceof JTextArea);
    }

    void testHistoryStory() {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new ChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        List historyList = testFrame.getHistoryList();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        HistoryQuestion question1 = 
            (HistoryQuestion) historyList.getComponents()[0];
        HistoryQuestion question2 = 
            (HistoryQuestion) historyList.getComponents()[1];
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
        assertTrue(chatQuestion.getLabel().equals("Question"));
        assertTrue(chatAnswer.getLabel().equals("Answer"));
        assertTrue(chatQuestion.getDialogueText().
            equals("Who is Louis Braille?"));
        assertTrue(chatAnswer.getDialogueText().
            equals("Mock answer to the follwing prompt:\n" + 
            "Who is Louis Braille?"));
    }
}
