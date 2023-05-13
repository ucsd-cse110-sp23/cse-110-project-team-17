package project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JTextArea;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

public class US1Test {
    @Test 
    void testHistoryDefault() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        HistoryList historyList = testFrame.getHistoryList();
        assertTrue(historyList.getComponents()[1] 
            instanceof JTextArea);
    }

    @Test
    void testHistoryStory() {

        String questionString1 = "What is project/dummy_audio/TestRecording0?";
        String questionString2 = "What is project/dummy_audio/TestRecording1?";
        String answer_part = "Mock answer to the following prompt:\n";

        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        HistoryList historyList = testFrame.getHistoryList();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
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
        ChatList chatList = testFrame.getChatList();
        ChatBox chatQuestion = 
            (ChatBox) chatList.getComponents()[0];
        ChatBox chatAnswer = 
            (ChatBox) chatList.getComponents()[1];
        assertEquals(chatQuestion.getLabel(), ("Question"));
        assertEquals(chatAnswer.getLabel(), ("Answer"));
        assertEquals(chatQuestion.getDialogueText(), 
            (questionString1));
        assertEquals(chatAnswer.getDialogueText(), 
            (answer_part + questionString1));
    }
}
