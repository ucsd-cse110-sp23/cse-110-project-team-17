package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import javax.swing.JTextArea;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;
import project.handler.*;

public class US5Test {

    // Delete history.csv file (fresh start)
    @BeforeEach
    void cleanHistory() {
        String filename = "project/history.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        System.out.println(System.getProperty("user.dir"));
        System.out.println(filename);
        File historyFile = new File(filename);
        historyFile.delete();
        System.out.println("Nuked history file.");
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

    // Test deletion when history list is empty
    @Test
    void testDeleteNothingSelected() throws IOException {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        String regex = ";;;";
        HTTPRequestMaker httpRequestMaker = testApp.getRequestMaker();
        HistoryListHandler historyList = new HistoryListHandler(regex, httpRequestMaker);
        assertTrue(0 == historyList.getHistoryList().size());
        historyList.deleteSelected();
        assertTrue(0 == historyList.getHistoryList().size());

        // Close app
        testApp.closeApp();
    }

    // Test deletion when no history question is selected
    @Test
    void testDeleteNothingSelectedMultiQuestions() throws IOException {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        String regex = ";;;";
        HTTPRequestMaker httpRequestMaker = testApp.getRequestMaker();
        HistoryListHandler historyList = new HistoryListHandler(regex, httpRequestMaker);
        HistoryQuestionHandler Q1 = 
            new HistoryQuestionHandler("1", httpRequestMaker);
        HistoryQuestionHandler Q2 = 
            new HistoryQuestionHandler("2", httpRequestMaker);
        HistoryQuestionHandler Q3 = 
            new HistoryQuestionHandler("3", httpRequestMaker);
        historyList.add(Q3, false);
        historyList.add(Q2, false);
        historyList.add(Q1, false);
        assertTrue(3 == historyList.getHistoryList().size());
        historyList.deleteSelected();
        assertTrue(3 == historyList.getHistoryList().size());

        // Close app
        testApp.closeApp();
    }

    // Test deletion when deleting the last question
    @Test
    void testDeleteSelectedLastQuestion() throws IOException {

        String question1 = "question1";
        String answer1 = "answer1";
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        String regex = ";;;";
        HTTPRequestMaker httpRequestMaker = testApp.getRequestMaker();
        httpRequestMaker.postRequest("1", question1, answer1);
        HistoryListHandler historyList = new HistoryListHandler(regex, httpRequestMaker);

        HistoryQuestionHandler Q1 = 
            new HistoryQuestionHandler("1", httpRequestMaker);
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
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        String regex = ";;;";
        HTTPRequestMaker httpRequestMaker = testApp.getRequestMaker();
        HistoryListHandler historyList = new HistoryListHandler(regex, httpRequestMaker);

        String question1 = "What is 2+2?";
        String answer1 = "4";
        String question2 = "What is the date?";
        String answer2 = "05/14";

        httpRequestMaker.postRequest("1", question1, answer1);
        httpRequestMaker.postRequest("2", question2, answer2);

        HistoryQuestionHandler Q1 = 
            new HistoryQuestionHandler("1", httpRequestMaker);
        Q1.toggleSelected();
        HistoryQuestionHandler Q2 = 
            new HistoryQuestionHandler("2", httpRequestMaker);
        Q2.toggleSelected();
        Q2.toggleSelected();
        historyList.add(Q1, false);
        historyList.add(Q2, false);
        assertTrue(2==historyList.getHistoryList().size());
        historyList.deleteSelected();
        assertTrue(1 == historyList.getHistoryList().size());

        HistoryQuestionHandler remainingQ = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        assertTrue(remainingQ.getQuestion().equals(question2));

        // Close app
        testApp.closeApp();
    }
}