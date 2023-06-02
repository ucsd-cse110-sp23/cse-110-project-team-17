package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.gui.AppGUI;
import project.gui.InputTextField;
import project.gui.setupEmailGUI;
import project.question_handler.*;
import project.handler.*;

import java.io.File;
import java.util.ArrayList;

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
        QuestionHandler questionHandler = new QuestionHandler();
        String prompt = "Set up email";
        String command = "Setup email";
        assertTrue(questionHandler.getCommand(prompt).equals(command));
    }

    @Test
    void verifyUsername() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        ArrayList<String[]> allEmails = DBCreate.readEmailInformation();
        String[] emailInfo = allEmails.get(0);
        
        assertTrue(emailInfo[0].equals("Joseph"));
        assertTrue(emailInfo[1].equals("Yeh"));
        assertTrue(emailInfo[2].equals("joseph"));
        assertTrue(emailInfo[3].equals("josephyeh0903@gmail.com"));
        assertTrue(emailInfo[6].equals("1234"));
        assertTrue(emailInfo[4].equals("1234"));
        assertTrue(emailInfo[5].equals("1234"));
        testApp.stopServer();
    }
}
