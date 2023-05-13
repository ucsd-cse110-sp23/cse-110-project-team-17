package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

public class US6Test {
    @Test 
    void testAnswer() throws IOException, InterruptedException {

        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        IAudioHandler audioHandlerTest = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        ChatList chatList = testFrame.getChatList();

        audioHandlerTest.startRecording();
        String filename = audioHandlerTest.stopRecording();
        String question = qHandler.getQuestion(filename);

        String filename_first = filename.split("[.]")[0];
        String expected_question = "What is " + filename_first + "?";
        assertEquals(question, expected_question);

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
        
        String answer_part = chatGPT.ask(expected_question);
        assertTrue(answer1.getDialogueText().
            equals(answer_part));
    }
}
