package project.handler;

import java.io.*;
import java.util.*;

import java.util.Vector;

import java.util.regex.*;

import project.gui.*;
import project.*;

public class HistoryListHandler {
    Boolean empty;
    int prompts;
    int count;
    String filename;
    String path;
    String folder;
    private String regex;
    HistoryListGUI historyListGUI;
    Vector<HistoryPromptHandler> historyList;
    HTTPRequestMaker httpRequestMaker;
    String username;

    // Constructor, initializes filepath and history list GUI component
    public HistoryListHandler(String regex, HTTPRequestMaker httpRequestMaker) {
        count = 0;
        empty = true;
        prompts = 0;
        this.regex = regex;
        historyList = new Vector<HistoryPromptHandler>();
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

    // Method to add a historyPromptHandler to the list
    public void add(HistoryPromptHandler historyPromptHandler, boolean old) {
        if (username.equals("")) {
            System.out.println("No username given.");
            return;
        }
        historyListGUI.removeDefault();
        historyList.add(historyPromptHandler);
        historyListGUI.add(historyPromptHandler.getHistoryPromptGUI());
        if (isEmpty()) {
            toggleEmpty();
        }
        if (!old) {
            // Add the prompt data to database
            String promptString = historyPromptHandler.getString(regex);
            DBCreate.addPromptDB(username, promptString);
        }
        count++;
        prompts++;
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

    // Method to return list of HistoryPrompt objects
    public Vector<HistoryPromptHandler> getHistoryList() {
        return historyList;
    }

    // Method to delete selected history prompt (if any) in
    // history list
    public void deleteSelected() {
        if (username.equals("")) {
            System.out.println("No username given.");
            return;
        }
        for (int i = 0; i < historyList.size(); i++) {
            HistoryPromptHandler hph = historyList.get(i);
            if (hph.isSelected()) {
                String removalString = hph.getString(regex);
                String removal_index = hph.getIndex();
                DBCreate.deletePromptDB(username, removalString);

                // Actually delete the prompt and remove default as needed
                historyListGUI.deleteSelected(hph.getHistoryPromptGUI());
                httpRequestMaker.deleteRequest(removal_index);
                historyList.remove(hph);
                prompts--;
                if (prompts == 0) {
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
        prompts = 0;
        DBCreate.clearAllDB(username);
        setDefault();
        empty = true;
        for (int i = 0; i < historyList.size(); i++) {
            HistoryPromptHandler hph = historyList.get(i);
            String removal_index = hph.getIndex();
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
                // Parse line and create historyPrompt
                String[] prompt_parts = historyArrayList.get(i).split(regex);
                String index_str = prompt_parts[0];
                if (isInteger(index_str)) {
                    int index = Integer.parseInt(index_str);
                    String prompt = prompt_parts[1];
                    String answer = prompt_parts[2];
                    if (index > max) {
                        max = index;
                    }
                    httpRequestMaker.postRequest(index_str, prompt, answer);
                    HistoryPromptHandler historyprompt = 
                        new HistoryPromptHandler(index_str, httpRequestMaker);
                    add(historyprompt, true);
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

    // Method to return email body from email historyPromptHandler
    public String getEmailBody() {
        String body = "No message selected.\n";

        if (username.equals("")) {
            System.out.println("No username given.");
            return body;
        }
        for (int i = 0; i < historyList.size(); i++) {
            HistoryPromptHandler hph = historyList.get(i);
            if (hph.isSelected()) {
                String tempSubject = hph.getPrompt();
                if (tempSubject.contains("Create email")) {
                    body = hph.getAnswer();
                }
            }
        }

        return body;
    }

    // Method to return email subject from email historyPromptHandler
    public String getEmailSubject() {
        String subject = "No message selected.\n";

        if (username.equals("")) {
            System.out.println("No username given.");
            return subject;
        }
        for (int i = 0; i < historyList.size(); i++) {
            HistoryPromptHandler hph = historyList.get(i);
            if (hph.isSelected()) {
                String tempSubject = hph.getPrompt();
                if (tempSubject.contains("Create email") && 
                        tempSubject.length() > 12) {
                    subject = tempSubject.substring(12);
                }
            }
        }

        return subject;
    }
}