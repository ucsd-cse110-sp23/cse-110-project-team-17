package project;

import project.chat_gpt.*;
import project.gui.*;

public interface IAppHandler {
    
    // Method to create GUI aspect of app
    public void createGUI(AppGUI appGUI);

    // Method to return HistoryListHandler element
    public HistoryListHandler getHistoryList();

    // Method to start recording to get question
    public void startRecording();

    // Method to stop recording and receive answer
    public void stopRecording();

    // Method to handle selecting a history button
    public void selectQuestion(HistoryQuestionHandler historyQuestionHandler);

    // Method to clear all history from list
    public void clearAll();

    // Method to delete selected question, if any
    public void deleteSelected();

    // Method to add listeners to select buttons from old history questions
    public void oldHistoryHandler();

    // Method to display a propmt and answer in chat window
    public void display(String question, String answer);

    // Method to clear chat window
    public void clearChat();

    // Method to get ChatGPT handler
    public IChatGPT getChatGPT();

    // Method to close server
    public void stopServer();

    // Method to close app
    public void closeApp();

    // Method to get associated GUI object
    public AppGUI getAppGUI();

    // Method to get HTTPRequestMaker object
    public HTTPRequestMaker getRequestMaker();

    // Method to get LogInWindow 
    public LogInWindowHandler getLogInWindowHandler();
}
