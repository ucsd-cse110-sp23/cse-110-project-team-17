package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.swing.JButton;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.handler.*;
import project.gui.*;

import java.io.*;

public class US2Test {

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

    // Test if ChatGPT handler is made correctly
    @Test 
    void testChatGPT() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");
        assertTrue(testApp.getChatGPT() instanceof IChatGPT);

        // Close test frame
        testApp.closeApp();
    }

    // Test that "Start" button exists
    @Test 
    void testNewQuestionButton() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");
        assertTrue(appGUI.getStartButton() instanceof JButton);

        // Close test frame
        testApp.closeApp();
    }

    // Test that "Start" button toggles visibility
    @Test 
    void testQuestionButtonToggle() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");
        JButton startButton = appGUI.getStartButton();
        JButton stopButton = appGUI.getStopButton();
        assertTrue(startButton.isVisible());
        assertFalse(stopButton.isVisible());
        appGUI.QuestionButtonHandler();
        assertFalse(startButton.isVisible());
        assertTrue(stopButton.isVisible());

        // Close test frame
        testApp.closeApp();
    }

    // Test that "Start" and "Stop Recording" buttons toggle
    @Test
    void testStory() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");
        JButton startButton = appGUI.getStartButton();
        JButton stopButton = appGUI.getStopButton();
        assertTrue(startButton.isVisible());
        assertFalse(stopButton.isVisible());
        appGUI.QuestionButtonHandler();
        assertFalse(startButton.isVisible());
        assertTrue(stopButton.isVisible());
        appGUI.StopButtonHandler();
        assertTrue(startButton.isVisible());
        assertFalse(stopButton.isVisible());

        // Close test frame
        appGUI.closeFrame();
    }

    
}
