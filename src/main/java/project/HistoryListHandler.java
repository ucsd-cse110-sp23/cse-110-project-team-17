package project;

import java.io.*;
import java.util.*;

import java.util.Vector;

import project.gui.HistoryListGUI;

public class HistoryListHandler {
    Boolean empty;
    int questions;
    int count;
    String filename;
    String path;
    String folder;
    private String regex;
    HistoryListGUI historyListGUI;
    Vector<HistoryQuestionHandler> historyList;
    HTTPRequestMaker httpRequestMaker;

    // Constructor, initializes filepath and history list GUI component
    public HistoryListHandler(String regex, HTTPRequestMaker httpRequestMaker) {
        count = 0;
        empty = true;
        questions = 0;
        this.regex = regex;
        historyList = new Vector<HistoryQuestionHandler>();
        this.httpRequestMaker = httpRequestMaker;

        this.historyListGUI = new HistoryListGUI(this);
        setDefault();

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

    // Method to add a HistoryQuestionHandler to the list
    public void add(HistoryQuestionHandler historyQuestionHandler, boolean old) {
        historyListGUI.removeDefault();
        historyList.add(historyQuestionHandler);
        historyListGUI.add(historyQuestionHandler.getHistoryQuestionGUI());
        if (isEmpty()) {
            toggleEmpty();
        }
        if (!old) {
            // Write the question data to history.csv
            String index = historyQuestionHandler.getIndex();
            String question = historyQuestionHandler.getQuestion();
            String answer = historyQuestionHandler.getAnswer();
            try {
                File csv_file = new File(filename);
                FileWriter csv_writer = new FileWriter(csv_file, true);
                csv_writer.write(index + regex + question + regex + answer + "\n");
                csv_writer.close();
            }
            catch (IOException ioe) {
                throw new RuntimeException("IO Exception in csv writer.");
            }
        }
        count++;
        questions++;
    }




    // Method to return current count
    public String getCount() {
        return Integer.toString(count);
    }

    // Method to return emptiness (boolean)
    public boolean isEmpty() {
        return empty;
    }

    // Method to toggle emptiness
    public void toggleEmpty() {
        empty = !empty;
    }

    // Method to return list of HistoryQuestion objects
    public Vector<HistoryQuestionHandler> getHistoryList() {
        return historyList;
    }

    // Method to delete selected history question (if any) in
    // history list
    public void deleteSelected() {
        for (int i = 0; i < historyList.size(); i++) {
            HistoryQuestionHandler hqh = historyList.get(i);
            if (hqh.isSelected()) {
                String removal_index = hqh.getIndex();

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
                            String next_line = csv_scanner.nextLine();
                            String[] question_parts = next_line.split(regex);
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

                // Actually delete the question and remove default as needed
                historyListGUI.deleteSelected(hqh.getHistoryQuestionGUI());
                httpRequestMaker.deleteRequest(removal_index);
                historyList.remove(hqh);
                questions--;
                if (questions == 0) {
                    setDefault();
                    empty = true;
                }
            }
        }
    }

    // Method to remove everything from history list
    public void removeEverything() {
        questions = 0;
        File historyFile = new File(filename);
        historyFile.delete();
        setDefault();
        empty = true;
        for (int i = 0; i < historyList.size(); i++) {
            HistoryQuestionHandler hqh = historyList.get(i);
            String removal_index = hqh.getIndex();
            httpRequestMaker.deleteRequest(removal_index);
        }
        historyList.clear();
        historyListGUI.removeEverythingGUI();
    }

    // Method to reset default
    public void setDefault() {
        historyListGUI.setDefaultGUI();
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
                    // Parse line and create HistoryQuestion
                    String[] question_parts = csv_scanner.nextLine().split(regex);
                    String index_str = question_parts[0];
                    int index = Integer.parseInt(question_parts[0]);
                    String question = question_parts[1];
                    String answer = question_parts[2];
                    if (index > max) {
                        max = index;
                    }
                    httpRequestMaker.postRequest(index_str, question, answer);
                    HistoryQuestionHandler historyQuestion = 
                        new HistoryQuestionHandler(index_str, httpRequestMaker);
                    add(historyQuestion, true);
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

    // Method to get associated HistoryList GUI object
    public HistoryListGUI getHistoryListGUI() {
        return this.historyListGUI;
    }
}
