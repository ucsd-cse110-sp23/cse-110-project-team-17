package project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;
import project.audio_handler.*;

import java.io.*;

public class US4Test {

    // Delete history.csv file (fresh start)
    @BeforeEach
    void cleanHistory() {
        String filename = "project/history.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File historyFile = new File(filename);
        historyFile.delete();
    }

    // Delete history.csv file (once at end of all tests)
    @AfterAll
    static void cleanUp() {
        String filename = "project/history.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
        File historyFile = new File(filename);
        historyFile.delete();
    }

    // Test that Clear All button properly clears everything
    @Test
    void testClearAllButtons() throws IOException {
        // Create mock handlers and appframe
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        AppHandler testApp = new AppHandler(qHandler, chatGPT, audioHandler);
        testApp.createGUI();
        AppGUI appGUI = testApp.getAppGUI();

        // Verify that all relevant HistoryList components (and ChatList) are
        // wiped
        ChatWindowGUI chatWindow = appGUI.getChatWindow();
        HistoryListHandler historyList = testApp.getHistoryList();
        appGUI.QuestionButtonHandler();
        appGUI.StopButtonHandler();
        assertTrue(!historyList.isEmpty());
        assertTrue(!(chatWindow.getComponents().length == 0));
        appGUI.clearAllHandler();
        assertTrue(historyList.isEmpty());
        assertTrue(0 == historyList.getHistoryList().size());
        assertTrue(chatWindow.getComponents().length == 0);


        // Close test frame
        testApp.closeApp();
    }
}
