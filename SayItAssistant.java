/**
 * This code was refactored from the original code found at:
 * https://copyassignment.com/to-do-list-app-in-java/
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

class HistoryQuestion extends JPanel {

  JLabel index;
  JTextField question;
  JTextField answer;
  JButton selectButton; //remove or change to answer cutoff

  Color gray = new Color(218, 229, 234);
  Color green = new Color(188, 226, 158);

  private boolean selected; //change to be if selected?

  HistoryQuestion() {
    this.setPreferredSize(new Dimension(400, 40)); // set size of task
    this.setBackground(gray); // set background color of task
    this.setLayout(new FlowLayout()); // set layout of task

    selected = false;

    index = new JLabel(""); // create index label
    index.setPreferredSize(new Dimension(20, 20)); // set size of index label
    index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(index); // add index label to task

    question = new JTextField("Question: "); // create task name text field
    question.setPreferredSize(new Dimension(100, 20));
    //question.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    question.setBackground(gray); // set background color of text field
    question.setEditable(false);

    answer = new JTextField("Answer: "); // create task name text field
    answer.setPreferredSize(new Dimension(100, 20));
    //question.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    answer.setBackground(green); // set background color of text field
    answer.setEditable(false);

    this.add(question);
    this.add(answer);

    selectButton = new JButton("Select");
    selectButton.setPreferredSize(new Dimension(80, 20));
    selectButton.setBorder(BorderFactory.createEmptyBorder());
    selectButton.setFocusPainted(false);

    this.add(selectButton);
  }

  public void changeIndex(int num) {
    this.index.setText(num + ""); // num to String
    this.revalidate(); // refresh
  }

  public JButton getDone() {
    return selectButton;
  }

  public boolean getState() {
    return selected;
  }

  public void changeState() {
    if (selected) {
      this.setBackground(gray);
      question.setBackground(gray);
      selected = false;
    }
    else {
      this.setBackground(green);
      question.setBackground(green);
      selected = true;
    }
    revalidate();
  }

  public void insertQuestion(String questionString) {
      question.setText(question.getText() + questionString);
      question.setCaretPosition(0);
  }

  public void insertAnswer(String ansString) {
    question.setText(answer.getText() + ansString);
}

}

class List extends JPanel {

  Color backgroundColor = new Color(240, 248, 255);
  Boolean empty;

  List() {
    GridLayout layout = new GridLayout(10, 1);
    layout.setVgap(5); // Vertical gap

    this.setLayout(layout); // 10 tasks
    this.setPreferredSize(new Dimension(400, 560));
    this.setBackground(backgroundColor);
    empty = true;
  }

  public void updateNumbers() {
    Component[] listItems = this.getComponents();

    for (int i = 0; i < listItems.length; i++) {
      if (listItems[i] instanceof HistoryQuestion) {
        ((HistoryQuestion) listItems[i]).changeIndex(i + 1);
      }
    }
  }

  public void removeCompletedTasks() {
    for (Component c : getComponents()) {
      if (c instanceof HistoryQuestion) {
        if (((HistoryQuestion) c).getState()) {
          remove(c); // remove the component
          updateNumbers(); // update the indexing of all items
        }
      }
    }
  }

  /**
   * Loads tasks from a file called "tasks.txt"
   * @return an ArrayList of Task
   */
  public ArrayList<HistoryQuestion> loadHistory() {
    // hint 1: use try-catch block
    // hint 2: use BufferedReader and FileReader
    // hint 3: task.question.setText(line) sets the text of the task
    ArrayList<HistoryQuestion> result = new ArrayList<>();
    try {
      FileReader file = new FileReader("History.txt");
      BufferedReader br = new BufferedReader(file);
      String st;  
      while ((st = br.readLine()) != null) {
        HistoryQuestion task = new HistoryQuestion();
        task.question.setText(st);
        result.add(task);
      }
      br.close();
      file.close();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    //System.out.println("loadTasks() not implemented");
    updateNumbers();
    revalidate();
    return result;
  }

  /**
   * Saves tasks to a file called "tasks.txt"
   */
  public void saveTasks() {
    // hint 1: use try-catch block
    // hint 2: use FileWriter
    // hint 3 get list of Tasks using this.getComponents()
    try {
      FileWriter file = new FileWriter("/Users/rei_crzy/Documents/CSE 110/Lab 5/CSE110Lab5_/src/tasks.txt");
      Component[] listOfSavedTasks = this.getComponents();
      for(int i = 0; i < listOfSavedTasks.length; i++) {
        HistoryQuestion task = (HistoryQuestion) listOfSavedTasks[i];
        file.write(task.question.getText() + '\n');
      }
      file.close();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}

class Footer extends JPanel {

  JButton questionButton;
  JButton dummyAskQuestionButton;

  Color backgroundColor = new Color(240, 248, 255);
  Border emptyBorder = BorderFactory.createEmptyBorder();

  Footer() {
    this.setPreferredSize(new Dimension(400, 60));
    this.setBackground(backgroundColor);
    GridLayout layout = new GridLayout(1, 4);
    this.setLayout(layout);

    questionButton = new JButton("Ask a question"); // add task button
    questionButton.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
    this.add(questionButton); // add to footer

    dummyAskQuestionButton = new JButton("Dummy Question Adder"); // add task button
    dummyAskQuestionButton.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
    this.add(dummyAskQuestionButton); // add to footer

  }

  public JButton getquestionButton() {
    return questionButton;
  }
  public JButton getdummyAskQuestionButton() {
    return dummyAskQuestionButton;
  }
}

class Header extends JPanel {

  Color backgroundColor = new Color(240, 248, 255);

  Header() {
    this.setPreferredSize(new Dimension(400, 60)); // Size of the header
    this.setBackground(backgroundColor);
    JLabel titleText = new JLabel("SayIt"); // Text of the header
    titleText.setPreferredSize(new Dimension(200, 60));
    titleText.setFont(new Font("Sans-serif", Font.BOLD, 20));
    titleText.setHorizontalAlignment(JLabel.CENTER); // Align the text to the center
    this.add(titleText); // Add the text to the header
  }
}

class AppFrame extends JFrame {

  private Header header;
  private Footer footer;
  private List list;

  private JButton questionButton;
  private JButton dummyAskQuestionButton;

  AppFrame() {
    this.revalidate();
    this.setSize(800, 600); // 400 width and 600 height
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
    this.setVisible(true); // Make visible

    header = new Header();
    footer = new Footer();
    list = new List();

    this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
    this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
    this.add(list, BorderLayout.WEST); // Add list in middle of footer and title

    questionButton = footer.getquestionButton();
    dummyAskQuestionButton = footer.getdummyAskQuestionButton();

    addListeners();
  }

  public void addListeners() {
    questionButton.addMouseListener(
      new MouseAdapter() {
        // @override
        // public void mousePressed(MouseEvent e) {
        //   Task task = new Task();
        //   list.add(task); // Add new task to list
        //   list.updateNumbers(); // Updates the numbers of the tasks

        //   JButton selectButton = task.getDone();
        //   selectButton.addMouseListener(
        //     new MouseAdapter() {
        //       @override
        //       public void mousePressed(MouseEvent e) {
        //         task.changeState(); // Change color of task
        //         list.updateNumbers(); // Updates the numbers of the tasks
        //         revalidate(); // Updates the frame
        //       }
        //     }
        //   );
        // }
      }
    );

    dummyAskQuestionButton.addActionListener(
      (ActionEvent e) -> {
          HistoryQuestion question = new HistoryQuestion();
          list.add(question); // Add new task to list
          question.insertQuestion("testing longer string bigger than field");
          list.updateNumbers(); // Updates the numbers of the tasks
          JButton selectButton = question.getDone();
          selectButton.addActionListener(
            (ActionEvent e2) -> {
                question.changeState(); // Change color of task
                list.updateNumbers(); // Updates the numbers of the tasks
                revalidate(); // Updates the frame
              }
          );
      }
    );
  }
}

public class SayItAssistant {

  public static void main(String args[]) {
    new AppFrame(); // Create the frame
  }
}

@interface override {
}
