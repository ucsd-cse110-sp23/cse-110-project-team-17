package project.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class ChatList extends JPanel {

    Color backgroundColor = new Color(240, 248, 255);
  
    // ChatList Constructor, sets format
    public ChatList() {
      GridLayout layout = new GridLayout(10, 1);
      layout.setVgap(20); // Vertical gap
  
      this.setLayout(layout); // 2 chat boxes
      this.setPreferredSize(new Dimension(400, 100));
      this.setBackground(backgroundColor);
    }
  
    // Method to clear the Chat Window
    public void clearChatWindow() {
      for (Component c : getComponents()) {
        if (c instanceof ChatBox) {
          remove(c); // remove the chatbox component
        }
      }
    }
  }