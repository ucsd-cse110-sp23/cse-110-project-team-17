package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project.gui.*;
import project.handler.*;

import java.io.File;


public class US14Test {

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
    void testMockServer() {
        IAppHandler mockApp = new MockAppHandler();
        AppGUI appGUI = new AppGUI(mockApp);
        mockApp.createGUI(appGUI);


        mockApp.startRecording();
        mockApp.stopRecording();

        ChatWindowGUI chatWindowGUI = appGUI.getChatWindow();
        ChatBoxGUI questionBox = 
            (ChatBoxGUI) chatWindowGUI.getComponents()[0];
        ChatBoxGUI answerBox = 
            (ChatBoxGUI) chatWindowGUI.getComponents()[1];

        assertEquals(questionBox.getDialogueText(),
            "No server found.");
        assertEquals(answerBox.getDialogueText(),
            "No server found.");
    }
}