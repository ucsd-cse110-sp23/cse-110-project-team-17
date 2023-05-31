package project.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import project.handler.*;

public class HistoryListGUI extends JPanel {

    Color backgroundColor = new Color(240, 248, 255);
    Color gray = new Color(218, 229, 234);
    HistoryListHandler historyListHandler;

    public HistoryListGUI(HistoryListHandler historyListHandler) {
        this.historyListHandler = historyListHandler;
    }

    // Method that adds a HistoryQuestionGUI
    public void addHistoryQuestionGUI(HistoryQuestionGUI historyQuestionGUI) {
        this.add(historyQuestionGUI);
        revalidate();
    }
  
    // Method to set default text in HistoryList for display
    public void setDefaultGUI() {

      // Sets format
      BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      this.setLayout(layout);
      setAlignmentY(TOP_ALIGNMENT);
      this.add(Box.createVerticalGlue());

      if (!(historyListHandler.isEmpty())) {
        historyListHandler.toggleEmpty();
      }
      
      // Creates default JTextArea
      JTextArea defaultArea = new JTextArea("No history to show. Ask a question!");
      defaultArea.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
      defaultArea.setBackground(gray); // set background color of text 
      defaultArea.setEditable(false);
      defaultArea.setFont(new Font("Serif", Font.ITALIC, 16));
      defaultArea.setLineWrap(true);
      defaultArea.setWrapStyleWord(true);
      defaultArea.setPreferredSize(new Dimension(400, 160));
      add(defaultArea); // adds default text area to History List
      revalidate();
    }
  
    // Method to remove default text area
    public void removeDefault() {
      for (Component c : getComponents()) {

        if (!(c instanceof HistoryQuestionGUI)) {
          remove(c);
        }
      }
      revalidate();
    }
  
    // Method to return components of HistoryList as an array of Components
    public Component[] getListComponents() {
      return getComponents();
    }

    // Method to handle deleting the selected HistoryQuestion (if any) in 
    // the HistoryList
    public void deleteSelected(HistoryQuestionGUI historyQuestionGUI) {
        remove(historyQuestionGUI);
        revalidate();
    }

    // Method to remove all HistoryQuestions from GUI
    public void removeEverythingGUI() {
      this.removeAll();
      this.setDefaultGUI();
      revalidate();
    }

}
