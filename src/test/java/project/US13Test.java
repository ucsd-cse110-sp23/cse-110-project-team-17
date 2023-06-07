package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.gui.AppGUI;
import project.handler.*;
import project.prompt_handler.*;

import java.io.File;

public class US13Test {

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
    void testIncorrectEmailSetup() {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
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
        String send_email = "Send email to Jim";
        String seCommand = "Send email";

        // Initialize error messages
        String wrongUsername = "Username and Password not accepted.";
        String wrongSMTP = "Couldn't connect to host";
        String wrongRecipient = "Invalid Addresses";
        String noEmailSelected = "No email selected.";

        // Test question command
        testApp.handleCommand(create_email, ceCommand);
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryPromptHandler prompt1 = historyList.getHistoryList().get(0);
        assertEquals(prompt1.getPrompt(),
            (create_email));
        assertEquals(prompt1.getAnswer(),
            (answer_part + create_email + "Daniel Tran"));

        prompt1.select();

        testApp.handleCommand(send_email, seCommand);
        HistoryPromptHandler prompt2 = historyList.getHistoryList().get(1);
        assertTrue(prompt2.getAnswer().
            contains(wrongUsername));

        
        emailHandler.setEmailAddress("orpheus157@gmail.com");
        emailHandler.setSMTPHost("smtp.gmail.co");
        emailHandler.addCurrEmailInfo();
        prompt1.select();
        testApp.handleCommand(send_email, seCommand);
        HistoryPromptHandler prompt3 = historyList.getHistoryList().get(2);
        assertTrue(prompt3.getAnswer().
            contains(wrongSMTP));

        
        emailHandler.setSMTPHost("smtp.gmail.com");
        emailHandler.addCurrEmailInfo();
        prompt1.select();
        testApp.handleCommand(send_email, seCommand);
        HistoryPromptHandler prompt4 = historyList.getHistoryList().get(3);
        assertTrue(prompt4.getAnswer().
            contains(wrongRecipient));

        
        prompt2.select();
        testApp.handleCommand(send_email, seCommand);
        HistoryPromptHandler prompt5 = historyList.getHistoryList().get(4);
        assertTrue(prompt5.getAnswer().
            contains(noEmailSelected));

        
        testApp.handleCommand(send_email, seCommand);
        HistoryPromptHandler prompt6 = historyList.getHistoryList().get(5);
        assertTrue(prompt6.getAnswer().
            contains(noEmailSelected));

        // Close test frame
        testApp.closeApp();
    }


    @Test
    void testCorrectEmailSetup() {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");


        setupEmailHandler emailHandler = testApp.getSetupEmailHandler();


        emailHandler.setFirstName("Daniel");
        emailHandler.setLastName("Tran");
        emailHandler.setEmailAddress("orpheus157@gmail.com");
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

        // Initialize result message
        String emailSentString = "Email successfully sent.";

        // Test question command
        testApp.handleCommand(create_email, ceCommand);
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryPromptHandler prompt1 = historyList.getHistoryList().get(0);
        assertEquals(prompt1.getPrompt(),
            (create_email));
        assertEquals(prompt1.getAnswer(),
            (answer_part + create_email + "Daniel Tran"));

        prompt1.select();

        testApp.handleCommand(send_email, seCommand);
        HistoryPromptHandler prompt2 = historyList.getHistoryList().get(1);
        assertEquals(prompt2.getAnswer(),
            (emailSentString));

        // Close test frame
        testApp.closeApp();
    }
}