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
import project.gui.*;

import java.io.*;

public class US2Test {

    // Delete history.csv file (fresh start)
    @BeforeEach
    void cleanHistory() {
        String filename = "project/history.txt";
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

    // Test if ChatGPT handler is made correctly
    @Test 
    void testChatGPT() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        testApp.createGUI();
        assertTrue(testApp.getChatGPT() instanceof IChatGPT);

        // Close test frame
        testApp.closeApp();
    }

    // Test that "Ask a Question" button exists
    @Test 
    void testNewQuestionButton() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        testApp.createGUI();
        AppGUI appGUI = testApp.getAppGUI();
        assertTrue(appGUI.getAskButton() instanceof JButton);

        // Close test frame
        testApp.closeApp();
    }

    // Test that "Ask a Question" button toggles visibility
    @Test 
    void testQuestionButtonToggle() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        testApp.createGUI();
        AppGUI appGUI = testApp.getAppGUI();
        JButton questionButton = appGUI.getAskButton();
        JButton stopButton = appGUI.getStopButton();
        assertTrue(questionButton.isVisible());
        assertFalse(stopButton.isVisible());
        appGUI.QuestionButtonHandler();
        assertFalse(questionButton.isVisible());
        assertTrue(stopButton.isVisible());

        // Close test frame
        testApp.closeApp();
    }

    // Test that "Ask a Question" and "Stop Recording" buttons toggle
    @Test
    void testStory() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        testApp.createGUI();
        AppGUI appGUI = testApp.getAppGUI();
        JButton questionButton = appGUI.getAskButton();
        JButton stopButton = appGUI.getStopButton();
        assertTrue(questionButton.isVisible());
        assertFalse(stopButton.isVisible());
        appGUI.QuestionButtonHandler();
        assertFalse(questionButton.isVisible());
        assertTrue(stopButton.isVisible());
        appGUI.StopButtonHandler();
        assertTrue(questionButton.isVisible());
        assertFalse(stopButton.isVisible());

        // Close test frame
        appGUI.closeFrame();
    }

    
}
