package project.handler;

import java.util.*;
import project.gui.LogInWindowGUI;
import project.*;

public class LogInWindowHandler {
    LogInWindowGUI logInWindow;
    Map<String, String> dbMap;
    HTTPRequestMaker httpRequestMaker;

    // Constructor, initializes filepath and history list GUI component
    public LogInWindowHandler() {
        this.logInWindow = new LogInWindowGUI(this);
        this.dbMap = DBCreate.readUserInformation();
    }

    // Create an account for new users
    public boolean createAccount(String username, String password, String password2) {
        if (verifyUsername(username, password)) {
            return false;
        }
        if (username.equals("") || password.equals("")) {
            return false;
        }
        if (!password.equals(password2)) {
            return false;
        }
        DBCreate.createUser(username, password);
        dbMap = DBCreate.readUserInformation();
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
    public boolean verifyUsername(String username, String password) {
        return dbMap.containsKey(username);
    }

    // Check if the two passwords are the same
    public boolean checkPassword(String password1, String password2) {
        return password1.equals(password2);
    }

    // Use "GET" request from server to check the password matched withe the
    // corresponding username
    public boolean verifyPassword(String username, String password) {
        if(dbMap.containsKey(username) && (dbMap.get(username).equals(password)))
            return true;
        return false;
    }
    
    // Method to get associated LogInWindow GUI object
    public LogInWindowGUI getLogInWindowGUI() {
        return this.logInWindow;
    }

}
