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
        AppFrame testFrame0 = new AppFrame(qHandler, chatGPT, audioHandler);

        // Set up expected values
        String questionString1 = "What is project/dummy_audio/TestRecording0?";
        String questionString2 = "What is project/dummy_audio/TestRecording1?";
        String questionString3 = "What is project/dummy_audio/TestRecording2?";
        String questionString4 = "What is project/dummy_audio/TestRecording3?";
        String answer_part = "Mock answer to the following prompt: ";
        
        // Test the "Ask a Question" and "Stop Recording" buttons
        // Verify that the question and answer show up as expected in chat window
        ChatList chatList0 = testFrame0.getChatList();
        testFrame0.QuestionButtonHandler();
        testFrame0.StopButtonHandler();
        ChatBox chatquestion0 = (ChatBox)
            chatList0.getComponents()[0];
        ChatBox answer0 = (ChatBox)
            chatList0.getComponents()[1];
        assertTrue(chatquestion0.getLabel().equals("Question"));
        assertTrue(answer0.getLabel().equals("Answer"));
        assertTrue(chatquestion0.getDialogueText().
            equals(questionString1));
        assertTrue(answer0.getDialogueText().
            equals(answer_part + questionString1));
        testFrame0.closeFrame();

        // Test the permanence of history
        // Verify that the question and answer from old session still show up
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        HistoryList historyList = testFrame.getHistoryList();
        HistoryQuestion question1 = 
            (HistoryQuestion) historyList.getComponents()[0];
        assertTrue(question1.getQuestionText().
            equals(questionString1));

        // Test the "Select" button
        // Verify that the selected question1 and answer show up as expected in chat window
        testFrame.SelectButtonHandler(question1);
        ChatList chatList = testFrame.getChatList();
        ChatBox chatQuestion = 
            (ChatBox) chatList.getComponents()[0];
        ChatBox chatAnswer = 
            (ChatBox) chatList.getComponents()[1];
        assertTrue(chatQuestion.getLabel().equals("Question"));
        assertTrue(chatAnswer.getLabel().equals("Answer"));
        assertTrue(chatQuestion.getDialogueText().
            equals(questionString1));
        assertTrue(chatAnswer.getDialogueText().
            equals(answer_part + questionString1));
        
        // Add a second question
        // Test that the question and answer show up 
        // in history list and chat box as expected
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        ChatBox chatquestion2 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer2 = (ChatBox)
            chatList.getComponents()[1];
        assertTrue(chatquestion2.getLabel().equals("Question"));
        assertTrue(answer2.getLabel().equals("Answer"));
        assertTrue(chatquestion2.getDialogueText().
            equals(questionString2));
        assertTrue(answer2.getDialogueText().
            equals(answer_part + questionString2));
        HistoryQuestion question2 = 
            (HistoryQuestion) historyList.getComponents()[1];
        assertTrue(question2.getQuestionText().
            equals(questionString2));

        // Add a third question
        // Test that the question and answer show up 
        // in history list and chat box as expected
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        ChatBox chatquestion3 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer3 = (ChatBox)
            chatList.getComponents()[1];
        assertTrue(chatquestion3.getLabel().equals("Question"));
        assertTrue(answer3.getLabel().equals("Answer"));
        assertTrue(chatquestion3.getDialogueText().
            equals(questionString3));
        assertTrue(answer3.getDialogueText().
            equals(answer_part + questionString3));
        HistoryQuestion question3 = 
            (HistoryQuestion) historyList.getComponents()[2];
        assertTrue(question3.getQuestionText().
            equals(questionString3));


        // Add a fourth question
        // Test that the question and answer show up 
        // in history list and chat box as expected
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        ChatBox chatquestion4 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer4 = (ChatBox)
            chatList.getComponents()[1];
        assertTrue(chatquestion4.getLabel().equals("Question"));
        assertTrue(answer4.getLabel().equals("Answer"));
        assertTrue(chatquestion4.getDialogueText().
            equals(questionString4));
        assertTrue(answer4.getDialogueText().
            equals(answer_part + questionString4));
        HistoryQuestion question4 = 
            (HistoryQuestion) historyList.getComponents()[3];
        assertTrue(question4.getQuestionText().
            equals(questionString4));

        // Select the first question, then select the fourth question
        // Test deletion of fourth question - chat window should be empty
        // and history list should no longer have that question
        testFrame.SelectButtonHandler(question1);
        testFrame.SelectButtonHandler(question4);
        testFrame.deleteSelectedHandler();
        assertTrue(chatList.getComponents().length == 0);
        assertTrue(historyList.getComponents().length == 3);
        for (Component c : historyList.getComponents()) {
            if (c instanceof HistoryQuestion) {
                HistoryQuestion curr_question = (HistoryQuestion) c;
                assertFalse(curr_question.getQuestionText().
                    equals(questionString4));
            }
        }

        // Test clearing all questions
        // HistoryList should be reset to default
        testFrame.clearAll();
        assertTrue(historyList.getComponentsNum() == 0);
        assertTrue(historyList.getComponents()[0] instanceof JTextArea);

        // Close the test frame
        testFrame.closeFrame();
    }
}
