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

import java.awt.Component;

import java.io.*;

import javax.swing.JTextArea;
public class DS2_1Test {
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

    @Test
    public void iterationTest() {
        // Create mock handlers and appframe
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp0 = new AppHandler(qHandler, chatGPT, audioHandler);
        testApp0.createGUI();
        AppGUI appGUI0 = testApp0.getAppGUI();

        // Set up expected values
        String questionString1 = "What is project/dummy_audio/TestRecording0?";
        String questionString2 = "What is project/dummy_audio/TestRecording1?";
        String questionString3 = "What is project/dummy_audio/TestRecording2?";
        String questionString4 = "What is project/dummy_audio/TestRecording3?";
        String answer_part = "Mock answer to the following prompt: ";
        
        // Test the "Ask a Question" and "Stop Recording" buttons
        // Verify that the question and answer show up as expected in chat window
        ChatWindowGUI chatWindow0 = appGUI0.getChatWindow();
        appGUI0.QuestionButtonHandler();
        appGUI0.StopButtonHandler();
        ChatBoxGUI chatquestion0 = (ChatBoxGUI)
            chatWindow0.getComponents()[0];
        ChatBoxGUI answer0 = (ChatBoxGUI)
            chatWindow0.getComponents()[1];
        assertTrue(chatquestion0.getLabel().equals("Question"));
        assertTrue(answer0.getLabel().equals("Answer"));
        assertTrue(chatquestion0.getDialogueText().
            equals(questionString1));
        assertTrue(answer0.getDialogueText().
            equals(answer_part + questionString1));
        testApp0.closeApp();

        // Test the permanence of history
        // Verify that the question and answer from old session still show up
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        testApp.createGUI();
        AppGUI appGUI = testApp.getAppGUI();
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryQuestionHandler question1 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        assertTrue(question1.getQuestion().
            equals(questionString1));

        // Test the "Select" button
        // Verify that the selected question1 and answer show up as expected in chat window
        appGUI.SelectButtonHandler(question1.getHistoryQuestionGUI());
        ChatWindowGUI chatWindow = appGUI.getChatWindow();
        ChatBoxGUI chatQuestion = 
            (ChatBoxGUI) chatWindow.getComponents()[0];
        ChatBoxGUI chatAnswer = 
            (ChatBoxGUI) chatWindow.getComponents()[1];
        assertTrue(chatQuestion.getLabel().equals("Question"));
        assertTrue(chatAnswer.getLabel().equals("Answer"));
        assertTrue(chatQuestion.getDialogueText().
            equals(questionString1));
        assertTrue(chatAnswer.getDialogueText().
            equals(answer_part + questionString1));
        
        // Add a second question
        // Test that the question and answer show up 
        // in history list and chat box as expected
        appGUI.QuestionButtonHandler();
        appGUI.StopButtonHandler();
        ChatBoxGUI chatQuestion2 = 
            (ChatBoxGUI) chatWindow.getComponents()[0];
        ChatBoxGUI chatAnswer2 = 
            (ChatBoxGUI) chatWindow.getComponents()[1];
        assertTrue(chatQuestion2.getLabel().equals("Question"));
        assertTrue(chatAnswer2.getLabel().equals("Answer"));
        assertTrue(chatQuestion2.getDialogueText().
            equals(questionString2));
        assertTrue(chatAnswer2.getDialogueText().
            equals(answer_part + questionString2));
        HistoryQuestionHandler question2 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(1);
        assertTrue(question2.getQuestion().
            equals(questionString2));

        // Add a third question
        // Test that the question and answer show up 
        // in history list and chat box as expected
        appGUI.QuestionButtonHandler();
        appGUI.StopButtonHandler();
        ChatBoxGUI chatQuestion3 = 
            (ChatBoxGUI) chatWindow.getComponents()[0];
        ChatBoxGUI chatAnswer3 = 
            (ChatBoxGUI) chatWindow.getComponents()[1];
        assertTrue(chatQuestion3.getLabel().equals("Question"));
        assertTrue(chatAnswer3.getLabel().equals("Answer"));
        assertTrue(chatQuestion3.getDialogueText().
            equals(questionString3));
        assertTrue(chatAnswer3.getDialogueText().
            equals(answer_part + questionString3));
        HistoryQuestionHandler question3 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(2);
        assertTrue(question3.getQuestion().
            equals(questionString3));


        // Add a fourth question
        // Test that the question and answer show up 
        // in history list and chat box as expected
        appGUI.QuestionButtonHandler();
        appGUI.StopButtonHandler();
        ChatBoxGUI chatQuestion4 = 
            (ChatBoxGUI) chatWindow.getComponents()[0];
        ChatBoxGUI chatAnswer4 = 
            (ChatBoxGUI) chatWindow.getComponents()[1];
        assertTrue(chatQuestion4.getLabel().equals("Question"));
        assertTrue(chatAnswer4.getLabel().equals("Answer"));
        assertTrue(chatQuestion4.getDialogueText().
            equals(questionString4));
        assertTrue(chatAnswer4.getDialogueText().
            equals(answer_part + questionString4));
        HistoryQuestionHandler question4 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(3);
        assertTrue(question4.getQuestion().
            equals(questionString4));

        // Select the first question, then select the fourth question
        // Test deletion of fourth question - chat window should be empty
        // and history list should no longer have that question
        appGUI.SelectButtonHandler(question1.getHistoryQuestionGUI());
        appGUI.SelectButtonHandler(question4.getHistoryQuestionGUI());
        appGUI.deleteSelectedHandler();
        assertTrue(chatWindow.getComponents().length == 0);
        assertTrue(historyList.getHistoryList().size() == 3);
        for (HistoryQuestionHandler hqh : historyList.getHistoryList()) {
            assertFalse(hqh.getQuestion().
                equals(questionString4));
        }

        // Test clearing all questions
        // HistoryList should be reset to default
        appGUI.clearAllHandler();
        assertTrue(historyList.getHistoryList().size() == 0);
        assertTrue(historyList.getHistoryListGUI().getComponents()[1] instanceof JTextArea);

        // Close the test frame
        testApp.closeApp();
    }
}
