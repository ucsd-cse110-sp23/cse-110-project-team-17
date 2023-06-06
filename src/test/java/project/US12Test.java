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

public class US12Test {

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
    void testCreateEmailCommand() {
        QuestionHandler questionHandler = new QuestionHandler();
        String prompt = "Create email to Jill. Let's meet at Geisel at 7 pm";
        String command = "Create email";
        assertTrue(questionHandler.getCommand(prompt).equals(command));
    }

    @Test
    void testAppCreateEmail() {
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password");
        testApp.LogIn("username", "password");

        // Initialize prompts and commands
        String answer_part = "Mock answer to the following prompt: ";
        String create_email = "Create email to Jill. Let's meet at Geisel at 7 pm";
        String ceCommand = "Create email";

        // Test question command
        testApp.handleCommand(create_email, ceCommand);
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryQuestionHandler prompt1 = historyList.getHistoryList().get(0);
        assertTrue(prompt1.getQuestion().
            equals(create_email));
        assertTrue(prompt1.getAnswer().
            equals(answer_part + create_email));

        // Close test frame
        testApp.closeApp();
    }
}