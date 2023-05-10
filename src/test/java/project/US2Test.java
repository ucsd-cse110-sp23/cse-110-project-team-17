package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.swing.JButton;

public class US2Test {
    @Test 
    void testChatGPT() {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new MockChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        assertTrue(testFrame.getChatGPT() instanceof IChatGPT);
    }

    @Test 
    void testNewQuestionButton() {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new ChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        assertTrue(testFrame.getAskButton() instanceof JButton);
    }

    @Test 
    void testQuestionButtonToggle() {
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new ChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
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
        IQuestionHandler qHandler = new MockQuestion();
        IChatGPT chatGPT = new ChatGPT();
        AppFrame testFrame = new AppFrame(qHandler, chatGPT);
        JButton questionButton = testFrame.getAskButton();
        JButton stopButton = testFrame.getStopButton();
        assertTrue(questionButton.isVisible());
        assertFalse(stopButton.isVisible());
        testFrame.QuestionButtonHandler();
        assertFalse(questionButton.isVisible());
        assertTrue(stopButton.isVisible());
    }

    
}
