package project.gui;

import project.handler.*;

import java.util.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import project.handler.setupEmailHandler;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class setupEmailGUI extends JPanel {
    Color backgroundColor = new Color(240, 248, 255);
    JButton save;
    JButton cancel;
    String userString;
    setupEmailHandler handler;
    JTextField firstName;
    JTextField lastName;
    JTextField username;
    JTextField emailAddress;
    JTextField SMTPHost;
    JTextField TLSPort;
    JTextField emailPassword;
    JTextField displayName;

    // ChatWindow constructor, sets format
    public setupEmailGUI(setupEmailHandler handler) {

        this.handler = handler;

        if (handler != null) {
            userString = handler.getUsername();
        }
        else {
            userString = "";
        }
        

        GridLayout layout = new GridLayout(10, 2);
        layout.setVgap(20); // Vertical gap
    
        this.setLayout(layout); // 2 chat boxes
        this.setPreferredSize(new Dimension(400, 100));
        this.setBackground(backgroundColor);

        // First Name textbox
        this.firstName = new JTextField("");
        firstName.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        JLabel firstNameLabel = new JLabel("First name");
        this.add(firstNameLabel);
        this.add(firstName);

        // Last Name textbox
        this.lastName = new JTextField("");
        lastName.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        JLabel lastNameLabel = new JLabel("Last name");
        lastNameLabel.setLabelFor(lastName);
        this.add(lastNameLabel);
        this.add(lastName);
        
        // Email Address textbox
        this.emailAddress = new JTextField("");
        emailAddress.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        JLabel emailAddressLabel = new JLabel("Email Address");
        emailAddressLabel.setLabelFor(emailAddress);
        this.add(emailAddressLabel);
        this.add(emailAddress);

        // Email Address Password textbox
        this.emailPassword = new JTextField("");
        emailPassword.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        JLabel emailPasswordLabel = new JLabel("Email Password");
        emailPasswordLabel.setLabelFor(emailPassword);
        this.add(emailPasswordLabel);
        this.add(emailPassword);
        
        // SMTP Host textbox
        this.SMTPHost = new JTextField("");
        SMTPHost.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        JLabel SMTPHostLabel = new JLabel("SMTP Host");
        SMTPHostLabel.setLabelFor(SMTPHost);
        this.add(SMTPHostLabel);
        this.add(SMTPHost);

        // TLS Port textbox
        this.TLSPort = new JTextField("");
        TLSPort.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        JLabel TLSPortLabel = new JLabel("TLS Port");
        TLSPortLabel.setLabelFor(TLSPort);
        this.add(TLSPortLabel);
        this.add(TLSPort);

        // displayName textbox
        this.displayName = new JTextField("");
        displayName.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        JLabel displayNameLabel = new JLabel("Display Name");
        displayNameLabel.setLabelFor(displayName);
        this.add(displayNameLabel);
        this.add(displayName);

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

    public void updateFields() {
        

        userString = handler.getUsername();

        Map<String, String> emailInfo = DBCreate.readEmailInformation(userString);

        String firstNameString = emailInfo.get("firstName_id");
        String lastNameString = emailInfo.get("lastName_id");
        String emailAddressString = emailInfo.get("emailAddress_id");
        String emailPasswordString = emailInfo.get("emailPassword_id");
        String SMTPHostString = emailInfo.get("SMTPHost_id");
        String TLSPortString = emailInfo.get("TLSPort_id");
        String displayNameString = emailInfo.get("displayName_id");

        
        firstName.setText(firstNameString);
        lastName.setText(lastNameString);
        emailAddress.setText(emailAddressString);
        emailPassword.setText(emailPasswordString);
        SMTPHost.setText(SMTPHostString);
        TLSPort.setText(TLSPortString);
        displayName.setText(displayNameString);


    }

    public String getFirstName() {
        return firstName.getText();
    }
    
    public String getLastName() {
        return lastName.getText();
    }

    public String getEmailAddress() {
        return emailAddress.getText();
    }
    
    public String getSMTPHost() {
        return SMTPHost.getText();
    }
    
    public String getTLSPort() {
        return TLSPort.getText();
    }
    
    public String getEmailPassword() {
        return emailPassword.getText();
    }

    public String getDisplayName() {
        return displayName.getText();
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