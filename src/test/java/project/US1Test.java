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
import project.handler.*;

import java.io.*;

public class US1Test {

    // Wipe all account histories before
    @BeforeEach
    void wipeAccountsBefore() {
        String filename = "project/accounts.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File accountFile = new File(filename);
        accountFile.delete();
        DBCreate.wipeDB();
    }

    // Wipe all account histories after
    @AfterAll
    static void wipeAccountsAfter() {
        String filename = "project/accounts.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File accountFile = new File(filename);
        accountFile.delete();
        DBCreate.wipeDB();
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
        String questionString1 = "Question: What is project/dummy_audio/Question0?";
        String questionString2 = "Question: What is project/dummy_audio/Question1?";
        String answer_part = "Mock answer to the following prompt: ";

        // Create mock handlers and appframe
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");

        // Confirm that HistoryQuestion components correspond to 
        // expected question values
        HistoryListHandler historyList = testApp.getHistoryList();
        testApp.startRecording();
        testApp.stopRecording();
        testApp.startRecording();
        testApp.stopRecording();
        HistoryQuestionHandler question1 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        HistoryQuestionHandler question2 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(1);
        assertTrue(question1.getQuestion().
            equals(questionString1));
        assertTrue(question1.getAnswer().
            equals(answer_part + questionString1));
        assertTrue(question2.getQuestion().
            equals(questionString2));
        assertTrue(question2.getAnswer().
            equals(answer_part + questionString2));
        
        // Confirm that select button works as expected 
        // (i.e., chat window displays the correct question)
        testApp.selectQuestion(question1);
        assertTrue(question1.isSelected());

        // Close test frame
        testApp.closeApp();
    }

    // Test that default history list values are set to empty / 0 as appropriate
    @Test
    void testingHistoryListParameters() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");
        HistoryListHandler historyList = testApp.getHistoryList();
        assertTrue(historyList.getHistoryList().size() == 0);

        // Close test frame
        testApp.closeApp();
    }
}
