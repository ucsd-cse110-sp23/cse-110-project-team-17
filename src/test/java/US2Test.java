<<<<<<<< HEAD:src/test/java/project/US2Test.java
package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
========
import static org.junit.jupiter.api.Assertions.*;
>>>>>>>> main:src/test/java/US2Test.java

import javax.swing.JButton;

public class US2Test {
    @Test 
    void testChatGPT() {
        AppFrame testFrame = new AppFrame();
        assertTrue(testFrame.getChatGPT() instanceof IChatGPT);
    }

    @Test 
    void testNewQuestionButton() {
        AppFrame testFrame = new AppFrame();
        assertTrue(testFrame.getAskButton() instanceof JButton);
    }

    @Test 
    void testQuestionButtonToggle() {
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
    void testStory() {
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
