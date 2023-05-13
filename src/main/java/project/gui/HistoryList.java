package project.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.io.*;
import java.util.*;

public class HistoryList extends JPanel {
    Color backgroundColor = new Color(240, 248, 255);
    Color gray = new Color(218, 229, 234);
    Boolean empty;
    int components;
    int count;
    String filename;
    String path;
    String folder;
  
    public HistoryList() {
      count = 0;
      BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      this.setLayout(layout);
      setAlignmentY(TOP_ALIGNMENT);
      empty = true;
      components = 0;
      this.add(Box.createVerticalGlue());
      folder = "project";
      path = folder;
      filename = folder + "/history.csv";
      String dir_path = "src/main/java";
      File potential_dir = new File(dir_path);
      if (potential_dir.isDirectory()) {
        filename = dir_path + "/" + filename;
        path = dir_path + "/" + path;
      }
      // this.setPreferredSize(new Dimension(400, 160));
    }
  
    @Override
    public Component add(Component comp) {
      
      if (empty == true && !(comp instanceof JLabel)) {
          this.removeAll();
          empty = false;
          
      }
      if (!(comp instanceof JTextArea)) {
        removeDefault();
      }
      if (comp instanceof HistoryQuestion) {
        HistoryQuestion historyQuestion = (HistoryQuestion) comp;
        historyQuestion.setIndex(count);
        String index = historyQuestion.getIndex();
        String question = historyQuestion.getQuestionText();
        String answer = historyQuestion.getAnswerText();
        try {
          File csv_file = new File(filename);
          FileWriter csv_writer = new FileWriter(csv_file, true);
          csv_writer.write(index + "," + question + "," + answer + "\n");
          csv_writer.close();
        }
        catch (IOException ioe) {
          throw new RuntimeException("IO Exception in csv writer.");
        }
        count++;
        components++;
      }
      super.add(comp);
      return comp;
    }
  
    public boolean isEmpty() {
      return (components == 0);
    }
  
    public void setDefault() {
      BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      this.setLayout(layout);
      setAlignmentY(TOP_ALIGNMENT);
      empty = true;
      components = 0;
      add(Box.createVerticalGlue());
      JTextArea defaultArea = new JTextArea("No history to show. Ask a question!");
      defaultArea.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
      defaultArea.setBackground(gray); // set background color of text 
      defaultArea.setEditable(false);
      defaultArea.setFont(new Font("Serif", Font.ITALIC, 16));
      defaultArea.setLineWrap(true);
      defaultArea.setWrapStyleWord(true);
      defaultArea.setPreferredSize(new Dimension(400, 160));
      add(defaultArea);
    }
  
    public void removeDefault() {
      for (Component c : getComponents()) {
        if (c instanceof JTextArea) {
          remove(c);
        }
      }
    }
  
    public Component[] getListComponents() {
      return getComponents();
    }

    public void deleteSelected() {
      for (Component c : getComponents()) {
        if (c instanceof HistoryQuestion) {
          HistoryQuestion workingQ = (HistoryQuestion) c;
          if (workingQ.getState() == true) {
            String removal_index = workingQ.getIndex();
            String temp_file_path = path + "/temphistory.csv";
            File tempHistory = new File(temp_file_path);
            File historyFile = new File(filename);
            if (historyFile.isFile()) {
              try {
                Scanner csv_scanner = new Scanner(historyFile);
                FileWriter csv_writer = new FileWriter(tempHistory, true);
                while (csv_scanner.hasNextLine()) {
                  removeDefault();
                  String next_line = csv_scanner.nextLine();
                  String[] question_parts = next_line.split(",");
                  if (!(removal_index.equals(question_parts[0]))) {
                    csv_writer.write(next_line + "\n");
                  }
                  components++;
                }
                csv_scanner.close();
                csv_writer.close();
              }
              catch (FileNotFoundException fnfe) {
                throw new RuntimeException("File not found for scanning.");
              }
              catch (IOException ioe) {
                throw new RuntimeException("IO Exception during scanning.");
              }
            }
            historyFile.delete();
            tempHistory.renameTo(historyFile);
            remove(workingQ);
            components--;
          }
        }
      }
    }

    public void removeEverything() {
      this.removeAll();
      components = 0;
      File historyFile = new File(filename);
      historyFile.delete();
      setDefault();
      empty = true;
    }

    public int getComponentsNum() {
      return components;
    }
    
    public boolean getEmpty() {
      return empty;
    }

    public void populateOldHistory() {
      File historyFile = new File(filename);
      if (historyFile.isFile()) {
        try {
          Scanner csv_scanner = new Scanner(historyFile);
          int max = -1;
          while (csv_scanner.hasNextLine()) {
            removeDefault();
            String[] question_parts = csv_scanner.nextLine().split(",");
            int index = Integer.parseInt(question_parts[0]);
            String question = question_parts[1];
            String answer = question_parts[2];
            if (index > max) {
              max = index;
            }
            HistoryQuestion historyQuestion = 
              new HistoryQuestion(Integer.toString(index), question, answer);
            super.add(historyQuestion);
            components++;
          }
          csv_scanner.close();
          if (max != -1) {
            count = max + 1;
          }
        }
        catch (FileNotFoundException fnfe) {
          throw new RuntimeException("File not found for scanning.");
        }
      }
    }
  }