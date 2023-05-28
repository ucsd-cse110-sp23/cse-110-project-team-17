package project.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class InputTextField extends JPanel{
    JLabel index;
    JTextField inputBox;
    
    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);

    public InputTextField(String label) {
        this.setPreferredSize(new Dimension(400, 20)); // set size of task
        this.setBackground(gray); // set background color of task

        this.setLayout(new BorderLayout()); // set layout of task

        index = new JLabel(label); // create index label
        index.setPreferredSize(new Dimension(100, 20)); // set size of index label
        index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
        this.add(index, BorderLayout.WEST); // add index label to task

        inputBox = new JTextField(""); // create task name text field
        inputBox.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
        inputBox.setBackground(gray); // set background color of text field

        this.add(inputBox, BorderLayout.CENTER);
    }

    public String getInput() {
        return inputBox.getText();
    }
}
