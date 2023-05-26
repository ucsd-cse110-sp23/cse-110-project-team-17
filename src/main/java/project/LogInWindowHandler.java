package project;

import java.awt.Component;
import java.io.*;
import java.util.*;

import project.gui.LogInWindowGUI;

public class LogInWindowHandler {
    LogInWindowGUI logInWindow;
    //Vector<HistoryQuestionHandler> historyList;
    Map<String, String> users;
    HTTPRequestMaker httpRequestMaker;


    // Constructor, initializes filepath and history list GUI component
    public LogInWindowHandler() {
        this.logInWindow = new LogInWindowGUI(this);
        users = new HashMap<String, String>();
        try {
            FileReader file = new FileReader("information.txt");
            BufferedReader br = new BufferedReader(file);
            String st;  
            while ((st = br.readLine()) != null) {
                String[] information = st.split(",");
                users.put(information[0], information[1]);
            }
            br.close();
            file.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Create an account for new users
    public boolean createAccount(String username, String password) {
        if (verifyUsername(username)) {
            return false;
        }
        users.put(username, password);
        try {
            FileWriter file = new FileWriter("information.txt");
            for(Map.Entry<String,String> entry : users.entrySet()) {
                file.write(entry.getKey() + ',' + entry.getValue() + '\n');
            }
            file.close();
          }
        catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // Login check username and password match
    public boolean logIn(String username, String password) {
        if (verifyPassword(username, password)) {
            return true;
        }
        return false;
    }

    // Use "GET" request from server to check if username has been taken
    public boolean verifyUsername(String username) {
        //String[] getUserName = httpRequestMaker.getRequest(username);
        return users.containsKey(username);
    }

    // Use "GET" request from server to check the password matched withe the
    // corresponding username
    public boolean verifyPassword(String username, String password) {
        if(users.containsKey(username) && (users.get(username).equals(password)))
            return true;
        return false;
    }
    
    // Method to get associated LogInWindow GUI object
    public LogInWindowGUI getLogInWindowGUI() {
        return this.logInWindow;
    }

}
