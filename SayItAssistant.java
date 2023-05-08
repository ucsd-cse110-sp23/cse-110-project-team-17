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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

class HistoryQuestion extends JPanel {

  JLabel index;
  JTextArea question;
  JTextField answer;
  JButton selectButton;

  Color gray = new Color(218, 229, 234);
  Color green = new Color(188, 226, 158);

  private boolean selected; //change to be if selected?

  HistoryQuestion() {
    this.setPreferredSize(new Dimension(400, 60)); // set size of task
    this.setBackground(gray); // set background color of task
    this.setLayout(new FlowLayout()); // set layout of task
    this.setBorder(BorderFactory.createLineBorder(Color.black));;

    selected = false;

    index = new JLabel(""); // create index label
    index.setPreferredSize(new Dimension(20, 20)); // set size of index label
    index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(index); // add index label to task

    question = new JTextArea("Question: "); // create task name text field
    question.setPreferredSize(new Dimension(300, 20));
    //question.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    question.setBackground(Color.white); // set background color of text field
    question.setEditable(false);

    answer = new JTextField("Answer: "); // create task name text field
    answer.setPreferredSize(new Dimension(200, 20));
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

  @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
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
    BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(layout);
    setAlignmentY(TOP_ALIGNMENT);
    empty = true;
  }

  @Override
  public Component add(Component comp) {
    
    if (empty == true && !(comp instanceof JLabel)) {
        this.removeAll();
        empty = false;
        
    }
    super.add(comp);
    return comp;
  }


  public ArrayList<HistoryQuestion> loadHistory()  {
    // hint 1: use try-catch block
    // hint 2: use BufferedReader and FileReader
    // hint 3: task.taskName.setText(line) sets the text of the task
    try {
      String linestrings;
      FileReader fileR = new FileReader("history.txt");
      BufferedReader bufferR = new BufferedReader(fileR);
      ArrayList<HistoryQuestion> historyList = new ArrayList<HistoryQuestion>();
      

      while (bufferR.ready()) {
          HistoryQuestion set = new HistoryQuestion();
          set.setMaximumSize(getPreferredSize());
          linestrings = bufferR.readLine();
          set.insertQuestion(linestrings);
          this.add(set);
          historyList.add(set);
      }
      
        bufferR.close();
        revalidate();

      return historyList;

    }

    catch (IOException exception) {
      System.out.println("load not implemented");
      return null;
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
    JScrollPane scrollPane = new JScrollPane(list);
    scrollPane.setPreferredSize(new Dimension(420,400));
    this.add(scrollPane, BorderLayout.WEST);
    JLabel notif = new JLabel("No History");
    notif.setMaximumSize(new Dimension(100, 20));
    notif.setAlignmentX(CENTER_ALIGNMENT);
    list.add(notif);
   

    this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
    this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
    questionButton = footer.getquestionButton();
    dummyAskQuestionButton = footer.getdummyAskQuestionButton();
    list.loadHistory();

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
          question.insertQuestion("testing longer string bigger than field adwddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
          question.setMaximumSize(getPreferredSize());
          revalidate();
          //list.updateNumbers(); // Updates the numbers of the tasks
          JButton selectButton = question.getDone();
          selectButton.addActionListener(
            (ActionEvent e2) -> {
                question.changeState(); // Change color of task
                //list.updateNumbers(); // Updates the numbers of the tasks
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
