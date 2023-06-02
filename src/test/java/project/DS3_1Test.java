package project;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;
import project.handler.*;

import java.io.*;

public class DS3_1Test {
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

    // Test History Methods Across Two Accounts
    // and test that history is connected to users
    @Test
    void iterationTest() {
        // Set expected values
        String questionString1 = "Question: What is project/dummy_audio/Question0?";
        String questionString2 = "Question: What is project/dummy_audio/Question1?";
        String questionString3 = "Question: What is project/dummy_audio/Question2?";
        String questionString4 = "Question: What is project/dummy_audio/Question3?";
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
        testApp.startRecording();
        testApp.stopRecording();
        testApp.startRecording();
        testApp.stopRecording();
        HistoryQuestionHandler question1 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        HistoryQuestionHandler question2 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(1);
        HistoryQuestionHandler question3 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(2);
        HistoryQuestionHandler question4 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(3);
        assertTrue(question1.getQuestion().
            equals(questionString1));
        assertTrue(question1.getAnswer().
            equals(answer_part + questionString1));
        assertTrue(question2.getQuestion().
            equals(questionString2));
        assertTrue(question2.getAnswer().
            equals(answer_part + questionString2));
        assertTrue(question3.getQuestion().
            equals(questionString3));
        assertTrue(question3.getAnswer().
            equals(answer_part + questionString3));
        assertTrue(question4.getQuestion().
            equals(questionString4));
        assertTrue(question4.getAnswer().
            equals(answer_part + questionString4));
        
        // Confirm that select button works as expected 
        // (i.e., chat window displays the correct question)
        testApp.selectQuestion(question1);
        assertTrue(question1.isSelected());

        // Confirm that deleting question properly deletes question
        testApp.deleteSelected();
        assertTrue(historyList.getHistoryList().size() == 3);
        for (HistoryQuestionHandler hqh : historyList.getHistoryList()) {
            assertFalse(hqh.getQuestion().
                equals(questionString1));
        }
        

        // Close test frame
        testApp.closeApp();

        // Reopen test frame
        AppHandler testApp2 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI2 = new AppGUI(testApp2);
        testApp2.createGUI(appGUI2);
        testApp2.LogIn("username", "password");

        // Test persistence of remaining question
        HistoryListHandler historyList2 = testApp2.getHistoryList();
        assertTrue(historyList2.getHistoryList().size() == 3);
        for (HistoryQuestionHandler hqh : historyList.getHistoryList()) {
            assertFalse(hqh.getQuestion().
                equals(questionString1));
        }
        
        // Test clearing of history
        testApp2.clearAll();
        assertTrue(historyList2.getHistoryList().size() == 0);

        // Close test frame
        testApp2.closeApp();
    }
}