package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HistoryHeaderGUI extends JPanel {
    Color gray = new Color(218, 229, 234);

    // HistoryHeader constructor, adds the "Clear All" and "Delete Selected"
    // buttons, as well as sets format
    public HistoryHeaderGUI() {
        
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
    }
}