package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;
import project.handler.*;

import java.io.File;

public class US9Test {

    @BeforeEach
    void wipeAccountsBefore() {
        String filename = "project/accounts.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File accountFile = new File(filename);
        accountFile.delete();
    }

    @AfterAll
    static void wipeAccountsAfter() {
        String filename = "project/accounts.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File accountFile = new File(filename);
        accountFile.delete();
    }

    @Test
    void recordLogInInfo() {
        AutomaticLogInHandler alHandler = new AutomaticLogInHandler();
        alHandler.recordLogInInfo("a", "b");
        String[] account_info = alHandler.getLogInInfo();
        String username = account_info[0];
        String password = account_info[1];
        assertEquals("a", username);
        assertEquals("b", password);
    }

    @Test
    void testBDDAutoLogIn() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
 
        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        assertTrue(loginHandler.verifyUsername("reisandy", "1234"));
        AutomaticLogInHandler alHandler = testApp.getAutomaticLogInHandler();
        alHandler.update("reisandy", "1234");
        assertTrue(testApp.autoLogin());
    }

    @Test
    void testBDDNoAutoLogIn() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
 
        LogInWindowHandler loginHandler = testApp.getLogInWindowHandler();
        assertTrue(loginHandler.verifyUsername("reisandy", "1234"));
        assertTrue(!testApp.autoLogin());
    }
}
