
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JButton;

import org.junit.jupiter.api.Test;

public class US2Test {
    @Test 
    public void testChatGPT() {
        AppFrame testFrame = new AppFrame();
        assertTrue(testFrame.getChatGPT() instanceof IChatGPT);
    }

    @Test 
    public void testNewQuestionButton() {
        AppFrame testFrame = new AppFrame();
        assertTrue(testFrame.getAskButton() instanceof JButton);
    }

    @Test 
    public void testQuestionButtonToggle() {
        AppFrame testFrame = new AppFrame();
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
    public void testStory() {
        AppFrame testFrame = new AppFrame();
        JButton questionButton = testFrame.getAskButton();
        JButton stopButton = testFrame.getStopButton();
        assertTrue(questionButton.isVisible());
        assertFalse(stopButton.isVisible());
        testFrame.QuestionButtonHandler();
        assertFalse(questionButton.isVisible());
        assertTrue(stopButton.isVisible());
    }

    
}
