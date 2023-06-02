package project.handler;

import java.util.*;
import project.gui.setupEmailGUI;
import project.*;

public class setupEmailHandler {
    setupEmailGUI setupEmail;
    Map<String, String> dbMap;
    HTTPRequestMaker httpRequestMaker;

    // Constructor, initializes filepath and history list GUI component
    public setupEmailHandler() {
        this.setupEmail = new setupEmailGUI(this);
        this.dbMap = DBCreate.readUserInformation();
    }

    // Use "GET" request from server to check if username has been taken
    public boolean verifyUsername(String username, String password) {
        return dbMap.containsKey(username);
    }

    public void addEmailInfo() {
        String firstName = setupEmail.getFirstName();
        String lastName = setupEmail.getLastName();
        String userName = setupEmail.getUserName();
        String emailAddress = setupEmail.getEmailAddress();
        String emailPassword = setupEmail.getEmailPassword();
        String SMTPHost = setupEmail.getSMTPHost();
        String TLSPort = setupEmail.getTLSPort();
        DBCreate.addEmailInformation(firstName, lastName, userName, emailAddress, SMTPHost, TLSPort, emailPassword);
    }
    
    // Method to get associated LogInWindow GUI object
    public setupEmailGUI getsetupEmailWindowGUI() {
        return this.setupEmail;
    }

}
