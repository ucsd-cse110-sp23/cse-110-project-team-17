package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JPanel;
import project.LogInWindowHandler;
import javax.swing.JButton;

public class LogInWindowGUI extends JPanel {
    Color backgroundColor = new Color(240, 248, 255);
    JButton createAccount;
    JButton logIn;
    LogInWindowHandler logInWindowHandler;
    InputTextField username;
    InputTextField password;

    // ChatWindow constructor, sets format
    public LogInWindowGUI(LogInWindowHandler logInWindowHandler) {
        this.logInWindowHandler = logInWindowHandler;
        GridLayout layout = new GridLayout(10, 1);
        layout.setVgap(20); // Vertical gap
    
        this.setLayout(layout); // 2 chat boxes
        this.setPreferredSize(new Dimension(400, 100));
        this.setBackground(backgroundColor);

        this.username = new InputTextField("Username");
        username.setPreferredSize(new Dimension(50, 30));
        username.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(username);
        
        this.password = new InputTextField("Password");
        password.setPreferredSize(new Dimension(50, 30));
        password.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(password);
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
    }

    public String getUserName() {
        return username.getInput();
    }
    
    public String getPassword() {
        return password.getInput();
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
