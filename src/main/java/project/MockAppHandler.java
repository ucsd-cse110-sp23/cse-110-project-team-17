package project;

import project.chat_gpt.*;
import project.gui.*;

public class MockAppHandler implements IAppHandler {
    AppGUI appGUI;


    // Empty constructor
    MockAppHandler() {}

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
    public void selectQuestion(HistoryQuestionHandler historyQuestionHandler) {}

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
}