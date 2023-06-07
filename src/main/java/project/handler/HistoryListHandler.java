package project.handler;

import java.io.*;
import java.util.*;

import java.util.Vector;

import java.util.regex.*;

import project.gui.*;
import project.*;

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
    String username;

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
        username = "";
    }

    // Method to add a HistoryQuestionHandler to the list
    public void add(HistoryQuestionHandler historyQuestionHandler, boolean old) {
        if (username.equals("")) {
            System.out.println("No username given.");
            return;
        }
        historyListGUI.removeDefault();
        historyList.add(historyQuestionHandler);
        historyListGUI.add(historyQuestionHandler.getHistoryQuestionGUI());
        if (isEmpty()) {
            toggleEmpty();
        }
        if (!old) {
            // Add the prompt data to database
            String promptString = historyQuestionHandler.getString(regex);
            DBCreate.addPromptDB(username, promptString);
        }
        count++;
        questions++;
    }


    // Method to set username
    public void setUsername(String username) {
        this.username = username;
    }

    // Method to get username
    public String getUsername() {
        return this.username;
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
        if (username.equals("")) {
            System.out.println("No username given.");
            return;
        }
        for (int i = 0; i < historyList.size(); i++) {
            HistoryQuestionHandler hqh = historyList.get(i);
            if (hqh.isSelected()) {
                String removalString = hqh.getString(regex);
                String removal_index = hqh.getIndex();
                DBCreate.deleteQuestionDB(username, removalString);

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
        if (username.equals("")) {
            System.out.println("No username given.");
            return;
        }
        questions = 0;
        DBCreate.clearAllDB(username);
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
        if (username.equals("")) {
            System.out.println("No username given.");
            return;
        }
        
        ArrayList<String> historyArrayList = DBCreate.getHistoryListDB(username);

        if (!(historyArrayList == null || historyArrayList.size() == 0)) {
            int max = -1;
            for (int i = 0; i < historyArrayList.size(); i++) {
                // Parse line and create HistoryQuestion
                String[] question_parts = historyArrayList.get(i).split(regex);
                String index_str = question_parts[0];
                if (isInteger(index_str)) {
                    int index = Integer.parseInt(index_str);
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
            }
            if (max != -1) {
                count = max + 1;
            }
        }
    }

    // Method to get associated HistoryList GUI object
    public HistoryListGUI getHistoryListGUI() {
        return this.historyListGUI;
    }
        
    // Helper variable to determine if a string is an integer or not
    private boolean isInteger(String numString) {
        if (numString.equals("")) {
            return false;
        }
        String numRegex = "[0-9]+[\\.]?[0-9]*";
        return Pattern.matches(numRegex, numString);
    }
}