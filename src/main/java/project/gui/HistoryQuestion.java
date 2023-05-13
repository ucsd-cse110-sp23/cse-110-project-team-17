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
    JButton selectButton; //remove or change to answer cutoff
  
    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);
  
    private boolean selected; //change to be if selected?
  
    public HistoryQuestion(String index_str, String question_text, String answer_text) {
      this.setMinimumSize(new Dimension(400, 160)); // set size of task
      this.setBackground(gray); // set background color of task
      this.setLayout(new FlowLayout()); // set layout of task

      this.index_str = index_str;
      this.question_text = question_text;
      this.answer_text = answer_text;
  
      selected = false;
  
      index = new JLabel(""); // create index label
      index.setPreferredSize(new Dimension(20, 160)); // set size of index label
      index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
      this.add(index); // add index label to task
  
      question = new JTextField("Question: "); // create task name text field
      question.setPreferredSize(new Dimension(300, 160));
      //question.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
      question.setBackground(gray); // set background color of text field
      question.setEditable(false);
      question.setText("Question: " + question_text);
  
      this.add(question);
  
      selectButton = new JButton("Select");
      selectButton.setPreferredSize(new Dimension(80, 20));
      selectButton.setBorder(BorderFactory.createEmptyBorder());
      selectButton.setFocusPainted(false);
  
      this.add(selectButton);
    }

    public void setIndex(int num) {
      index_str = Integer.toString(num);
      this.index.setText(index_str);
    }
  
    public JButton getDone() {
      return selectButton;
    }
  
    public boolean getState() {
      return selected;
    }
  
    public void changeState() {
      if (selected) {
        this.setBackground(gray);
        question.setBackground(gray);
        selected = false;
      }
      else {
        this.setBackground(green);
        question.setBackground(green);
        selected = true;
      }
      revalidate();
    }
  
    public void insertQuestion(String questionString) {
        question.setText(question.getText() + questionString);
        question.setCaretPosition(0);
        question_text = questionString;
    }
  
    public void insertAnswer(String ansString) {
      //question.setText(answer.getText() + ansString);
      answer_text = ansString;
    }
  
    public String getQuestionText() {
      return question_text;
    }
  
    public String getAnswerText() {
      return answer_text;
    }

    public String getIndex() {
      return index_str;
    }
  
}