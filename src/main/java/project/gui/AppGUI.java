package project.gui;

import project.*;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class AppGUI extends JFrame {
    IAppHandler appHandler;
    private HistoryWindowGUI historyWindowGUI;
    private HistoryListGUI historyListGUI;
    private ChatWindowGUI chatWindowGUI;
    private HeaderGUI header;
    private FooterGUI footer;
    private JButton askQuestion;
    private JButton stopRecordingButton;
    private JButton clearAllButton;
    private JButton deleteSelected;
    

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
        this.add(chatWindowGUI, BorderLayout.CENTER); // Add chat list in middle of footer and title

        // Obtains buttons from GUI components for later use
        askQuestion = footer.getAskQuestion();
        stopRecordingButton = footer.getStopRecordingButton();
        clearAllButton = historyWindowGUI.getHistoryHeader().getClearAll();
        deleteSelected = historyWindowGUI.getHistoryHeader().getdeleteSelected();


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
        clearAllButton.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    clearAllHandler();
                }
            }
        );
        deleteSelected.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    deleteSelectedHandler();
                }
            }
        );
    }

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

    // Method to get "Ask a Question" button
    public JButton getAskButton() {
        return askQuestion;
    }

    // Method to get "Stop Recording" button
    public JButton getStopButton() {
        return stopRecordingButton;
    }

    // Method to get "Clear All" button
    public JButton getClearButton() {
        return clearAllButton;
    }

    // Method to get "Delete Selected" button
    public JButton getDeleteButton() {
        return deleteSelected;
    }

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
