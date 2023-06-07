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

public class US10Test {

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
    void testQuestionCommand() {
        PromptHandler questionHandler = new PromptHandler();
        String prompt = "Question. What is 2+2?";
        String command = "Question";
        assertTrue(questionHandler.getCommand(prompt).equals(command));
    }

    @Test
    void testDeleteCommand() {
        PromptHandler questionHandler = new PromptHandler();
        String prompt = "Delete.";
        String command = "Delete";
        assertTrue(questionHandler.getCommand(prompt).equals(command));
    }

    @Test
    void testClearCommand() {
        PromptHandler questionHandler = new PromptHandler();
        String prompt = "Clear.";
        String command = "Clear";
        assertTrue(questionHandler.getCommand(prompt).equals(command));
    }

    @Test
    void testInvalidCommand() {
        PromptHandler questionHandler = new PromptHandler();
        String prompt = "Not a command.";
        String command = "invalid";
        assertTrue(questionHandler.getCommand(prompt).equals(command));
    }

    @Test
    void testAllCommands() {
        IPromptHandler qHandler = new MockPromptHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        AppGUI appGUI = new AppGUI(testApp);
        testApp.createGUI(appGUI);
        LogInWindowHandler logInHandler = testApp.getLogInWindowHandler();
        logInHandler.createAccount("username", "password", "password");
        testApp.LogIn("username", "password");

        // Initialize prompts and commands
        String answer_part = "Mock answer to the following prompt: ";
        String question = "Question. What is 2+2?";
        String qCommand = "Question";

        String delete = "Delete.";
        String dCommand = "Delete";

        String clear = "Clear.";
        String cCommand = "Clear";

        String invalid = "Not a command.";
        String iCommand = "invalid";

        // Test question command
        testApp.handleCommand(question, qCommand);
        HistoryListHandler historyList = testApp.getHistoryList();
        HistoryPromptHandler question1 = historyList.getHistoryList().get(0);
        assertTrue(question1.getPrompt().
            equals(question));
        assertTrue(question1.getAnswer().
            equals(answer_part + question));

        // Test delete command
        testApp.handleCommand(delete, dCommand);
        assertTrue(historyList.getHistoryList().size() == 0);

        // Re-add question and test invalid and clear commands
        testApp.handleCommand(question, qCommand);
        question1 = historyList.getHistoryList().get(0);
        assertTrue(question1.getPrompt().
            equals(question));
        assertTrue(question1.getAnswer().
            equals(answer_part + question));
        testApp.handleCommand(invalid, iCommand);
        testApp.handleCommand(clear, cCommand);
        assertTrue(historyList.getHistoryList().size() == 0);

        // Close test frame
        testApp.closeApp();
    }
}