package project.gui;
import project.*;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AppGUI extends JFrame {
    IAppHandler appHandler;
    private HistoryWindowGUI historyWindowGUI;
    private HistoryListGUI historyListGUI;
    private ChatWindowGUI chatWindowGUI;
    private LogInWindowGUI logInWindowGUI;
    private setupEmailGUI setupEmail;
    private AutomaticLogInGUI alGUI;
    private HeaderGUI header;
    private FooterGUI footer;
    private JButton startButton;
    private JButton stopRecordingButton;
    private JButton createAccount;
    private JButton logIn;
    private JButton clearAllButton;
    private JButton deleteSelected;
    private JButton acceptButton;
    private JButton denyButton;
    private JButton saveButton;
    private JButton cancelButton;
    

    // Constructor, initializes GUI objects
    public AppGUI(IAppHandler appHandler) {
        this.appHandler = appHandler;
        if (this.appHandler == null) {
            this.appHandler = new MockAppHandler();
        }
        if (appHandler.getHistoryList() != null) {
            this.historyListGUI = appHandler.getHistoryList().getHistoryListGUI();
        }
        else {
            this.historyListGUI = new HistoryListGUI(null);
        }
        

        
        this.historyWindowGUI = 
            new HistoryWindowGUI(historyListGUI);
        this.chatWindowGUI = new ChatWindowGUI();
        this.setupEmail = null;
        this.logInWindowGUI = null;
        this.alGUI = null;
        
        // Creates GUI components
        this.header = new HeaderGUI();
        this.footer = new FooterGUI();

        // Sets basic frame GUI properties
        this.revalidate();
        this.setSize(800, 600); // 400 width and 600 height
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
        this.setVisible(true); // Make visible

        // Adds initialized GUI components to the frame
        this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
        this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
        this.add(historyWindowGUI, BorderLayout.WEST); // Add history list in left of screen
        
        // Obtains buttons from GUI components for later use
        startButton = footer.getAskQuestion();
        stopRecordingButton = footer.getStopRecordingButton();
        //clearAllButton = historyWindowGUI.getHistoryHeader().getClearAll();
        //deleteSelected = historyWindowGUI.getHistoryHeader().getdeleteSelected();
        acceptButton = null;
        denyButton = null;
        createAccount = null;
        logIn = null;
        saveButton = null;
        cancelButton = null;


        // Adds listeners to the added buttons
        addListeners();
    }

    // Method to add listeners to core buttons, using helper handler methods
    public void addListeners() {
        startButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    StartButtonHandler();
                }
            }
        );
        stopRecordingButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    StopButtonHandler();
                }
            }
        );
        
    }

    // Method to handle saving the user's setup email information
    public void saveButtonHandler() {
        appHandler.getSetupEmailHandler().addEmailInfo();
        if (setupEmail != null) {
            setupEmail.dispose();
        }
        setupEmail = null;
        //revalidate();
        showApp();
        revalidate();
    }

    // Method to handle canceling the user's setup email information
    public void cancelButtonHandler() {
        //revalidate();
        if (setupEmail != null) {
            setupEmail.dispose();
        }
        setupEmail = null;
        showApp();
        revalidate();
    }

    // Method to handle starting the recording to ask a question
    public void StartButtonHandler() {
        // Start recording
        appHandler.startRecording();

        // Toggles visibility of the footer buttons and updates frame
        startButton.setVisible(false);
        stopRecordingButton.setVisible(true);
        clearChat();
        repaint();
    }

    // Method to handle stopping the recording and getting a question back
    public void StopButtonHandler() {
        // Stop recording
        appHandler.stopRecording();

        // Toggle footer button visibility
        stopRecordingButton.setVisible(false);
        startButton.setVisible(true);
    }

    // Method to display the given question and answer
    public void display(String question, String answer) {
        clearChat();
        revalidate();
        chatWindowGUI.displayPrompt(question, answer);
        revalidate();
    }

    // Method to clear the chat window
    public void clearChat() {
        chatWindowGUI.clearChatWindowGUI();
        revalidate();
    }

    // Method to make a given history question selectable
    public void makeSelectable(HistoryPromptGUI historyQuestionGUI) {
        // Add listener to new history question's select button
        JButton selectButton = historyQuestionGUI.getDone();
        selectButton.addActionListener(
            (ActionEvent e2) -> {
                SelectButtonHandler(historyQuestionGUI);
            }
        );
    }


    // Method to add listener to select button of given history question
    public void SelectButtonHandler(HistoryPromptGUI historyQuestionGUI) {

        // select History Question Handler and process according to app handler
        appHandler.selectPrompt(historyQuestionGUI.getHandler());

        revalidate(); // Updates the frame
    }

    // Method to clear all history from GUI
    public void clearAllHandler() {

        // Use handler to clear everything
        appHandler.clearAll();

        // Clears chat window and update frame
        clearChat();
        revalidate();
    }

    // Method to delete selected button, if any
    public void deleteSelectedHandler() {
        // Use app handler function to delete selected
        appHandler.deleteSelected();

        // Clear chat window and update frame
        clearChat();
        revalidate();
    }

    // Method to handle starting the login page
    public void beginLogIn() {

        boolean canAutoLogin = appHandler.canAutoLogin();

        if (canAutoLogin) {
            appHandler.autoLogin();
            showApp();
        }
        else {
            // this.add(logInWindowGUI, BorderLayout.CENTER);
            // chatWindowGUI.setVisible(false);
            if (appHandler.getLogInWindowHandler() != null) {
                this.logInWindowGUI = appHandler.getLogInWindowHandler().getLogInWindowGUI();
            }
            else {
                this.logInWindowGUI = new LogInWindowGUI(null);
            }
            logInWindowGUI.setVisible(true);
                
            createAccount = logInWindowGUI.getCreateAccount();
            logIn = logInWindowGUI.getlogIn();

            createAccount.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        createAccountHandler();
                    }
                }
            );
            logIn.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        logInHandler();
                    }
                }
            );


            footer.setVisible(false);
            historyWindowGUI.setVisible(false);
            // alGUI.setVisible(false);
            chatWindowGUI.setVisible(false);
        }
    }

    // Method to handle setting up email
    public void beginSetupEmail() {
        // this.add(setupEmail, BorderLayout.CENTER);
        if (setupEmail == null) {
            if (appHandler.getSetupEmailHandler() != null) {
                this.setupEmail = appHandler.getSetupEmailHandler().getsetupEmailWindowGUI();
            }
            else {
                this.setupEmail = new setupEmailGUI(null);
            }
        }

        saveButton = this.setupEmail.getSaveButton();
        cancelButton = this.setupEmail.getCancelButton();
        
        saveButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    saveButtonHandler();
                }
            }
        );
        cancelButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    cancelButtonHandler();
                }
            }
        );

        setupEmail.setVisible(true);
        setupEmail.updateFields();
        setupEmail.repaint();
        footer.setVisible(false);
        historyWindowGUI.setVisible(false);
        chatWindowGUI.setVisible(false);
    }

    // Method to handle creating account
    public void createAccountHandler() {
        String username = logInWindowGUI.getUserName();
        String password = logInWindowGUI.getPassword();
        String password2 = logInWindowGUI.getVerifyPassword();
        boolean valid = appHandler.getLogInWindowHandler().
            createAccount(username, password, password2);
        if (valid) {
            revalidate();
            logInHandler();
            revalidate();
        }
        else {
            if (!appHandler.getLogInWindowHandler().
                    verifyUsername(username, password)) {
                JOptionPane.showMessageDialog(null, 
                    "Accounts already existed");
            }
            if (!appHandler.getLogInWindowHandler().
                    checkPassword(password, password2)) {
                JOptionPane.showMessageDialog(null, 
                    "Passwords don't match");
            }
        }
    }

    // Method to handle logging in
    public void logInHandler() {
        String username = logInWindowGUI.getUserName();
        String password = logInWindowGUI.getPassword();
        boolean verify = appHandler.LogIn(username, password);
        if (verify) {
            createAccount.setVisible(false);
            logIn.setVisible(false);
            logInWindowGUI.dispose();

            askAutoLoginHandler(username, password);
        }
        else {
            JOptionPane.showMessageDialog(null, 
                "Username doesn't match password");
        }
        revalidate();
    }

    // Method to display automatic login popup
    public void askAutoLoginHandler(String username, String password) {
        
        this.alGUI = new AutomaticLogInGUI();
        this.alGUI.register(appHandler.getAutomaticLogInHandler());
        this.alGUI.setUsername(username);
        this.alGUI.setPassword(password);
        this.alGUI.setVisible(true);
        this.acceptButton = alGUI.getAcceptButton();
        this.denyButton = alGUI.getDenyButton();
        acceptButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    acceptButtonHandler();
                }
            }
        );
        denyButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    denyButtonHandler();
                }
            }
        );
        revalidate();
    }

    // Method to handle accepting automatic login
    public void acceptButtonHandler() {
        this.alGUI.notifyObservers();
        acceptButton.setVisible(false);
        denyButton.setVisible(false);
        alGUI.dispose();
        showApp();
    }

    // Method to handle denying automatic login
    public void denyButtonHandler() {
        acceptButton.setVisible(false);
        denyButton.setVisible(false);
        alGUI.dispose();
        showApp();
    }

    // Helper method to show the actual app window after logging in
    private void showApp() {
        historyWindowGUI.setVisible(true);
        footer.setVisible(true);
        // Add chat list in middle of footer and title
        this.add(chatWindowGUI, BorderLayout.CENTER); 
        chatWindowGUI.setVisible(true);
    }

    // Method to get HeaderGUI object
    public HeaderGUI getHeader() {
        return header;
    }

    // Method to get FooterGUI object
    public FooterGUI getFooter() {
        return footer;
    }

    // Method to get ChatListGUI object
    public ChatWindowGUI getChatWindow() {
        return chatWindowGUI;
    }

    // Method to get HistoryListGUI object
    public HistoryListGUI getHistoryList() {
        return historyListGUI;
    }

    // Method to get log-in window GUI object
    public LogInWindowGUI getLoginWindow() {
        return logInWindowGUI;
    }

    // Method to get setup email GUI object
    public setupEmailGUI getSetupEmailGUI() {
        return setupEmail;
    }

    // Method to get clear button
    public JButton getClearButton() {
        return clearAllButton;
    }

    // Method to get delete button
    public JButton getDeleteButton() {
        return deleteSelected;
    }

    // Method to get "Start" button
    public JButton getStartButton() {
        return startButton;
    }

    // Method to get "Stop Recording" button
    public JButton getStopButton() {
        return stopRecordingButton;
    }

    // Method to get visibility of "Stop Recording" button
    public boolean getStopButtonVisibility() {
        return stopRecordingButton.isVisible();
    }

    // Method to get visibility of "Ask a Question" button
    public boolean getStartButtonVisibility() {
        return startButton.isVisible();
    }

    // Method to close frame
    public void closeFrame() {
        appHandler.stopServer();
        if (this.setupEmail != null) {
            this.setupEmail.dispose();
        }
        
        if (this.logInWindowGUI != null) {
            this.logInWindowGUI.dispose();
        }
        
        if (this.alGUI != null) {
            this.alGUI.dispose();
        }
        this.dispose();
    }
}
