package project;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import javax.swing.JTextArea;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

import java.io.*;

public class US1Test {

    // Delete history.csv file (fresh start)
    @BeforeEach
    void cleanHistory() {
        String filename = "project/history.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File historyFile = new File(filename);
        historyFile.delete();
    }

    // Delete history.csv file (once at end of all tests)
    @AfterAll
    static void cleanUp() {
        String filename = "project/history.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File historyFile = new File(filename);
        historyFile.delete();
    }

    // Test that text area is displayed as default when there are no questions
    @Test 
    void testHistoryDefault() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();

        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        HistoryListGUI historyListGUI = appGUI.getHistoryList();
        assertTrue(historyListGUI.getComponents()[1] 
            instanceof JTextArea);
        
        // Close test frame
        testApp.closeApp();
    }

    // Test History Methods
    @Test
    void testHistoryStory() {
        // Set expected values
        String questionString1 = "What is project/dummy_audio/TestRecording0?";
        String questionString2 = "What is project/dummy_audio/TestRecording1?";
        String answer_part = "Mock answer to the following prompt: ";

        // Create mock handlers and appframe
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);

        // Confirm that HistoryQuestion components correspond to 
        // expected question values
        HistoryListHandler historyList = testApp.getHistoryList();
        appGUI.QuestionButtonHandler();
        appGUI.StopButtonHandler();
        appGUI.QuestionButtonHandler();
        appGUI.StopButtonHandler();
        HistoryQuestionHandler question1 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        HistoryQuestionHandler question2 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(1);
        assertTrue(question1.getQuestion().
            equals(questionString1));
        assertTrue(question2.getQuestion().
            equals(questionString2));
        
        // Confirm that select button works as expected 
        // (i.e., chat window displays the correct question)
        appGUI.SelectButtonHandler(question1.getHistoryQuestionGUI());
        ChatWindowGUI chatWindow = appGUI.getChatWindow();
        ChatBoxGUI chatQuestion = 
            (ChatBoxGUI) chatWindow.getComponents()[0];
        ChatBoxGUI chatAnswer = 
            (ChatBoxGUI) chatWindow.getComponents()[1];
        assertEquals(chatQuestion.getLabel(), ("Question"));
        assertEquals(chatAnswer.getLabel(), ("Answer"));
        assertEquals(chatQuestion.getDialogueText(), 
            (questionString1));
        assertEquals(chatAnswer.getDialogueText(), 
            (answer_part + questionString1));

        // Close test frame
        testApp.closeApp();
    }

    @Test
    void testingHistoryListParameters() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        HistoryListHandler historyList = testApp.getHistoryList();
        assertTrue(historyList.getHistoryList().size() == 0);

        // Close test frame
        testApp.closeApp();
    }
}
