package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import project.handler.LogInWindowHandler;
import javax.swing.JButton;
import javax.swing.JFrame;

public class LogInWindowGUI extends JFrame {
    Color backgroundColor = new Color(240, 248, 255);
    JButton createAccount;
    JButton logIn;
    LogInWindowHandler logInWindowHandler;
    InputTextField username;
    InputTextField password;
    InputTextField verify;

    // LogInWindowGUI constructor, sets format
    public LogInWindowGUI(LogInWindowHandler logInWindowHandler) {
        this.logInWindowHandler = logInWindowHandler;
        GridLayout layout = new GridLayout(10, 1);
        layout.setVgap(20); // Vertical gap
    
        this.setLayout(layout); // 2 chat boxes
        this.setPreferredSize(new Dimension(400, 100));
        this.setBackground(backgroundColor);

        this.username = new InputTextField("Username: ");
        username.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(username);
        
        this.password = new InputTextField("Password: ");
        password.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(password);

        this.verify = new InputTextField("Verify Password: ");
        verify.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(verify);
        // Add ClearAll button
        createAccount = new JButton();
        createAccount.setText("Create Account");
        createAccount.setPreferredSize(new Dimension(80, 30));
        createAccount.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(createAccount);

        // Add deleteSelected button
        logIn = new JButton();
        logIn.setText("Log In");
        logIn.setPreferredSize(new Dimension(80, 30));
        logIn.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(logIn);

        super.pack();
        super.setSize(800,800);
    }

    // Method to return string in username field
    public String getUserName() {
        return username.getInput();
    }
    
    // Method to return string in password field
    public String getPassword() {
        return password.getInput();
    }

    // Method to return string in second password field
    public String getVerifyPassword() {
        return verify.getInput();
    }

    
    
    // Return "Create Account" button
    public JButton getCreateAccount() {
        return this.createAccount;
    }

    // Return "Log In" button
    public JButton getlogIn() {
        return this.logIn;
    }
    
}
