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

import java.io.*;

public class US3Test {

    // Delete history.csv file (fresh start)
    @BeforeEach
    void cleanHistory() {
        String filename = "project/history.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File historyFile = new File(filename);
        historyFile.delete();
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
        String expected_question = "What is " + filename_first + "?";

        // Create appframe
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        testApp.createGUI();
        AppGUI appGUI = testApp.getAppGUI();

        // Verify that on stopping and starting recording, the actual 
        // chat window displays the expected dialogue
        appGUI.QuestionButtonHandler();
        appGUI.StopButtonHandler();
        ChatWindowGUI chatWindow = appGUI.getChatWindow();
        ChatBoxGUI question1 = (ChatBoxGUI)
            chatWindow.getComponents()[0];
        ChatBoxGUI answer1 = (ChatBoxGUI)
            chatWindow.getComponents()[1];
        assertTrue(question1.getLabel().equals("Question"));
        assertTrue(answer1.getLabel().equals("Answer"));
        assertTrue(question1.getDialogueText().
            equals(expected_question));
        String answer_part = chatGPT.ask(expected_question);
        assertTrue(answer1.getDialogueText().
            equals(answer_part));

        // Close test frame
        testApp.closeApp();
    }
}
