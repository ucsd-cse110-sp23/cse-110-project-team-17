package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.handler.*;
import project.prompt_handler.*;

import java.util.*;

public class US11Test {
    @BeforeEach
    void wipeDataBefore() {
        DBCreate.wipeDB();
    }
    
    @AfterAll
    static void wipeDataAfter() {
        DBCreate.wipeDB();
    }

    @Test
    void testSetupEmailCommand() {
        PromptHandler questionHandler = new PromptHandler();
        String prompt = "Set up email";
        String command = "Setup email";
        assertTrue(questionHandler.getCommand(prompt).equals(command));
    }

    @Test
    void verifyUsername() {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);


        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        loginHandler.createAccount("joseph", "jo", "jo");
        testApp.LogIn("joseph", "jo");
        setupEmailHandler emailHandler = testApp.getSetupEmailHandler();


        emailHandler.setFirstName("Joseph");
        emailHandler.setLastName("Yeh");
        emailHandler.setEmailAddress("josephyeh0903@gmail.com");
        emailHandler.setEmailPassword("1234");
        emailHandler.setSMTPHost("3456");
        emailHandler.setTLSPort("5678");
        emailHandler.setDisplayName("Joseph Yeh");
        emailHandler.addCurrEmailInfo();



        Map<String, String> emailInfo = DBCreate.readEmailInformation("joseph");
        
        assertTrue(emailInfo.get("firstName_id").equals("Joseph"));
        assertTrue(emailInfo.get("lastName_id").equals("Yeh"));
        assertTrue(emailInfo.get("emailAddress_id").equals("josephyeh0903@gmail.com"));
        assertTrue(emailInfo.get("emailPassword_id").equals("1234"));
        assertTrue(emailInfo.get("SMTPHost_id").equals("3456"));
        assertTrue(emailInfo.get("TLSPort_id").equals("5678"));
        assertTrue(emailInfo.get("displayName_id").equals("Joseph Yeh"));
        testApp.stopServer();
    }
}
