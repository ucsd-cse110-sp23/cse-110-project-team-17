package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

import java.io.*;

public class US6Test {

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

    // Test that "Ask a Question" button works as expected
    @Test
    void testGetQuestion() throws IOException, InterruptedException {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IAudioHandler audioHandlerTest = new MockAudioHandler();

        audioHandlerTest.startRecording();
        String filename = audioHandlerTest.stopRecording();
        String question = qHandler.getQuestion(filename);

        // Verify that expected question is equal to actual question
        String filename_first = filename.split("[.]")[0];
        String expected_question = "What is " + filename_first + "?";
        assertEquals(question, expected_question);
    }

    // Test that using ChatGPT displays the expected answer
    @Test 
    void testGPTAskMethod() throws IOException, InterruptedException {

        // Create mock handlers and appframe
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        IAudioHandler audioHandlerTest = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        
        // Use second mock audio handler to obtain expected question
        audioHandlerTest.startRecording();
        String filename = audioHandlerTest.stopRecording();
        String question = qHandler.getQuestion(filename);
        String filename_first = filename.split("[.]")[0];
        String expected_question = "What is " + filename_first + "?";
        assertEquals(question, expected_question);

        // Use chatGPT handler to obtain expected answer
        String answer_part = chatGPT.ask(expected_question);
        
        // Verify that expected question and answer are actually displayed
        // in the chat window
        ChatList chatList = testFrame.getChatList();
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();
        ChatBox question1 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer1 = (ChatBox)
            chatList.getComponents()[1];
        assertTrue(question1.getLabel().equals("Question"));
        assertTrue(answer1.getLabel().equals("Answer"));
        assertTrue(question1.getDialogueText().
            equals(expected_question));
        assertTrue(answer1.getDialogueText().
            equals(answer_part));
    }
}
