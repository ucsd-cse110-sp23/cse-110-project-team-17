package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.JPanel;

import project.handler.*;

public class HistoryPromptGUI extends JPanel {

    JLabel index;
    JTextField question;
    JTextField answer;
    String prompt_str;
    String index_str;
    JButton selectButton;
    HistoryPromptHandler historyPromptHandler;

    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);

    // Constructor, initializes label and question
    public HistoryPromptGUI(HistoryPromptHandler historyPromptHandler) {
        this.setMinimumSize(new Dimension(400, 160)); // set size of task
        this.setBackground(gray); // set background color of task
        this.setLayout(new FlowLayout()); // set layout of task
        this.historyPromptHandler = historyPromptHandler;

        this.prompt_str = historyPromptHandler.getPrompt();

        String cmd = "";
        String index_cmd = "";

        
        if (prompt_str.length() >= 5) {
            cmd = prompt_str.substring(0, 5);
            if (cmd.toUpperCase().equals("CLEAR")) {
                index_cmd = "Clear";
            }
        }
        if (prompt_str.length() >= 6) {
            cmd = prompt_str.substring(0, 6);
            if (cmd.toUpperCase().equals("DELETE")) {
                index_cmd = "Delete";
            }
        }
        if (prompt_str.length() >= 8) {
            cmd = prompt_str.substring(0, 8);
            index_cmd = "This is a question";
            if (cmd.toUpperCase().equals("QUESTION")) {
                index_cmd = "Question";
            }
        }
        if (prompt_str.length() >= 10) {
            cmd = prompt_str.substring(0, 10);
            if (cmd.toUpperCase().equals("SEND EMAIL")) {
                index_cmd = "Send email";
            }
        }
        if (prompt_str.length() >= 12) {
            cmd = prompt_str.substring(0, 12);
            if (cmd.toUpperCase().equals("SET UP EMAIL")) {
                index_cmd = "Setup email";
            }
            else if (cmd.toUpperCase().equals("CREATE EMAIL")) {
                index_cmd = "Create email";
            }
        }
        if (cmd.equals("")) {
            index_cmd = "invalid";
        }

        // Creates index JLabel
        index = new JLabel(""); // create index label
        index.setPreferredSize(new Dimension(80, 160)); // set size of index label
        index.setHorizontalAlignment(JLabel.LEFT); // set alignment of index label
        index.setText(index_cmd);
        this.add(index); // add index label to task

        // Creates question JTextField
        question = new JTextField(""); // create task name text field
        question.setPreferredSize(new Dimension(300, 160));
        question.setBackground(gray); // set background color of text field
        question.setEditable(false);
        question.setText(prompt_str);
        this.add(question); // Adds question text field to HistoryQuestion
    
        // Creates Select JButton
        selectButton = new JButton("Select");
        selectButton.setPreferredSize(new Dimension(80, 20));
        selectButton.setBorder(BorderFactory.createEmptyBorder());
        selectButton.setFocusPainted(false);
        this.add(selectButton); // Adds select button to HistoryQuestion
    }

    // Method to return Select button
    public JButton getDone() {
        return selectButton;
    }
    
    // Method to toggle HistoryQuestion's selected state in GUI
    public void toggleSelectedGUI(boolean selected) {
        // Deselects if selected
        if (selected) {
            deselectGUI();
        }
        // Selects if not selected
        else {
            selectGUI();
        }
        revalidate(); // update component
    }

    // Method to deselect HistoryQuestionGUI
    public void deselectGUI() {
        this.setBackground(gray);
        question.setBackground(gray);
        revalidate(); // update component
    }

    // Method to select HistoryQuestionGUI
    public void selectGUI() {
        this.setBackground(green);
        question.setBackground(green);
        revalidate(); // update component
    }
    
    // Method to get associated history question handler
    public HistoryPromptHandler getHandler() {
        return historyPromptHandler;
    }
}
