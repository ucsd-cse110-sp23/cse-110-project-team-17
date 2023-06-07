package project;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.gui.*;
import project.handler.*;
import project.prompt_handler.*;

import java.io.*;

public class DS1_1Test {
    
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

    @Test
    public void iterationTest() {
        // Create mock handlers and appframe
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");

        // Set up expected values
        String questionString1 = "Question: What is project/dummy_audio/Question0?";
        String questionString2 = "Question: What is project/dummy_audio/Question1?";
        String answer_part = "Mock answer to the following prompt: ";
        
        // Test the "Ask a Question" and "Stop Recording" buttons
        // Verify that the question and answer show up as expected in chat window
        HistoryListHandler historyList = testApp.getHistoryList();
        testApp.startRecording();
        testApp.stopRecording();
        HistoryPromptHandler question1 = historyList.getHistoryList().get(0);
        assertTrue(question1.getPrompt().
            equals(questionString1));
        assertTrue(question1.getAnswer().
            equals(answer_part + questionString1));
        
        // Test the "Select" button
        // Verify that the selected question and answer show up as expected in chat window
        testApp.startRecording();
        testApp.stopRecording();
        HistoryPromptHandler question2 = 
            (HistoryPromptHandler) historyList.getHistoryList().get(1);
        assertTrue(question1.getPrompt().
            equals(questionString1));
        assertTrue(question2.getPrompt().
            equals(questionString2));
        testApp.selectPrompt(question1);
        assertTrue(question1.isSelected());
        
        // Close test frame
        testApp.closeApp();
    }
}