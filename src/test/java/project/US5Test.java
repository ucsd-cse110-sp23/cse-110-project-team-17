package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import javax.swing.JTextArea;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.gui.*;
import project.handler.*;
import project.prompt_handler.*;

public class US5Test {

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

    // Test deletion when history list is empty
    @Test
    void testDeleteNothingSelected() throws IOException {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");
        HistoryListHandler historyList = testApp.getHistoryList();
        assertTrue(0 == historyList.getHistoryList().size());
        historyList.deleteSelected();
        assertTrue(0 == historyList.getHistoryList().size());

        // Close app
        testApp.closeApp();
    }

    // Test deletion when no history question is selected
    @Test
    void testDeleteNothingSelectedMultiQuestions() throws IOException {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");
        HTTPRequestMaker httpRequestMaker = testApp.getRequestMaker();
        HistoryListHandler historyList = testApp.getHistoryList();
        testApp.startRecording();
        testApp.stopRecording();
        testApp.startRecording();
        testApp.stopRecording();
        testApp.startRecording();
        testApp.stopRecording();
        assertEquals(3, historyList.getHistoryList().size());
        HistoryPromptHandler question3 = historyList.getHistoryList().get(2);
        question3.deselect();
        historyList.deleteSelected();
        assertEquals(3, historyList.getHistoryList().size());

        // Close app
        testApp.closeApp();
    }

    // Test deletion when deleting the last question
    @Test
    void testDeleteSelectedLastQuestion() throws IOException {

        String question1 = "question1";
        String answer1 = "answer1";
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");
        HTTPRequestMaker httpRequestMaker = testApp.getRequestMaker();
        httpRequestMaker.postRequest("1", question1, answer1);
        HistoryListHandler historyList = testApp.getHistoryList();

        HistoryPromptHandler Q1 = 
            new HistoryPromptHandler("1", httpRequestMaker);
        Q1.toggleSelected();
        historyList.add(Q1, false);
        assertTrue(1 == historyList.getHistoryList().size());
        historyList.deleteSelected();
        assertTrue(0 == historyList.getHistoryList().size());
        HistoryListGUI historyListGUI = historyList.getHistoryListGUI();
        assertTrue(historyListGUI.getComponents()[1] instanceof JTextArea);

        // Close app
        testApp.closeApp();
    }

    // Test deletion when deleting a question while there are multiple questions
    @Test
    void testDeleteSelectedMultiQuestions() throws IOException {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");
        HTTPRequestMaker httpRequestMaker = testApp.getRequestMaker();
        HistoryListHandler historyList = testApp.getHistoryList();

        String question1 = "What is 2+2?";
        String answer1 = "4";
        String question2 = "What is the date?";
        String answer2 = "05/14";

        httpRequestMaker.postRequest("1", question1, answer1);
        httpRequestMaker.postRequest("2", question2, answer2);

        HistoryPromptHandler Q1 = 
            new HistoryPromptHandler("1", httpRequestMaker);
        Q1.toggleSelected();
        HistoryPromptHandler Q2 = 
            new HistoryPromptHandler("2", httpRequestMaker);
        Q2.toggleSelected();
        Q2.toggleSelected();
        historyList.add(Q1, false);
        historyList.add(Q2, false);
        assertTrue(2==historyList.getHistoryList().size());
        historyList.deleteSelected();
        assertTrue(1 == historyList.getHistoryList().size());

        HistoryPromptHandler remainingQ = 
            (HistoryPromptHandler) historyList.getHistoryList().get(0);
        assertTrue(remainingQ.getPrompt().equals(question2));

        // Close app
        testApp.closeApp();
    }
}