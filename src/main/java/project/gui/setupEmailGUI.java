package project.gui;

import project.handler.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import project.handler.setupEmailHandler;
import javax.swing.JPanel;
import javax.swing.JButton;

public class setupEmailGUI extends JPanel {
    Color backgroundColor = new Color(240, 248, 255);
    JButton save;
    JButton cancel;
    setupEmailHandler handler;
    InputTextField firstName;
    InputTextField lastName;
    InputTextField username;
    InputTextField emailAddress;
    InputTextField SMTPHost;
    InputTextField TLSPort;
    InputTextField emailPassword;

    // ChatWindow constructor, sets format
    public setupEmailGUI(setupEmailHandler handler) {
        this.handler = handler;
        GridLayout layout = new GridLayout(10, 1);
        layout.setVgap(20); // Vertical gap
    
        this.setLayout(layout); // 2 chat boxes
        this.setPreferredSize(new Dimension(400, 100));
        this.setBackground(backgroundColor);

        // First Name textbox
        this.firstName = new InputTextField("First Name: ");
        firstName.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(firstName);

        // Last Name textbox
        this.lastName = new InputTextField("Last Name: ");
        lastName.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(lastName);        

        // Username textbox
        this.username = new InputTextField("Username: ");
        username.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(username);
        
        // Email Address textbox
        this.emailAddress = new InputTextField("Email Address: ");
        emailAddress.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(emailAddress);

        // Email Address Password textbox
        this.emailPassword = new InputTextField("Email Address Password: ");
        emailPassword.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(emailPassword);
        
        // SMTP Host textbox
        this.SMTPHost = new InputTextField("SMTP Host: ");
        SMTPHost.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(SMTPHost);

        // TLS Port textbox
        this.TLSPort = new InputTextField("TLS Port: ");
        TLSPort.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(TLSPort);

        // Add ClearAll button
        save = new JButton();
        save.setText("Save");
        save.setPreferredSize(new Dimension(80, 30));
        save.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(save);

        // Add deleteSelected button
        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setPreferredSize(new Dimension(80, 30));
        cancel.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(cancel);
    }

    public String getFirstName() {
        return firstName.getInput();
    }
    
    public String getLastName() {
        return lastName.getInput();
    }
    
    public String getUserName() {
        return username.getInput();
    }

    public String getEmailAddress() {
        return emailAddress.getInput();
    }
    
    public String getSMTPHost() {
        return SMTPHost.getInput();
    }
    
    public String getTLSPort() {
        return TLSPort.getInput();
    }
    
    public String getEmailPassword() {
        return emailPassword.getInput();
    }

    // Return "Create Account" button
    public JButton getSaveButton() {
        return this.save;
    }

    // Return "Log In" button
    public JButton getCancelButton() {
        return this.cancel;
    }
}