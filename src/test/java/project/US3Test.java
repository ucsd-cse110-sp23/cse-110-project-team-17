package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;
import project.handler.*;

import java.io.*;

public class US3Test {

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

    // Test that recording audio creates two separate files
    @Test
    void testStartAndStopRecording() {
        IAudioHandler audioHandler = new MockAudioHandler();
        audioHandler.startRecording();
        String file1 = audioHandler.stopRecording();
        audioHandler.startRecording();
        String file2 = audioHandler.stopRecording();
        assertTrue(!file1.equals(file2));
    }

    // Test that recording methods create the expected questions in 
    // the app
    @Test
    void testStartAndStopRecordingGUI() throws IOException, InterruptedException {
        // Create mock handlers
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        IAudioHandler audioHandler2 = new MockAudioHandler();

        // Obtain expected strings using second mock audio handler
        audioHandler2.startRecording();
        String filename = audioHandler2.stopRecording();
        String filename_first = filename.split("[.]")[0];
        String expected_question = "Question: What is " + filename_first + "?";

        // Create appframe
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");

        // Verify that on stopping and starting recording, the actual 
        // chat window displays the expected dialogue
        testApp.startRecording();
        testApp.stopRecording();
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryQuestionHandler question = historyList.getHistoryList().get(0);
        assertTrue(question.getQuestion().
            equals(expected_question));

        // Close test frame
        testApp.closeApp();
    }
}
