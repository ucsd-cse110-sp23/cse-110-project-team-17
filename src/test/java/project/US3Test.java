package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;


public class US3Test {
    @Test
    void testStartAndStopRecording() {
        IAudioHandler audioHandler = new MockAudioHandler();
        audioHandler.startRecording();
        String file1 = audioHandler.stopRecording();
        audioHandler.startRecording();
        String file2 = audioHandler.stopRecording();
        assertTrue(!file1.equals(file2));
    }

    @Test
    void testStartAndStopRecordingGUI() throws IOException, InterruptedException {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        IAudioHandler audioHandler2 = new MockAudioHandler();

        audioHandler2.startRecording();
        String filename = audioHandler2.stopRecording();
        String filename_first = filename.split("[.]")[0];
        String expected_question = "What is " + filename_first + "?";

        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        testFrame.QuestionButtonHandler();
        testFrame.StopButtonHandler();

        ChatList chatList = testFrame.getChatList();
        ChatBox question1 = (ChatBox)
            chatList.getComponents()[0];
        ChatBox answer1 = (ChatBox)
            chatList.getComponents()[1];
        assertTrue(question1.getLabel().equals("Question"));
        assertTrue(answer1.getLabel().equals("Answer"));
        assertTrue(question1.getDialogueText().
            equals(expected_question));
        
        String answer_part = chatGPT.ask(expected_question);
        assertTrue(answer1.getDialogueText().
            equals(answer_part));
    }
}
