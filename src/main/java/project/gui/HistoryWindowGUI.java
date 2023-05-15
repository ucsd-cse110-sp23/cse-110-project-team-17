package project.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HistoryWindowGUI extends JPanel {
    Color gray = new Color(218, 229, 234);
    HistoryHeaderGUI historyHeader;
    HistoryListGUI list;
    JScrollPane scrollWindow;
  
    // HistoryWindow constructor, adds HistoryHeader and HistoryList
    public HistoryWindowGUI(HistoryListGUI historyListGUI) {

        // Sets format
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);
        this.setBackground(gray);
        setAlignmentY(TOP_ALIGNMENT);
    
        // Add History Header
        historyHeader = new HistoryHeaderGUI();
        this.add(historyHeader);
    
        // Add History List
        list = historyListGUI;
        scrollWindow = new JScrollPane(list);
        this.add(scrollWindow);
    }
  
    // Method to return associated HistoryList
    public HistoryListGUI getList() {
        return this.list;
    }

    // Method to return associated HistoryHeader
    public HistoryHeaderGUI getHistoryHeader() {
        return this.historyHeader;
    }
}
