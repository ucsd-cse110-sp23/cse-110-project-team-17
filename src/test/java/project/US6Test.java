package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.gui.*;

import project.handler.*;
import project.prompt_handler.*;

import java.io.*;

public class US6Test {

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

    // Test that "Ask a Question" button works as expected
    @Test
    void testGetQuestion() throws IOException, InterruptedException {
        IPromptHandler qHandler = new MockPromptHandler();
        IAudioHandler audioHandlerTest = new MockAudioHandler();

        audioHandlerTest.startRecording();
        String filename = audioHandlerTest.stopRecording();
        String question = qHandler.getPrompt(filename);

        // Verify that expected question is equal to actual question
        String filename_first = filename.split("[.]")[0];
        String expected_question = "Question: What is " + filename_first + "?";
        assertEquals(question, expected_question);
    }

    // Test that using ChatGPT displays the expected answer
    @Test 
    void testGPTAskMethod() throws IOException, InterruptedException {

        // Create mock handlers and appframe
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        IAudioHandler audioHandlerTest = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");
        
        // Use second mock audio handler to obtain expected question
        audioHandlerTest.startRecording();
        String filename = audioHandlerTest.stopRecording();
        String question = qHandler.getPrompt(filename);
        String filename_first = filename.split("[.]")[0];
        String expected_question = "Question: What is " + filename_first + "?";
        assertEquals(question, expected_question);

        // Use chatGPT handler to obtain expected answer
        String answer_part = chatGPT.ask(expected_question);
        
        // Verify that expected question and answer are actually added
        // to prompt history
        HistoryListHandler historyList = testApp.getHistoryList();
        testApp.startRecording();
        testApp.stopRecording();
        HistoryPromptHandler question1 = historyList.getHistoryList().get(0);
        assertTrue(question1.getPrompt().
            equals(expected_question));
        assertTrue(question1.getAnswer().
            equals(answer_part));

        // Close test frame
        testApp.closeApp();
    }
}
