package project.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HistoryList extends JPanel {
    Color backgroundColor = new Color(240, 248, 255);
    Color gray = new Color(218, 229, 234);
    Boolean empty;
    int components;
  
    public HistoryList() {
      BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      this.setLayout(layout);
      setAlignmentY(TOP_ALIGNMENT);
      empty = true;
      components = 0;
      this.add(Box.createVerticalGlue());
      // this.setPreferredSize(new Dimension(400, 160));
    }
  
    @Override
    public Component add(Component comp) {
      
      if (empty == true && !(comp instanceof JLabel)) {
          this.removeAll();
          empty = false;
          
      }
      if (!(comp instanceof JTextArea)) {
        removeDefault();
      }
      super.add(comp);
      components++;
      return comp;
    }
  
    public boolean isEmpty() {
      return (components == 0);
    }
  
    public void setDefault() {
      JTextArea defaultArea = new JTextArea("No history to show. Ask a question!");
      defaultArea.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
      defaultArea.setBackground(gray); // set background color of text 
      defaultArea.setEditable(false);
      defaultArea.setFont(new Font("Serif", Font.ITALIC, 16));
      defaultArea.setLineWrap(true);
      defaultArea.setWrapStyleWord(true);
      defaultArea.setPreferredSize(new Dimension(400, 160));
      add(defaultArea);
    }
  
    public void removeDefault() {
      for (Component c : getComponents()) {
        if (c instanceof JTextArea) {
          remove(c);
        }
      }
    }
  
    public Component[] getListComponents() {
      return getComponents();
    }

    public void removeEverything() {
      this.removeAll();
      components = 0;
      empty = true;
    }

    public int getComponentsNum() {
      return components;
    }
    
    public boolean getEmpty() {
      return empty;
    }
  }