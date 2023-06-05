package project.handler;

import java.util.*;
import project.gui.setupEmailGUI;
import project.*;

public class setupEmailHandler {
    setupEmailGUI setupEmail;
    Map<String, String> dbMap;
    HTTPRequestMaker httpRequestMaker;
    String userName;
    String firstName;
    String lastName;
    String emailAddress;
    String emailPassword;
    String SMTPHost;
    String TLSPort;
    String displayName;

    // Constructor, initializes filepath and history list GUI component
    public setupEmailHandler() {
        this.setupEmail = new setupEmailGUI(this);
        this.dbMap = DBCreate.readUserInformation();
        this.userName = "";
    }

    // Use "GET" request from server to check if username has been taken
    public boolean verifyUsername(String username, String password) {
        return dbMap.containsKey(username);
    }

    public void addEmailInfo() {
        firstName = setupEmail.getFirstName();
        lastName = setupEmail.getLastName();
        emailAddress = setupEmail.getEmailAddress();
        emailPassword = setupEmail.getEmailPassword();
        SMTPHost = setupEmail.getSMTPHost();
        TLSPort = setupEmail.getTLSPort();
        displayName = setupEmail.getDisplayName();
        DBCreate.addEmailInformation(firstName, lastName, userName, emailAddress, SMTPHost, TLSPort, emailPassword, displayName);
    }

    // Method to set first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Method to set last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Method to set email address
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    // Method to set email password
    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    // Method to set SMTP host
    public void setSMTPHost(String SMTPHost) {
        this.SMTPHost = SMTPHost;
    }

    // Method to set TLS Port
    public void setTLSPort(String TLSPort) {
        this.TLSPort = TLSPort;
    }

    // Method to set display name
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    // Method to add email information
    public void addCurrEmailInfo() {
        DBCreate.addEmailInformation(firstName, lastName, userName, emailAddress, SMTPHost, TLSPort, emailPassword, displayName);
    }

    // Method to set username
    public void setUsername(String username) {
        this.userName = username;
    }

    // Method to get associated LogInWindow GUI object
    public setupEmailGUI getsetupEmailWindowGUI() {
        return this.setupEmail;
    }

    // Method to get username string
    public String getUsername() {
        return this.userName;
    }
}