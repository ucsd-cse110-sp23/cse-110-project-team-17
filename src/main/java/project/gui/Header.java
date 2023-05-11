package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Header extends JPanel {

Color backgroundColor = new Color(240, 248, 255);

    public Header() {

        this.setPreferredSize(new Dimension(400, 60)); // Size of the header
        this.setBackground(backgroundColor);
        GridLayout layout = new GridLayout(1,1);
        this.setLayout(layout);
        
        // Add main title
        JLabel titleText = new JLabel("SayIt"); // Text of the header
        titleText.setPreferredSize(new Dimension(200, 60));
        titleText.setFont(new Font("BrixSansBlack", Font.ITALIC, 20));
        titleText.setHorizontalAlignment(JLabel.CENTER); // Align the text to the center
        //titleText.setBorder(BorderFactory.createLineBorder(Color.red));
        this.add(titleText); // Add the text to the header
        // components++;
        
    }
}