package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.swing.JButton;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
// import project.gui.*;

public class US2Test {
    @Test 
    void testChatGPT() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        assertTrue(testFrame.getChatGPT() instanceof IChatGPT);
    }

    @Test 
    void testNewQuestionButton() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new ChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        assertTrue(testFrame.getAskButton() instanceof JButton);
    }

    @Test 
    void testQuestionButtonToggle() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new ChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        JButton questionButton = testFrame.getAskButton();
        JButton stopButton = testFrame.getStopButton();
        assertTrue(questionButton.isVisible());
        assertFalse(stopButton.isVisible());
        testFrame.QuestionButtonHandler();
        assertFalse(questionButton.isVisible());
        assertTrue(stopButton.isVisible());
        testFrame.StopButtonHandler();
        assertTrue(questionButton.isVisible());
        assertFalse(stopButton.isVisible());
    }

    @Test
    void testStory() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new ChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT, audioHandler);
        JButton questionButton = testFrame.getAskButton();
        JButton stopButton = testFrame.getStopButton();
        assertTrue(questionButton.isVisible());
        assertFalse(stopButton.isVisible());
        testFrame.QuestionButtonHandler();
        assertFalse(questionButton.isVisible());
        assertTrue(stopButton.isVisible());
    }

    
}
