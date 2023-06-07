package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.gui.AppGUI;
import project.question_handler.*;
import project.handler.*;

import java.io.File;

public class DS4_1Test {
    
    // Wipe all account histories before
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
        DBCreate.wipeDB();
    }

    // Wipe all account histories after
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
        DBCreate.wipeDB();
    }

    @Test
    void iterationTest() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");


        setupEmailHandler emailHandler = testApp.getSetupEmailHandler();


        emailHandler.setFirstName("Daniel");
        emailHandler.setLastName("Tran");
        emailHandler.setEmailAddress("orpheus157@gmail.co");
        emailHandler.setEmailPassword("vsuwlvowusinisbt");
        emailHandler.setSMTPHost("smtp.gmail.com");
        emailHandler.setTLSPort("587");
        emailHandler.setDisplayName("Daniel Tran");
        emailHandler.addCurrEmailInfo();

         // Initialize prompts and commands
        String answer_part = "Mock answer to the following prompt: ";
        String create_email = "Create email to Daniel. Let's meet at Geisel at 7 pm";
        String ceCommand = "Create email";
        String send_email = "Send email to dnt004 at ucsd.edu";
        String seCommand = "Send email";
        String delete = "Delete.";
        String dCommand = "Delete";

        // Initialize result messages
        String emailSentString = "Email successfully sent.";
        String wrongUsername = "Username and Password not accepted.";

        // Test question command
        testApp.handleCommand(create_email, ceCommand);
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryQuestionHandler prompt1 = historyList.getHistoryList().get(0);
        assertEquals(prompt1.getQuestion(),
            (create_email));
        assertEquals(prompt1.getAnswer(),
            (answer_part + create_email + "Daniel Tran"));

        prompt1.select();

        testApp.handleCommand(send_email, seCommand);
        HistoryQuestionHandler prompt2 = historyList.getHistoryList().get(1);
        assertTrue(prompt2.getAnswer().
            contains(wrongUsername));
        
        
        prompt2.select();
        // Test delete command
        testApp.handleCommand(delete, dCommand);
        assertTrue(historyList.getHistoryList().size() == 1);


        emailHandler.setEmailAddress("orpheus157@gmail.com");
        emailHandler.addCurrEmailInfo();

        prompt1.select();

        testApp.handleCommand(send_email, seCommand);
        HistoryQuestionHandler prompt3 = historyList.getHistoryList().get(1);
        assertEquals(prompt3.getAnswer(),
            (emailSentString));

        // Close test frame
        testApp.closeApp();
    }
}