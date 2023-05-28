package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

import java.io.*;

public class US7Test {
    @Test
    void verifyUsername() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
 
        LogInWindowGUI loginWindow = appGUI.getLoginWindow();
        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        assertTrue(loginHandler.createAccount("reisandy", "rei"));
        assertTrue(loginHandler.verifyUsername("reisandy"));
    }
    
    @Test
    void verifyPassword() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        //testApp.createGUI(appGUI);

        LogInWindowGUI loginWindow = appGUI.getLoginWindow();
        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        loginHandler.createAccount("joseph", "jo");
        assertTrue(loginHandler.verifyPassword("joseph", "jo"));
        assertTrue(loginHandler.createAccount("reisandy", "rei"));
    }
}