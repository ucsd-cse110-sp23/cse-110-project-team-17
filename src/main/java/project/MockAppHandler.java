package project;

import project.chat_gpt.*;
import project.gui.*;
import project.handler.*;

public class MockAppHandler implements IAppHandler {
    AppGUI appGUI;


    // Empty constructor
    public MockAppHandler() {}

    // Method to create GUI aspect of app
    public void createGUI(AppGUI appGUI) {
        this.appGUI = appGUI;
    }

    // Method to return HistoryListHandler element (null)
    public HistoryListHandler getHistoryList() {
        return null;
    }

    // Dummy method to substitute for asking a question
    public void startRecording() {}

    // Method to display error when trying to receive answer to question
    public void stopRecording() {
        String error_string = "No server found.";
        display(error_string, error_string);
    }

    // Dummy method to substitute for selecting a question
    public void selectPrompt(HistoryPromptHandler historyQuestionHandler) {}

    // Dummy method to substitute for clearing all history
    public void clearAll() {}

    // Dummy method to substitute for deleting a selected question
    public void deleteSelected() {}

    // Dummy method to substitute for adding buttons to old questions
    public void oldHistoryHandler() {}

    // Method to display a propmt and answer in chat window
    public void display(String question, String answer) {
        appGUI.display(question, answer);
    }

    // Dummy method to substitute for logging in
    public boolean LogIn(String username, String password) {
        return false;
    }

    // Dummy method to substitute for automatically logging in
    public void autoLogin() {
        return;
    }

    // Dummy method to substitute for determining if you can autologin
    public boolean canAutoLogin() {
        return false;
    }

    // Method to get Automatic Login Handler object (null)
    public AutomaticLogInHandler getAutomaticLogInHandler() {
        return null;
    }

    // Method to clear chat window
    public void clearChat() {
        appGUI.clearChat();
    }

    // Method to get ChatGPT handler (null)
    public IChatGPT getChatGPT() {
        return null;
    }

    // Dummy method to substitute for closing server
    public void stopServer() {}

    // Method to close app
    public void closeApp() {
        appGUI.closeFrame();
    }

    // Method to get associated GUI object
    public AppGUI getAppGUI() {
        return this.appGUI;
    }

    // Method to get HTTPRequestMaker object (null)
    public HTTPRequestMaker getRequestMaker() {
        return null;
    }

    public LogInWindowHandler getLogInWindowHandler() {
        return null;
    }

    public setupEmailHandler getSetupEmailHandler() {
        return null;
    }

    public String[] sampleEmailInfo() {
        String[] test = {"firstname", "lastname", "username", "emailaddress", "SMTPHost", "TLSPort", "emailpassword"};
        return test;
    }
}