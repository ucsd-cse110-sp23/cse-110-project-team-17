package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HistoryHeader extends JPanel {
    Color gray = new Color(218, 229, 234);
    JButton clearAll;
    JButton deleteSelected;
  
    // HistoryHeader constructor, adds the "Clear All" and "Delete Selected"
    // buttons, as well as sets format
    public HistoryHeader() {
      
      // Sets format
      BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
      this.setLayout(layout);
      this.setBackground(gray);
      setAlignmentY(TOP_ALIGNMENT);
  
      // Add History label above history scroll window
      JLabel historyText = new JLabel("History");
      historyText.setPreferredSize(new Dimension(80, 30));
      historyText.setFont(new Font("BrixSansBlack", Font.ITALIC, 20));
      //historyText.setBorder(BorderFactory.createLineBorder(Color.red));
      this.add(historyText);
  
      // Add filler space
      this.add(Box.createRigidArea(new Dimension(120, 30)));
  
      // Add ClearAll button
      clearAll = new JButton();
      clearAll.setText("Clear All");
      clearAll.setPreferredSize(new Dimension(80, 30));
      clearAll.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
      this.add(clearAll);

      // Add deleteSelected button
      deleteSelected = new JButton();
      deleteSelected.setText("Delete Selected");
      deleteSelected.setPreferredSize(new Dimension(80, 30));
      deleteSelected.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
      this.add(deleteSelected);
    }

    
    // Return "Clear All" button
    public JButton getClearAll() {
      return this.clearAll;
    }

    // Return "Delete Selected" button
    public JButton getdeleteSelected() {
      return this.deleteSelected;
    }
}