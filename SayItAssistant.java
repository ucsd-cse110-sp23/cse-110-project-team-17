/**
 * This code was refactored from the original code found at:
 * https://copyassignment.com/to-do-list-app-in-java/
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

import org.json.JSONArray;
import org.json.JSONObject;




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






class Task extends JPanel {

  JLabel index;
  JTextField taskName;
  JButton doneButton;

  Color gray = new Color(218, 229, 234);
  Color green = new Color(188, 226, 158);

  private boolean markedDone;

  Task() {
    this.setPreferredSize(new Dimension(400, 20)); // set size of task
    this.setBackground(gray); // set background color of task

    this.setLayout(new BorderLayout()); // set layout of task

    markedDone = false;

    index = new JLabel(""); // create index label
    index.setPreferredSize(new Dimension(20, 20)); // set size of index label
    index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(index, BorderLayout.WEST); // add index label to task

    taskName = new JTextField(""); // create task name text field
    taskName.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    taskName.setBackground(gray); // set background color of text field

    this.add(taskName, BorderLayout.CENTER);

    doneButton = new JButton("Done");
    doneButton.setPreferredSize(new Dimension(80, 20));
    doneButton.setBorder(BorderFactory.createEmptyBorder());
    doneButton.setFocusPainted(false);

    this.add(doneButton, BorderLayout.EAST);
  }

  public void changeIndex(int num) {
    this.index.setText(num + ""); // num to String
    this.revalidate(); // refresh
  }

  public JButton getDone() {
    return doneButton;
  }

  public boolean getState() {
    return markedDone;
  }

  public void changeState() {
    if (markedDone) {
      this.setBackground(gray);
      taskName.setBackground(gray);
      markedDone = false;
    }
    else {
      this.setBackground(green);
      taskName.setBackground(green);
      markedDone = true;
    }
    revalidate();
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


interface IChatGPT {
  public String ask(String prompt) throws IOException, InterruptedException;
}

class MockChatGPT implements IChatGPT {

  MockChatGPT() {}

  public String ask(String prompt) throws IOException, InterruptedException {
    return "Mock answer to the following prompt:\n" + prompt;
  }
}

class ChatGPT implements IChatGPT {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
  private static final String API_KEY = "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";
  // Joseph's API Token = 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
  private static final String MODEL = "text-davinci-003";

  ChatGPT() {}

  public String ask(String prompt) throws IOException, InterruptedException {
    int maxTokens = 100;
    String generatedText = "";


    JSONObject requestBody = new JSONObject();
    requestBody.put("model", MODEL);
    requestBody.put("prompt", prompt);
    requestBody.put("max_tokens", maxTokens);
    requestBody.put("temperature", 1.0);


    //Create the HTTP client
    HttpClient client = HttpClient.newHttpClient();

    // Create the request object
    HttpRequest request = HttpRequest
    .newBuilder()
    .uri(URI.create(API_ENDPOINT))
    .header("Content-type", "application/json")
    .header("Authorization", String.format("Bearer %s",API_KEY))
    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
    .build();

    // Send the request and receive the response
    try {
      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString()
      );
      // Process the response
      String responseBody = response.body();
      //System.out.println(responseBody);

      JSONObject responseJson = new JSONObject(responseBody);

      JSONArray choices = responseJson.getJSONArray("choices");
      generatedText = choices.getJSONObject(0).getString("text");
    }
    catch (IOException io_e) {
      System.out.println("Something went wrong with IO in ChatGPT.");
    }
    catch (InterruptedException int_e) {
      System.out.println("Something was interrupted in ChatGPT.");
    }
      

    

    return generatedText;
  }
}

class ChatBox extends JPanel {
  JLabel dialogue_type;
  JTextArea dialogue;

  Color gray = new Color(218, 229, 234);

  ChatBox(String type_input, String dialogue_input) {
    this.setPreferredSize(new Dimension(400, 20)); // set size of task
    this.setBackground(gray); // set background color of task

    this.setLayout(new BorderLayout()); // set layout of task

    dialogue_type = new JLabel(type_input); // create index label
    dialogue_type.setPreferredSize(new Dimension(80, 20)); // set size of index label
    dialogue_type.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(dialogue_type, BorderLayout.WEST); // add index label to task

    dialogue = new JTextArea(dialogue_input); // create task name text field
    dialogue.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    dialogue.setBackground(gray); // set background color of text 
    dialogue.setEditable(false);
    dialogue.setFont(new Font("Serif", Font.ITALIC, 16));
    dialogue.setLineWrap(true);
    dialogue.setWrapStyleWord(true);
    this.add(dialogue, BorderLayout.CENTER);
  }
}

class ChatList extends JPanel {

  Color backgroundColor = new Color(240, 248, 255);

  ChatList() {
    GridLayout layout = new GridLayout(10, 1);
    layout.setVgap(20); // Vertical gap

    this.setLayout(layout); // 2 chat boxes
    this.setPreferredSize(new Dimension(400, 100));
    this.setBackground(backgroundColor);
  }

  public void clearChatWindow() {
    for (Component c : getComponents()) {
      if (c instanceof ChatBox) {
        remove(c); // remove the chatbox component
      }
    }
  }
}


class Footer extends JPanel {

  JButton questionButton;

  JButton askQuestion;
  JButton stopRecordingButton;

  Color backgroundColor = new Color(240, 248, 255);
  Border emptyBorder = BorderFactory.createEmptyBorder();

  Footer() {
    this.setPreferredSize(new Dimension(400, 60));
    this.setBackground(backgroundColor);
    GridLayout layout = new GridLayout(1, 2);
    this.setLayout(layout);
    //this.setBorder(BorderFactory.createLineBorder(Color.red));

    // questionButton = new JButton("Ask a Question"); // add task button
    // questionButton.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
    // this.add(questionButton); // add to footer

    askQuestion = new JButton("Ask a Question"); // add task button
    askQuestion.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
    this.add(askQuestion); // add to footer

    stopRecordingButton = new JButton("Stop Recording"); // add task button
    stopRecordingButton.setFont(new Font("Sans-serif", Font.ITALIC, 10)); // set font
    this.add(stopRecordingButton); // add to footer
    stopRecordingButton.setVisible(false);
  }

  public JButton getquestionButton() {
    return questionButton;
  }
  
  public JButton getAskQuestion() {
    return askQuestion;
  }

  public JButton getStopRecordingButton() {
    return stopRecordingButton;
  }
}

class Header extends JPanel {

  Color backgroundColor = new Color(240, 248, 255);

  Header() {
    this.setPreferredSize(new Dimension(400, 60)); // Size of the header
    this.setBackground(backgroundColor);
    GridLayout layout = new GridLayout(1, 2);
    this.setLayout(layout);

    JLabel historyText = new JLabel("History");
    historyText.setPreferredSize(new Dimension(50, 50));
    historyText.setFont(new Font("BrixSansBlack", Font.ITALIC, 15));
    historyText.setHorizontalAlignment(JLabel.LEFT); // Align the text to the center
    historyText.setVerticalAlignment(SwingConstants.BOTTOM);
    //historyText.setBorder(BorderFactory.createLineBorder(Color.red));
    this.add(historyText);

    ImageIcon trashCan = new ImageIcon("/Users/rei_crzy/Documents/CSE 110/Final Project/cse-110-project-team-17/trashCan.png");
    JButton clearAll = new JButton();
    clearAll.setText("Clear All");
    clearAll.setVerticalTextPosition(AbstractButton.CENTER);
    clearAll.setHorizontalTextPosition(AbstractButton.LEADING); 
    clearAll.setPreferredSize(new Dimension(50, 50));
    clearAll.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
    clearAll.setHorizontalAlignment(SwingConstants.RIGHT); // Align the text to the center
    clearAll.setVerticalAlignment(SwingConstants.BOTTOM);
    //clearAll.setBorder(BorderFactory.createLineBorder(Color.red));
    this.add(clearAll);

    JLabel titleText = new JLabel("SayIt"); // Text of the header
    titleText.setPreferredSize(new Dimension(200, 60));
    titleText.setFont(new Font("BrixSansBlack", Font.ITALIC, 30));
    titleText.setHorizontalAlignment(JLabel.CENTER); // Align the text to the center
    //titleText.setBorder(BorderFactory.createLineBorder(Color.red));
    this.add(titleText); // Add the text to the header


    
  }
}

class AppFrame extends JFrame {

  private Header header;
  private Footer footer;
  private IChatGPT chatGPT;
  private ChatList chatList;
  private List list;
  private String prompt;
  private String chat_gpt_answer;
  private JButton askQuestion;
  private JButton stopRecordingButton;

  AppFrame() {
    this.revalidate();
    this.setSize(800, 600); // 400 width and 600 height
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
    this.setVisible(true); // Make visible

    header = new Header();
    footer = new Footer();
    list = new List();
    chatList = new ChatList();
    prompt = "Who is Louis Braille?";
    chatGPT = new MockChatGPT();

    this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
    this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
    this.add(list, BorderLayout.WEST); // Add history list in left of screen
    this.add(chatList, BorderLayout.CENTER); // Add chat list in middle of footer and title

    askQuestion = footer.getAskQuestion();
    stopRecordingButton = footer.getStopRecordingButton();

    addListeners();
  }

  public void addListeners() {
    askQuestion.addMouseListener(
      new MouseAdapter() {
        @override
        public void mousePressed(MouseEvent e) {
          askQuestion.setVisible(false);
          stopRecordingButton.setVisible(true);

          chatList.clearChatWindow();
          repaint();
        }
      }
    );
    stopRecordingButton.addMouseListener(
      new MouseAdapter() {
        @override
        public void mousePressed(MouseEvent e) {
          ChatBox question = new ChatBox("Question", prompt);
          chatList.add(question);
          revalidate();
          try {
            chat_gpt_answer = chatGPT.ask(prompt);
            ChatBox answer = new ChatBox("Answer", chat_gpt_answer);
            chatList.add(answer);
            revalidate();
          }
          catch (IOException io_e) {
            throw new RuntimeException("An IO Exception happened on click.");
          }
          catch (InterruptedException int_e) {
            throw new RuntimeException("An Interruption Exception happened on click.");
          }

          HistoryQuestion historyQuestion = new HistoryQuestion();
          list.add(historyQuestion); // Add new task to list
          historyQuestion.insertQuestion(prompt);
          historyQuestion.insertAnswer(chat_gpt_answer);
          list.updateNumbers(); // Updates the numbers of the tasks
          JButton selectButton = historyQuestion.getDone();
          selectButton.addActionListener(
            (ActionEvent e2) -> {
                historyQuestion.changeState(); // Change color of task
                list.updateNumbers(); // Updates the numbers of the tasks
                revalidate(); // Updates the frame
              }
          );

          stopRecordingButton.setVisible(false);
          askQuestion.setVisible(true);
        }
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
