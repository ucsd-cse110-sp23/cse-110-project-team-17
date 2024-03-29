package project;

import project.chat_gpt.*;
import project.gui.*;
import project.handler.*;

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
    public void selectPrompt(HistoryPromptHandler historyQuestionHandler);

    // Method to clear all history from list
    public void clearAll();

    // Method to delete selected question, if any
    public void deleteSelected();

    // Method to add listeners to select buttons from old history questions
    public void oldHistoryHandler();

    // Method to display a prompt and answer in chat window
    public void display(String question, String answer);

    // Method to log in, returning true if it works and false if it doesn't
    public boolean LogIn(String username, String password);

    // Method to return if you can autologin on this computer
    public boolean canAutoLogin();

    // Method to return automatic login handler
    public AutomaticLogInHandler getAutomaticLogInHandler();

    // Method to check whether or not we can automatically login
    public void autoLogin();

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

    // Method to get EmailSetUpWindow
    public setupEmailHandler getSetupEmailHandler();
}
