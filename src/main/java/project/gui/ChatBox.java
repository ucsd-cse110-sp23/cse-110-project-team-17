package project.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatBox extends JPanel {
    JLabel dialogue_type;
    JTextArea dialogue;
    String dialogue_label;
    String dialogue_text;

    Color gray = new Color(218, 229, 234);

    public ChatBox(String type_input, String dialogue_input) {

        dialogue_label = type_input;
        dialogue_text = dialogue_input;

        this.setPreferredSize(new Dimension(400, 20)); // set size of task
        this.setBackground(gray); // set background color of task

        this.setLayout(new BorderLayout()); // set layout of task

        dialogue_type = new JLabel(type_input); // create index label
        dialogue_type.setPreferredSize(new Dimension(80, 20)); // set size of index label
        dialogue_type.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
        this.add(dialogue_type, BorderLayout.WEST); // add index label to task

        dialogue = new JTextArea(dialogue_input); // create task name text field
        dialogue.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
        dialogue.setPreferredSize(new Dimension(320, 80));
        dialogue.setBackground(gray); // set background color of text 
        dialogue.setEditable(false);
        dialogue.setFont(new Font("Serif", Font.ITALIC, 16));
        dialogue.setLineWrap(true);
        dialogue.setWrapStyleWord(true);
        this.add(dialogue, BorderLayout.CENTER);
    }

    public String getLabel() {
        return dialogue_label;
    }

    public String getDialogueText() {
        return dialogue_text;
    }
}