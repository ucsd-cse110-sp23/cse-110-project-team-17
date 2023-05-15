package project.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class ChatWindowGUI extends JPanel {
    
    Color backgroundColor = new Color(240, 248, 255);
  

    // ChatWindow constructor, sets format
    public ChatWindowGUI() {
        GridLayout layout = new GridLayout(10, 1);
        layout.setVgap(20); // Vertical gap
    
        this.setLayout(layout); // 2 chat boxes
        this.setPreferredSize(new Dimension(400, 100));
        this.setBackground(backgroundColor);
    }

    // Method to clear the Chat Window GUI
    public void clearChatWindowGUI() {
        for (Component c : getComponents()) {
            if (c instanceof ChatBoxGUI) {
                remove(c); // remove the chatbox component
            }
        }
        repaint();
        revalidate();
    }

    public void displayQuestion(String question, String answer) {
        ChatBoxGUI questionBox = new ChatBoxGUI("Question", question);
        ChatBoxGUI answerBox = new ChatBoxGUI("Answer", answer);
        add(questionBox);
        add(answerBox);
    }
}
