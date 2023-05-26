package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;


import java.io.*;

import javax.swing.JTextArea;

public class DS2_2Test {
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
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);

        // Set up expected values
        String questionString1 = "What is project/dummy_audio/TestRecording0?";
        String answer_part = "Mock answer to the following prompt: ";
        
        // Test the "Ask a Question" and "Stop Recording" buttons
        // Verify that the question and answer show up as expected in chat window
        ChatWindowGUI chatWindow = appGUI.getChatWindow();
        appGUI.QuestionButtonHandler();
        appGUI.StopButtonHandler();
        ChatBoxGUI chatquestion = (ChatBoxGUI)
            chatWindow.getComponents()[0];
        ChatBoxGUI answer = (ChatBoxGUI)
            chatWindow.getComponents()[1];
        assertTrue(chatquestion.getLabel().equals("Question"));
        assertTrue(answer.getLabel().equals("Answer"));
        assertTrue(chatquestion.getDialogueText().
            equals(questionString1));
        assertTrue(answer.getDialogueText().
            equals(answer_part + questionString1));
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryQuestionHandler question1 = 
            (HistoryQuestionHandler) historyList.getHistoryList().get(0);
        assertTrue(question1.getQuestion().
            equals(questionString1));

        // Select first question
        // Test deletion of first question - chat window should be empty
        // and history list should be set to default
        appGUI.SelectButtonHandler(question1.getHistoryQuestionGUI());
        appGUI.deleteSelectedHandler();
        assertTrue(chatWindow.getComponents().length == 0);
        assertTrue(historyList.getHistoryList().size() == 0);
        assertTrue(historyList.getHistoryListGUI().getComponents()[1] instanceof JTextArea);
        

        // Test clearing all questions
        // HistoryList should still be set to the default
        appGUI.clearAllHandler();
        assertTrue(historyList.getHistoryList().size() == 0);
        assertTrue(historyList.getHistoryListGUI().getComponents()[1] instanceof JTextArea);

        // Close the test frame
        testApp.closeApp();
    }
    
}
