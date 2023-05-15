package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;


public class HistoryQuestion extends JPanel {

    JLabel index;
    JTextField question;
    JTextField answer;
    String question_text;
    String answer_text;
    String index_str;
    JButton selectButton;
  
    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);
  
    private boolean selected;
  
    // HistoryQuestion Constructor, uses inputs to set identifying fields of 
    // HistoryQuestion (i.e., label, question text, answer text)
    public HistoryQuestion(String index_str, String question_text, String answer_text) {
      this.setMinimumSize(new Dimension(400, 160)); // set size of task
      this.setBackground(gray); // set background color of task
      this.setLayout(new FlowLayout()); // set layout of task

      this.index_str = index_str;
      this.question_text = question_text;
      this.answer_text = answer_text;
  
      selected = false;
  
      // Creates index JLabel
      index = new JLabel(""); // create index label
      index.setPreferredSize(new Dimension(20, 160)); // set size of index label
      index.setHorizontalAlignment(JLabel.LEFT); // set alignment of index label
      this.add(index); // add index label to task
  
      // Creates question JTextField
      question = new JTextField("Question: "); // create task name text field
      question.setPreferredSize(new Dimension(300, 160));
      question.setBackground(gray); // set background color of text field
      question.setEditable(false);
      question.setText("Question: " + question_text);
      this.add(question); // Adds question text field to HistoryQuestion
  
      // Creates Select JButton
      selectButton = new JButton("Select");
      selectButton.setPreferredSize(new Dimension(80, 20));
      selectButton.setBorder(BorderFactory.createEmptyBorder());
      selectButton.setFocusPainted(false);
      this.add(selectButton); // Adds select button to HistoryQuestion
    }

    // Method to set index based on input number
    public void setIndex(int num) {
      index_str = Integer.toString(num);
      this.index.setText(index_str);
    }
  
    // Method to return Select button
    public JButton getDone() {
      return selectButton;
    }
  
    // Method to check if HistoryQuestion is selected
    public boolean getState() {
      return selected;
    }
  
    // Method to toggle HistoryQuestion's selected state
    public void changeState() {
      // Deselects if selected
      if (selected) {
        this.setBackground(gray);
        question.setBackground(gray);
        selected = false;
      }
      // Selects if not selected
      else {
        this.setBackground(green);
        question.setBackground(green);
        selected = true;
      }
      revalidate(); // update component
    }
  
    // Method to return the question text
    public String getQuestionText() {
      return question_text;
    }
  
    // Method to return the answer text
    public String getAnswerText() {
      return answer_text;
    }

    // Method to return the index as a String
    public String getIndex() {
      return index_str;
    }
  
}