package project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.swing.JTextArea;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

public class US5Test {
    @Test
    void testDeleteNothingSelected() throws IOException {
        HistoryList historyList = new HistoryList();
        assertTrue(1 == historyList.getComponentsNum());
        historyList.deleteSelected();
        assertTrue(1 == historyList.getComponentsNum());
    }

    @Test
    void testDeleteNothingSelectedMultiQuestions() throws IOException {
        HistoryList historyList = new HistoryList();
        HistoryQuestion Q1 = new HistoryQuestion();
        HistoryQuestion Q2 = new HistoryQuestion();
        HistoryQuestion Q3= new HistoryQuestion();
        historyList.add(Q3);
        historyList.add(Q2);
        historyList.add(Q1);
        assertTrue(4 == historyList.getComponentsNum());
        historyList.deleteSelected();
        assertTrue(4 == historyList.getComponentsNum());
    }

    @Test
    void testDeleteSelectedLastQuestion() throws IOException {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        ChatList chatList = testFrame.getChatList();
        HistoryList historyList = new HistoryList();

        HistoryQuestion Q1 = new HistoryQuestion();
        Q1.changeState();
        historyList.add(Q1);
        assertTrue(2==historyList.getComponentsNum());
        historyList.deleteSelected();
        assertTrue(1 == historyList.getComponentsNum());
    }

    @Test
    void testDeleteSelectedMultiQuestions() throws IOException {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        HistoryList historyList = new HistoryList();

        HistoryQuestion Q1 = new HistoryQuestion();
        Q1.changeState();
        Q1.insertQuestion("What is 2+2?");
        HistoryQuestion Q2 = new HistoryQuestion();
        Q2.insertQuestion("What is the date?");
        Q2.changeState();
        Q2.changeState();
        historyList.add(Q1);
        historyList.add(Q2);
        assertTrue(3==historyList.getComponentsNum());
        historyList.deleteSelected();
        assertTrue(2 == historyList.getComponentsNum());

        HistoryQuestion remainingQ = (HistoryQuestion) historyList.getComponent(1);
        assertTrue(remainingQ.getQuestionText() == "What is the date?");
    }
    
    @Test
    void test_deleteButtonGUI() throws IOException {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        IAudioHandler audioHandlerTest = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        ChatList chatList = testFrame.getChatList();
        HistoryList historyList = new HistoryList();
        
        assertTrue(deleteSelected.isVisible()); //test deleteSelected button is visible
    }
}
