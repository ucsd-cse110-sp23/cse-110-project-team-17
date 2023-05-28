package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeaderGUI extends JPanel {

    Color backgroundColor = new Color(240, 248, 255);
   
    // Header Constructor, sets up app title and sets format
    public HeaderGUI() {

        // Sets format
        this.setPreferredSize(new Dimension(400, 60)); // Size of the header
        this.setBackground(backgroundColor);
        GridLayout layout = new GridLayout(1,1);
        this.setLayout(layout);
        
        // Add main title
        JLabel titleText = new JLabel("SayIt Assistant 2"); // Text of the header
        titleText.setPreferredSize(new Dimension(200, 60));
        titleText.setFont(new Font("BrixSansBlack", Font.ITALIC, 20));
        titleText.setHorizontalAlignment(JLabel.CENTER); // Align the text to the center
        this.add(titleText); // Add the text to the header        
    }
}