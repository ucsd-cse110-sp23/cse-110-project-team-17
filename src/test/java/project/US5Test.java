package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import javax.swing.JTextArea;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

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

    // Test deletion when history list is empty
    @Test
    void testDeleteNothingSelected() throws IOException {
        HistoryList historyList = new HistoryList();
        assertTrue(0 == historyList.getComponentsNum());
        historyList.deleteSelected();
        assertTrue(0 == historyList.getComponentsNum());
    }

    // Test deletion when no history question is selected
    @Test
    void testDeleteNothingSelectedMultiQuestions() throws IOException {
        HistoryList historyList = new HistoryList();
        String question1 = "question1";
        String question2 = "question2";
        String question3 = "question3";
        String answer1 = "answer1";
        String answer2 = "answer2";
        String answer3 = "answer3";
        HistoryQuestion Q1 = new HistoryQuestion("1", question1, answer1);
        HistoryQuestion Q2 = new HistoryQuestion("2", question2, answer2);
        HistoryQuestion Q3= new HistoryQuestion("3", question3, answer3);
        historyList.add(Q3);
        historyList.add(Q2);
        historyList.add(Q1);
        assertTrue(3 == historyList.getComponentsNum());
        historyList.deleteSelected();
        assertTrue(3 == historyList.getComponentsNum());
    }

    // Test deletion when deleting the last question
    @Test
    void testDeleteSelectedLastQuestion() throws IOException {

        String question1 = "question1";
        String answer1 = "answer1";
        HistoryList historyList = new HistoryList();

        HistoryQuestion Q1 = new HistoryQuestion("1", question1, answer1);
        Q1.changeState();
        historyList.add(Q1);
        assertTrue(1 == historyList.getComponentsNum());
        historyList.deleteSelected();
        assertTrue(0 == historyList.getComponentsNum());
        assertTrue(historyList.getComponents()[0] instanceof JTextArea);
    }

    // Test deletion when deleting a question while there are multiple questions
    @Test
    void testDeleteSelectedMultiQuestions() throws IOException {
        HistoryList historyList = new HistoryList();

        String question1 = "What is 2+2?";
        String answer1 = "4";
        String question2 = "What is the date?";
        String answer2 = "05/14";

        HistoryQuestion Q1 = new HistoryQuestion("1", question1, answer1);
        Q1.changeState();
        HistoryQuestion Q2 = new HistoryQuestion("2", question2, answer2);
        Q2.changeState();
        Q2.changeState();
        historyList.add(Q1);
        historyList.add(Q2);
        assertTrue(2==historyList.getComponentsNum());
        historyList.deleteSelected();
        assertTrue(1 == historyList.getComponentsNum());

        HistoryQuestion remainingQ = (HistoryQuestion) historyList.getComponent(1);
        assertTrue(remainingQ.getQuestionText() == "What is the date?");
    }
    
    // Test that delete button is visible on the GUI
    @Test
    void test_deleteButtonGUI() throws IOException {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        
        assertTrue(testFrame.getDeleteButton().isVisible()); //test deleteSelected button is visible
        testFrame.closeFrame();
    }
}