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

public class US8Test {
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

    // Test that history list defaults to empty when there are no questions
    @Test 
    void testHistoryDefault() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();

        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");
        HistoryListHandler historyListHandler = testApp.getHistoryList();

        assertTrue(historyListHandler.getHistoryList().size() == 0);
        
        // Close test frame
        testApp.closeApp();
    }

    // Test History Methods Across One Account
    // and test that history persists across sessions
    @Test
    void testHistoryOneAccount() {
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

        // Confirm that deleting question properly deletes question
        testApp.deleteSelected();
        assertTrue(historyList.getHistoryList().size() == 1);
        HistoryQuestionHandler remainingQuestion = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        assertTrue(remainingQuestion.getQuestion().
            equals(questionString2));
        assertTrue(remainingQuestion.getAnswer().
            equals(answer_part + questionString2));
        

        // Close test frame
        testApp.closeApp();

        // Reopen test frame
        AppHandler testApp2 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI2 = new AppGUI(testApp2);
        testApp2.createGUI(appGUI2);
        testApp2.LogIn("username", "password");

        // Test persistence of remaining question
        HistoryListHandler historyList2 = testApp2.getHistoryList();
        assertTrue(historyList2.getHistoryList().size() == 1);
        HistoryQuestionHandler persistentQuestion = 
            (HistoryQuestionHandler) historyList2.getHistoryList().get(0);
        assertTrue(persistentQuestion.getQuestion().
            equals(questionString2));
        assertTrue(persistentQuestion.getAnswer().
            equals(answer_part + questionString2));
        
        // Test clearing of history
        testApp2.clearAll();
        assertTrue(historyList2.getHistoryList().size() == 0);

        // Close test frame
        testApp2.closeApp();

        // Reopen test frame
        AppHandler testApp3 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI3 = new AppGUI(testApp3);
        testApp3.createGUI(appGUI3);
        testApp3.LogIn("username", "password");

        // Test persistence of cleared history
        HistoryListHandler historyList3 = testApp3.getHistoryList();
        assertTrue(historyList3.getHistoryList().size() == 0);

        // Close test frame
        testApp3.closeApp();
    }


    // Test History Methods Across Two Accounts
    // and test that history is connected to users
    @Test
    void testHistoryTwoAccounts() {
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

        // Confirm that deleting question properly deletes question
        testApp.deleteSelected();
        assertTrue(historyList.getHistoryList().size() == 1);
        HistoryQuestionHandler remainingQuestion = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        assertTrue(remainingQuestion.getQuestion().
            equals(questionString2));
        assertTrue(remainingQuestion.getAnswer().
            equals(answer_part + questionString2));
        

        // Close test frame
        testApp.closeApp();

        // Reopen test frame
        AppHandler testApp2 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI2 = new AppGUI(testApp2);
        testApp2.createGUI(appGUI2);
        testApp2.LogIn("username", "password");

        // Test persistence of remaining question
        HistoryListHandler historyList2 = testApp2.getHistoryList();
        assertTrue(historyList2.getHistoryList().size() == 1);
        HistoryQuestionHandler persistentQuestion = 
            (HistoryQuestionHandler) historyList2.getHistoryList().get(0);
        assertTrue(persistentQuestion.getQuestion().
            equals(questionString2));
        assertTrue(persistentQuestion.getAnswer().
            equals(answer_part + questionString2));
        
        // Test clearing of history
        testApp2.clearAll();
        assertTrue(historyList2.getHistoryList().size() == 0);

        // Close test frame
        testApp2.closeApp();

        // Reopen test frame
        AppHandler testApp3 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI3 = new AppGUI(testApp3);
        testApp3.createGUI(appGUI3);
        LogInWindowHandler logInHandler2 = testApp3.getLogInWindowHandler();
        logInHandler2.createAccount("username2", "password2");
        testApp3.LogIn("username2", "password2");

        // Setup data (add two question and delete a different one 
        // than in previous account), test that expected values are returned

        testApp3.startRecording();
        testApp3.stopRecording();
        testApp3.startRecording();
        testApp3.stopRecording();
        HistoryListHandler historyList3 = testApp3.getHistoryList();

        HistoryQuestionHandler question3 = 
            (HistoryQuestionHandler) historyList3.getHistoryList().get(1);

        // De-select and re-select second question
        testApp3.selectQuestion(question3);
        testApp3.selectQuestion(question3);
        testApp3.deleteSelected();

        assertTrue(historyList3.getHistoryList().size() == 1);
        HistoryQuestionHandler remainingQuestion2 = 
            (HistoryQuestionHandler) historyList3.getHistoryList().get(0);
        assertTrue(remainingQuestion2.getQuestion().
            equals(questionString3));
        assertTrue(remainingQuestion2.getAnswer().
            equals(answer_part + questionString3));

        // Close test frame
        testApp3.closeApp();


        // Reopen test frame and login to previous account
        AppHandler testApp4 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI4 = new AppGUI(testApp4);
        testApp4.createGUI(appGUI4);
        testApp4.LogIn("username2", "password2");

        // Test persistence of previous session with this account
        HistoryListHandler historyList4 = testApp4.getHistoryList();
        assertTrue(historyList4.getHistoryList().size() == 1);
        HistoryQuestionHandler persistentQuestion2 = 
            (HistoryQuestionHandler) historyList4.getHistoryList().get(0);
        assertTrue(persistentQuestion2.getQuestion().
            equals(questionString3));
        assertTrue(persistentQuestion2.getAnswer().
            equals(answer_part + questionString3));

        // Close test frame
        testApp4.closeApp();
    }


}
