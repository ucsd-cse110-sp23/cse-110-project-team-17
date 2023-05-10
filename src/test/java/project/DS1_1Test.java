package project;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DS1_1Test {
    @Test
    public void iterationTest() {
        
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new ChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        
        ChatList chatList = testFrame.getChatList();
        List historyList = testFrame.getHistoryList();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        ChatBox chatquestion1 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer1 = (ChatBox)
            chatList.getComponents()[1];
        assertTrue(chatquestion1.getLabel().equals("Question"));
        assertTrue(answer1.getLabel().equals("Answer"));
        assertTrue(chatquestion1.getDialogueText().
            equals("Who is Louis Braille?"));
        
        String answer_part = "Mock answer to the following prompt:\n";
        String question_part = "Who is Louis Braille?";
        assertTrue(answer1.getDialogueText().
            equals(answer_part + question_part));
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
        ChatBox chatQuestion = 
            (ChatBox) chatList.getComponents()[0];
        ChatBox chatAnswer = 
            (ChatBox) chatList.getComponents()[1];
        assertTrue(chatQuestion.getLabel().equals("Question"));
        assertTrue(chatAnswer.getLabel().equals("Answer"));
        assertTrue(chatQuestion.getDialogueText().
            equals("Who is Louis Braille?"));
        assertTrue(answer1.getDialogueText().
            equals(answer_part + question_part));
    }
}
