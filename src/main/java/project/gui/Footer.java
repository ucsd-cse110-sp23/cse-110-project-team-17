package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Footer extends JPanel {

    JButton askQuestion;
    JButton stopRecordingButton;

    Color backgroundColor = new Color(240, 248, 255);
    Border emptyBorder = BorderFactory.createEmptyBorder();

    public Footer() {
        this.setPreferredSize(new Dimension(400, 60));
        this.setBackground(backgroundColor);
        GridLayout layout = new GridLayout(1, 2);
        this.setLayout(layout);
        //this.setBorder(BorderFactory.createLineBorder(Color.red));

        askQuestion = new JButton("Ask a Question"); // add task button
        askQuestion.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
        this.add(askQuestion); // add to footer

        stopRecordingButton = new JButton("Stop Recording"); // add task button
        stopRecordingButton.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
        this.add(stopRecordingButton); // add to footer
        stopRecordingButton.setVisible(false);
    }

    public JButton getAskQuestion() {
        return askQuestion;
    }

    public JButton getStopRecordingButton() {
        return stopRecordingButton;
    }
}