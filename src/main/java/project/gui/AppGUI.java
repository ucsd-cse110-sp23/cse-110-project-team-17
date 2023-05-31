package project.gui;
import project.IAppHandler;

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
    private AutomaticLogInGUI alGUI;
    private HeaderGUI header;
    private FooterGUI footer;
    private JButton askQuestion;
    private JButton stopRecordingButton;
    private JButton createAccount;
    private JButton logIn;
    private JButton clearAllButton;
    private JButton deleteSelected;
    private JButton acceptButton;
    private JButton denyButton;
    

    // Constructor, initializes GUI objects
    public AppGUI(IAppHandler appHandler) {
        this.appHandler = appHandler;
        if (appHandler.getHistoryList() != null) {
            this.historyListGUI = appHandler.getHistoryList().getHistoryListGUI();
        }
        else {
            this.historyListGUI = new HistoryListGUI(null);
        }
        this.historyWindowGUI = 
            new HistoryWindowGUI(historyListGUI);
        this.chatWindowGUI = new ChatWindowGUI();
        this.logInWindowGUI = appHandler.getLogInWindowHandler().getLogInWindowGUI();
        this.alGUI = new AutomaticLogInGUI();
        this.alGUI.register(appHandler.getAutomaticLogInHandler());
        
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

        // Start login process
        beginLogIn();
        
        // Obtains buttons from GUI components for later use
        askQuestion = footer.getAskQuestion();
        stopRecordingButton = footer.getStopRecordingButton();
        //clearAllButton = historyWindowGUI.getHistoryHeader().getClearAll();
        //deleteSelected = historyWindowGUI.getHistoryHeader().getdeleteSelected();
        createAccount = logInWindowGUI.getCreateAccount();
        logIn = logInWindowGUI.getlogIn();
        acceptButton = this.alGUI.getAcceptButton();
        denyButton = this.alGUI.getDenyButton();


        // Adds listeners to the added buttons
        addListeners();
    }

    // Method to add listeners to core buttons, using helper handler methods
    public void addListeners() {
        askQuestion.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    QuestionButtonHandler();
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
        /* 
        clearAllButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    clearAllHandler();
                }
            }
        );
        */
        /* 
        deleteSelected.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    deleteSelectedHandler();
                }
            }
        );
        */
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
        
    }


    // private static void createUI(final JFrame frame){  
    //     JPanel panel = new JPanel();
    //     LayoutManager layout = new FlowLayout();  
    //     panel.setLayout(layout);       
    //     JButton button = new JButton("Click Me!");
    //     button.addActionListener(new ActionListener() {
    //        @Override
    //        public void actionPerformed(ActionEvent e) {
    //           JOptionPane.showMessageDialog(frame, "Welcome to Swing!");
    //        }
    //     });
  
    //     panel.add(button);
    //     frame.getContentPane().add(panel, BorderLayout.CENTER);    
    //  }  


    // Method to handle starting the recording to ask a question
    public void QuestionButtonHandler() {
        // Start recording
        appHandler.startRecording();

        // Toggles visibility of the footer buttons and updates frame
        askQuestion.setVisible(false);
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
        askQuestion.setVisible(true);
    }

    // Method to display the given question and answer
    public void display(String question, String answer) {
        clearChat();
        revalidate();
        chatWindowGUI.displayQuestion(question, answer);
        revalidate();
    }

    // Method to clear the chat window
    public void clearChat() {
        chatWindowGUI.clearChatWindowGUI();
        revalidate();
    }

    // Method to make a given history question selectable
    public void makeSelectable(HistoryQuestionGUI historyQuestionGUI) {
        // Add listener to new history question's select button
        JButton selectButton = historyQuestionGUI.getDone();
        selectButton.addActionListener(
            (ActionEvent e2) -> {
                SelectButtonHandler(historyQuestionGUI);
            }
        );
    }


    // Method to add listener to select button of given history question
    public void SelectButtonHandler(HistoryQuestionGUI historyQuestionGUI) {

        // select History Question Handler and process according to app handler
        appHandler.selectQuestion(historyQuestionGUI.getHandler());

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

    public void beginLogIn() {

        boolean autoLogin = appHandler.autoLogin();

        if (autoLogin) {
            showApp();
        }
        else {
            this.add(logInWindowGUI, BorderLayout.CENTER);
            // chatWindowGUI.setVisible(false);
            footer.setVisible(false);
            historyWindowGUI.setVisible(false);
            alGUI.setVisible(false);
            chatWindowGUI.setVisible(false);
        }
    }

    // Method to handle creating account
    public void createAccountHandler() {
        String username = logInWindowGUI.getUserName();
        String password = logInWindowGUI.getPassword();
        boolean valid = appHandler.getLogInWindowHandler().createAccount(username, password);
        if (valid) {
            revalidate();
            logInHandler();
            revalidate();
        }
        else {
            JOptionPane.showMessageDialog(null, "Accounts already existed");
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
            logInWindowGUI.setVisible(false);
            remove(logInWindowGUI);

            askAutoLoginHandler(username, password);
        }
        revalidate();
    }

    // Method to display automatic login popup
    public void askAutoLoginHandler(String username, String password) {
        this.alGUI.setUsername(username);
        this.alGUI.setPassword(password);
        this.add(this.alGUI, BorderLayout.CENTER);
        this.alGUI.setVisible(true);
        revalidate();
    }

    // Method to handle accepting automatic login
    public void acceptButtonHandler() {
        this.alGUI.notifyObservers();
        acceptButton.setVisible(false);
        denyButton.setVisible(false);
        remove(alGUI);
        showApp();
    }

    // Method to handle denying automatic login
    public void denyButtonHandler() {
        remove(alGUI);
        acceptButton.setVisible(false);
        denyButton.setVisible(false);
        showApp();
    }

    // Helper method to show the actual app window after logging in
    private void showApp() {
        historyWindowGUI.setVisible(true);
        footer.setVisible(true);
        this.add(chatWindowGUI, BorderLayout.CENTER); // Add chat list in middle of footer and title
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

    public LogInWindowGUI getLoginWindow() {
        return logInWindowGUI;
    }

    public JButton getClearButton() {
        return clearAllButton;
    }

    public JButton getDeleteButton() {
        return deleteSelected;
    }

    // Method to get "Ask a Question" button
    public JButton getAskButton() {
        return askQuestion;
    }

    // Method to get "Stop Recording" button
    public JButton getStopButton() {
        return stopRecordingButton;
    }

    // Method to get "Clear All" button
    // public JButton getClearButton() {
    //     return createAccount;
    // }

    // Method to get "Delete Selected" button
    // public JButton getDeleteButton() {
    //     return logIn;
    // }

    // Method to get visibility of "Stop Recording" button
    public boolean getStopButtonVisibility() {
        return stopRecordingButton.isVisible();
    }

    // Method to get visibility of "Ask a Question" button
    public boolean getAskButtonVisibility() {
        return askQuestion.isVisible();
    }

    // Method to close frame
    public void closeFrame() {
        appHandler.stopServer();
        this.dispose();
    }
}
