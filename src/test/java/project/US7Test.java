package project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.handler.*;
import project.prompt_handler.*;


public class US7Test {

    @BeforeEach
    void wipeDataBefore() {
        DBCreate.wipeDB();
    }
    
    @AfterAll
    static void wipeDataAfter() {
        DBCreate.wipeDB();
    }

    @Test
    void verifyUsername() {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
 
        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        assertTrue(loginHandler.createAccount("reisandy", "1234", "1234"));
        assertFalse(loginHandler.createAccount("reisandy", "1234", "1234"));
        assertTrue(loginHandler.verifyUsername("reisandy", "1234"));
        testApp.stopServer();
    }
    
    @Test
    void verifyPassword() {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);

        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        loginHandler.createAccount("joseph", "jo", "1234");
        assertFalse(loginHandler.verifyPassword("joseph", "jo"));
        assertFalse(loginHandler.verifyPassword("joseph", "1234"));
        loginHandler.createAccount("joseph", "jo", "jo");
        assertFalse(loginHandler.verifyPassword("joseph", "1234"));
        assertTrue(loginHandler.verifyPassword("joseph", "jo"));
        testApp.stopServer();
    }
}