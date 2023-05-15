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
  
    // HistoryList constructor, sets format and prepares filepath of 
    // history.csv file for reading and writing
    public HistoryList() {
      count = 0;
      empty = true;
      components = 0;

      // Sets format
      BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      this.setLayout(layout);
      setAlignmentY(TOP_ALIGNMENT);
      this.add(Box.createVerticalGlue());

      // Prepares filepath
      folder = "project";
      path = folder;
      filename = folder + "/history.csv";
      String dir_path = "src/main/java";
      File potential_dir = new File(dir_path);
      if (potential_dir.isDirectory()) {
        filename = dir_path + "/" + filename;
        path = dir_path + "/" + path;
      }
    }
  
    // Overridden add method, which removes default settings
    // if input is not a JTextArea and writes to history.csv if
    // input is a HistoryQuestion
    // Also updates count and component variables
    @Override
    public Component add(Component comp) {
      
      // Updates empty state
      if (empty == true && !(comp instanceof JLabel)) {
          this.removeAll();
          empty = false;
      }

      // Removes default settings if necessary
      if (!(comp instanceof JTextArea)) {
        removeDefault();
      }

      // Writes to history.csv and updates count/components
      // if input is HistoryQuestion
      if (comp instanceof HistoryQuestion) {
        HistoryQuestion historyQuestion = (HistoryQuestion) comp;
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
  
    // Method to check if there are any HistoryQuestions in the HistoryList
    public boolean isEmpty() {
      return (components == 0);
    }
  
    // Method to set default text in HistoryList for display
    public void setDefault() {

      // Sets format
      BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      this.setLayout(layout);
      setAlignmentY(TOP_ALIGNMENT);
      this.add(Box.createVerticalGlue());

      empty = true;
      components = 0;
      
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
    }
  
    // Method to remove default text area
    public void removeDefault() {
      for (Component c : getComponents()) {
        if (c instanceof JTextArea) {
          remove(c);
        }
      }
    }
  
    // Method to return components of HistoryList as an array of Components
    public Component[] getListComponents() {
      return getComponents();
    }

    // Method to handle deleting the selected HistoryQuestion (if any) in 
    // the HistoryList
    public void deleteSelected() {


      for (Component c : getComponents()) {

        if (c instanceof HistoryQuestion) {
          HistoryQuestion workingQ = (HistoryQuestion) c;

          // Check if a HistoryQuestion is selected
          if (workingQ.getState() == true) {
            String removal_index = workingQ.getIndex();

            // Set up filepath for history.csv updates
            String temp_file_path = path + "/temphistory.csv";
            File tempHistory = new File(temp_file_path);
            File historyFile = new File(filename);

            if (historyFile.isFile()) {

              // Iterate through history.csv line by line, storing 
              // each line that is not the one to be deleted in temphistory.csv
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

            // Clean up files
            historyFile.delete();
            tempHistory.renameTo(historyFile);

            // Actually remove HistoryQuestion and update default as needed
            remove(workingQ);
            components--;
            if (components == 0) {
              setDefault();
            }
          }
        }
      }
    }

    // Method to remove all HistoryQuestions and restore default
    public void removeEverything() {
      this.removeAll();
      components = 0;
      File historyFile = new File(filename);
      historyFile.delete();
      setDefault();
      empty = true;
    }

    // Method to return number of HistoryQuestions
    public int getComponentsNum() {
      return components;
    }

    // Method to return current count
    public int getCount() {
      return count;
    }
    
    // Method to check if HistoryList has any HistoryQuestions
    public boolean getEmpty() {
      return empty;
    }

    // Method to populate HistoryList using contents of history.csv
    public void populateOldHistory() {

      // Initializes history.csv File
      File historyFile = new File(filename);
      if (historyFile.isFile()) {
        try {

          // Create Scanner to read file
          Scanner csv_scanner = new Scanner(historyFile);
          int max = -1;
          while (csv_scanner.hasNextLine()) {
            // Remove default from HistoryList, since we are adding a HistoryQuestion 
            removeDefault();

            // Parse line and create HistoryQuestion
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