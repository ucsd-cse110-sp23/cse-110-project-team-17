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
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(20); // Vertical gap
    
        this.setLayout(layout); // 2 chat boxes
        this.setPreferredSize(new Dimension(400, 400));
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

    // Method to display a given prompt and answer pair
    public void displayPrompt(String prompt, String answer) {
        this.setVisible(true);
        ChatBoxGUI promptBox = new ChatBoxGUI("Prompt", prompt);
        ChatBoxGUI answerBox = new ChatBoxGUI("Answer", answer);
        promptBox.setVisible(true);
        answerBox.setVisible(true);
        this.add(promptBox);
        this.add(answerBox);
        repaint();
        revalidate();
    }
}
