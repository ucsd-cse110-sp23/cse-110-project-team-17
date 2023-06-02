package project;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;
import project.handler.*;

import java.io.*;

public class DS2_1Test {
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
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp0 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI0 = new AppGUI(testApp0);
        testApp0.createGUI(appGUI0);
        LogInWindowHandler logInHandler = testApp0.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp0.LogIn("username", "password");

        // Set up expected values
        String questionString1 = "Question: What is project/dummy_audio/Question0?";
        String questionString2 = "Question: What is project/dummy_audio/Question1?";
        String questionString3 = "Question: What is project/dummy_audio/Question2?";
        String questionString4 = "Question: What is project/dummy_audio/Question3?";
        String answer_part = "Mock answer to the following prompt: ";
        
        // Test the "Ask a Question" and "Stop Recording" buttons
        // Verify that the question and answer show up as expected in chat window
        HistoryListHandler historyList0 = testApp0.getHistoryList();
        testApp0.startRecording();
        testApp0.stopRecording();
        HistoryQuestionHandler question0 = historyList0.getHistoryList().get(0);
        assertTrue(question0.getQuestion().
            equals(questionString1));
        assertTrue(question0.getAnswer().
            equals(answer_part + questionString1));
        testApp0.closeApp();

        // Test the permanence of history
        // Verify that the question and answer from old session still show up
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        testApp.LogIn("username", "password");
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryQuestionHandler question1 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        assertTrue(question1.getQuestion().
            equals(questionString1));

        // Test the "Select" button
        testApp.selectQuestion(question1);
        assertTrue(question1.isSelected());
        
        // Add a second question
        // Test that the question and answer show up 
        // in history list as expected
        testApp.startRecording();
        testApp.stopRecording();
        HistoryQuestionHandler question2 = historyList.getHistoryList().get(1);
        assertTrue(question2.getQuestion().
            equals(questionString2));
        assertTrue(question2.getAnswer().
            equals(answer_part + questionString2));

        // Add a third question
        // Test that the question and answer show up 
        // in history list as expected
        testApp.startRecording();
        testApp.stopRecording();
        HistoryQuestionHandler question3 = historyList.getHistoryList().get(2);
        assertTrue(question3.getQuestion().
            equals(questionString3));
        assertTrue(question3.getAnswer().
            equals(answer_part + questionString3));


        // Add a fourth question
        // Test that the question and answer show up 
        // in history list as expected
        testApp.startRecording();
        testApp.stopRecording();
        HistoryQuestionHandler question4 = historyList.getHistoryList().get(3);
        assertTrue(question4.getQuestion().
            equals(questionString4));
        assertTrue(question4.getAnswer().
            equals(answer_part + questionString4));

        // Select the first question, then select the fourth question
        // Test deletion of fourth question - chat window should be empty
        // and history list should no longer have that question
        testApp.selectQuestion(question1);
        assertTrue(question1.isSelected());
        testApp.selectQuestion(question4);
        assertTrue(!question1.isSelected());
        assertTrue(question4.isSelected());
        testApp.deleteSelected();
        assertTrue(historyList.getHistoryList().size() == 3);
        for (HistoryQuestionHandler hqh : historyList.getHistoryList()) {
            assertFalse(hqh.getQuestion().
                equals(questionString4));
        }

        // Test clearing all questions
        // HistoryList should be reset to default
        testApp.clearAll();
        assertTrue(historyList.getHistoryList().size() == 0);

        // Close the test frame
        testApp.closeApp();
    }
}
