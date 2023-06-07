package project.gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.util.ArrayList;

public class AutomaticLogInGUI extends JFrame implements AutomaticLogInGUISubject {

    private JButton acceptButton;
    private JButton denyButton;
    private JTextArea promptArea;
    private String username;
    private String password;
    private ArrayList<AutomaticLoginGUIObserver> algObservers;
    

    // AutomaticLogInGUI Constructor
    public AutomaticLogInGUI() {
        Color backgroundColor = new Color(240, 248, 255);
        GridLayout layout = new GridLayout(10, 1);
        layout.setVgap(20); // Vertical gap
    
        this.setLayout(layout); // 2 chat boxes
        this.setPreferredSize(new Dimension(400, 100));
        this.setBackground(backgroundColor);


        this.username = "";
        this.password = "";

        String prompt = "Would you like to login automatically on this computer?";

        promptArea = new JTextArea(prompt);
        promptArea.setPreferredSize(new Dimension(160, 90));
        promptArea.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(promptArea);

        acceptButton = new JButton();
        acceptButton.setText("Accept");
        acceptButton.setPreferredSize(new Dimension(80, 30));
        acceptButton.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(acceptButton);

        denyButton = new JButton();
        denyButton.setText("No thanks");
        denyButton.setPreferredSize(new Dimension(80, 30));
        denyButton.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
        this.add(denyButton);

        super.pack();
        super.setSize(800,800);

        algObservers = new ArrayList<AutomaticLoginGUIObserver>();
    }

    // Method to add observer to observer list
    public void register(AutomaticLoginGUIObserver algObs) {
        algObservers.add(algObs);
    }

    // Method to notify observers
    public void notifyObservers() {
        for (AutomaticLoginGUIObserver algObs : algObservers) {
            algObs.update(username, password);
        }
    }

    // Method to return accept button
    public JButton getAcceptButton() {
        return this.acceptButton;
    }

    // Method to return deny button
    public JButton getDenyButton() {
        return this.denyButton;
    }

    // Method to set username
    public void setUsername(String username) {
        this.username = username;
    }

    // Method to set password
    public void setPassword(String password) {
        this.password = password;
    }
}