package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.security.auth.PrincipalComparator;

import javax.swing.JPanel;

import project.*;
import project.handler.*;

public class HistoryQuestionGUI extends JPanel {

    JLabel index;
    JTextField question;
    JTextField answer;
    String question_str;
    String index_str;
    JButton selectButton;
    HistoryQuestionHandler historyQuestionHandler;

    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);

    // Constructor, initializes label and question
    public HistoryQuestionGUI(HistoryQuestionHandler historyQuestionHandler) {
        this.setMinimumSize(new Dimension(400, 160)); // set size of task
        this.setBackground(gray); // set background color of task
        this.setLayout(new FlowLayout()); // set layout of task
        this.historyQuestionHandler = historyQuestionHandler;

        this.index_str = historyQuestionHandler.getIndex();
        this.question_str = historyQuestionHandler.getQuestion();

        // Creates index JLabel
        index = new JLabel(""); // create index label
        index.setPreferredSize(new Dimension(20, 160)); // set size of index label
        index.setHorizontalAlignment(JLabel.LEFT); // set alignment of index label
        index.setText(index_str);
        this.add(index); // add index label to task

        // Creates question JTextField
        question = new JTextField("Prompt: "); // create task name text field
        question.setPreferredSize(new Dimension(300, 160));
        question.setBackground(gray); // set background color of text field
        question.setEditable(false);
        question.setText("Prompt: " + question_str);
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
    
    public HistoryQuestionHandler getHandler() {
        return historyQuestionHandler;
    }
}
