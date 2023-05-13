package project;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

public class DS1_1Test {
    @Test
    public void iterationTest() {
        
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();

        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);

        String questionString1 = "What is project/dummy_audio/TestRecording0?";
        String questionString2 = "What is project/dummy_audio/TestRecording1?";
        String answer_part = "Mock answer to the following prompt: ";
        
        ChatList chatList = testFrame.getChatList();
        HistoryList historyList = testFrame.getHistoryList();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        ChatBox chatquestion1 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer1 = (ChatBox)
            chatList.getComponents()[1];
        assertTrue(chatquestion1.getLabel().equals("Question"));
        assertTrue(answer1.getLabel().equals("Answer"));
        assertTrue(chatquestion1.getDialogueText().
            equals(questionString1));
        
        assertTrue(answer1.getDialogueText().
            equals(answer_part + questionString1));
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        HistoryQuestion question1 = 
            (HistoryQuestion) historyList.getComponents()[1];
        HistoryQuestion question2 = 
            (HistoryQuestion) historyList.getComponents()[2];
        assertTrue(question1.getQuestionText().
            equals(questionString1));
        assertTrue(question2.getQuestionText().
            equals(questionString2));
        testFrame.SelectButtonHandler(question1);
        ChatBox chatQuestion = 
            (ChatBox) chatList.getComponents()[0];
        ChatBox chatAnswer = 
            (ChatBox) chatList.getComponents()[1];
        assertTrue(chatQuestion.getLabel().equals("Question"));
        assertTrue(chatAnswer.getLabel().equals("Answer"));
        assertTrue(chatQuestion.getDialogueText().
            equals(questionString1));
        assertTrue(answer1.getDialogueText().
            equals(answer_part + questionString1));
    }
}