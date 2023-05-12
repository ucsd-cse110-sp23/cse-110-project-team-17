package project.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HistoryWindow extends JPanel {
    Color gray = new Color(218, 229, 234);
    HistoryHeader historyHeader;
    HistoryList list;
    JScrollPane scrollWindow;
  
    public HistoryWindow() {
      BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
      this.setLayout(layout);
      this.setBackground(gray);
      setAlignmentY(TOP_ALIGNMENT);
  
      // Add History Header
      historyHeader = new HistoryHeader();
      this.add(historyHeader);
  
      // Add History List
      list = new HistoryList();
      scrollWindow = new JScrollPane(list);
      this.add(scrollWindow);
    }
  
    public HistoryList getList() {
      return this.list;
    }

    public HistoryHeader getHistoryHeader() {
      return this.historyHeader;
    }
  
  }