package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.handler.*;


public class US7Test {
    @Test
    void verifyUsername() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
 
        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        assertFalse(loginHandler.createAccount("reisandy", "1234"));
        assertTrue(loginHandler.verifyUsername("reisandy", "1234"));
        testApp.stopServer();
    }
    
    @Test
    void verifyPassword() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);

        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        //loginHandler.createAccount("joseph", "jo");
        assertTrue(loginHandler.verifyPassword("joseph", "1234"));
        assertFalse(loginHandler.createAccount("joseph", "1234"));
        testApp.stopServer();
    }
}