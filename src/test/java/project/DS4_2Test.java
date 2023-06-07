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

public class DS4_2Test {
    
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
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");

        AutomaticLogInHandler alHandler = testApp.getAutomaticLogInHandler();
        alHandler.update("username", "password");


        // Set up expected values
        String questionString1 = "Question: What is project/dummy_audio/Question0?";
        String questionString2 = "Question: What is project/dummy_audio/Question1?";
        String questionString3 = "Question: What is project/dummy_audio/Question2?";
        String questionString4 = "Question: What is project/dummy_audio/Question3?";
        String answer_part = "Mock answer to the following prompt: ";
        
        // Test the "Ask a Question" and "Stop Recording" buttons
        // Verify that the question and answer show up as expected in chat window
        HistoryListHandler historyList = testApp.getHistoryList();
        testApp.startRecording();
        testApp.stopRecording();
        HistoryPromptHandler question1 = historyList.getHistoryList().get(0);
        assertTrue(question1.getPrompt().
            equals(questionString1));
        assertTrue(question1.getAnswer().
            equals(answer_part + questionString1));

        // Test the "Select" button
        testApp.selectPrompt(question1);
        testApp.selectPrompt(question1);
        assertTrue(question1.isSelected());
        
        // Add a second question
        // Test that the question and answer show up 
        // in history list as expected
        testApp.startRecording();
        testApp.stopRecording();
        HistoryPromptHandler question2 = historyList.getHistoryList().get(1);
        assertTrue(question2.getPrompt().
            equals(questionString2));
        assertTrue(question2.getAnswer().
            equals(answer_part + questionString2));

        // Add a third question
        // Test that the question and answer show up 
        // in history list as expected
        testApp.startRecording();
        testApp.stopRecording();
        HistoryPromptHandler question3 = historyList.getHistoryList().get(2);
        assertTrue(question3.getPrompt().
            equals(questionString3));
        assertTrue(question3.getAnswer().
            equals(answer_part + questionString3));


        // Add a fourth question
        // Test that the question and answer show up 
        // in history list as expected
        testApp.startRecording();
        testApp.stopRecording();
        HistoryPromptHandler question4 = historyList.getHistoryList().get(3);
        assertTrue(question4.getPrompt().
            equals(questionString4));
        assertTrue(question4.getAnswer().
            equals(answer_part + questionString4));



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
        String create_email = "Create email to Daniel. Let's meet at Geisel at 7 pm";
        String ceCommand = "Create email";
        String send_email = "Send email to dnt004 at ucsd.edu";
        String seCommand = "Send email";
        String delete = "Delete.";
        String dCommand = "Delete";
        String clear = "Clear.";
        String cCommand = "Clear";

        // Initialize result messages
        String emailSentString = "Email successfully sent.";

        // Test question command
        testApp.handleCommand(create_email, ceCommand);
        HistoryPromptHandler prompt1 = historyList.getHistoryList().get(4);
        assertEquals(prompt1.getPrompt(),
            (create_email));
        assertEquals(prompt1.getAnswer(),
            (answer_part + create_email + "Daniel Tran"));

        prompt1.select();

        testApp.handleCommand(send_email, seCommand);
        HistoryPromptHandler prompt2 = historyList.getHistoryList().get(5);
        assertTrue(prompt2.getAnswer().
            contains(emailSentString));
        
        assertTrue(historyList.getHistoryList().size() == 6);

        // Close test frame
        testApp.closeApp();

        // Open new app ("same computer", do not delete accounts.csv)
        AppHandler testApp2 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI2 = new AppGUI(testApp2);
        testApp2.createGUI(appGUI2);
        assertTrue(testApp2.canAutoLogin());

        HistoryListHandler historyList2 = testApp2.getHistoryList();
        assertEquals(historyList2.getHistoryList().size(), 6);

        // Close test frame
        testApp2.closeApp();

        // Open new app ("different computer", delete accounts.csv)
        String filename = "project/accounts.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File accountFile = new File(filename);
        accountFile.delete();


        // Open up new app in "different computer," re-login
        AppHandler testApp3 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI3 = new AppGUI(testApp3);
        testApp3.createGUI(appGUI3);
        assertTrue(!testApp3.canAutoLogin());
        testApp3.LogIn("username", "password");

        // Test persistence of added data across computers
        HistoryListHandler historyList3 = testApp3.getHistoryList();
        assertTrue(historyList3.getHistoryList().size() == 6);

        // Test delete command
        HistoryPromptHandler prompt6 = historyList3.getHistoryList().get(5);
        prompt6.select();
        testApp3.handleCommand(delete, dCommand);
        assertTrue(historyList3.getHistoryList().size() == 5);


        // Close test frame
        testApp3.closeApp();

        // Open new app ("new trusted computer", rewrite accounts.csv)
        AppHandler testApp4 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI4 = new AppGUI(testApp4);
        testApp4.createGUI(appGUI4);
        testApp4.LogIn("username", "password");

        // Re-establish autologin
        AutomaticLogInHandler alHandler2 = testApp4.getAutomaticLogInHandler();
        alHandler2.update("username", "password");
        assertTrue(testApp4.canAutoLogin());

        // Test persistence of deleted data
        HistoryListHandler historyList4 = testApp4.getHistoryList();
        assertTrue(historyList4.getHistoryList().size() == 5);

        // Test clear command
        testApp4.handleCommand(clear, cCommand);
        assertTrue(historyList4.getHistoryList().size() == 0);

        // Close test frame
        testApp4.closeApp();

        // Open new app ("same computer", do not delete accounts.csv)
        AppHandler testApp5 = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI5 = new AppGUI(testApp5);
        testApp5.createGUI(appGUI5);
        assertTrue(testApp5.canAutoLogin());

        // Test persistence of cleared history
        HistoryListHandler historyList5 = testApp5.getHistoryList();
        assertTrue(historyList5.getHistoryList().size() == 0);

        // Close test 
        testApp5.closeApp();
    }
}