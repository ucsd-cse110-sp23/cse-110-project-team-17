/**
 * This code was refactored from the original code found at:
 * https://copyassignment.com/to-do-list-app-in-java/
 */

package project;

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
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.BoxLayout;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;




class HistoryQuestion extends JPanel {

  JLabel index;
  JTextField question;
  JTextField answer;
  String question_text;
  String answer_text;
  JButton selectButton; //remove or change to answer cutoff

  Color gray = new Color(218, 229, 234);
  Color green = new Color(188, 226, 158);

  private boolean selected; //change to be if selected?

  HistoryQuestion() {
    this.setMinimumSize(new Dimension(400, 160)); // set size of task
    this.setBackground(gray); // set background color of task
    this.setLayout(new FlowLayout()); // set layout of task

    selected = false;

    index = new JLabel(""); // create index label
    index.setPreferredSize(new Dimension(20, 160)); // set size of index label
    index.setHorizontalAlignment(JLabel.CENTER); // set alignment of index label
    this.add(index); // add index label to task

    question = new JTextField("Question: "); // create task name text field
    question.setPreferredSize(new Dimension(300, 160));
    //question.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    question.setBackground(gray); // set background color of text field
    question.setEditable(false);

    //answer = new JTextField("Answer: "); // create task name text field
    //answer.setPreferredSize(new Dimension(100, 20));
    //question.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    //answer.setBackground(green); // set background color of text field
    //answer.setEditable(false);

    this.add(question);
    //this.add(answer);

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
      question_text = questionString;
  }

  public void insertAnswer(String ansString) {
    //question.setText(answer.getText() + ansString);
    answer_text = ansString;
  }

  public String getQuestionText() {
    return question_text;
  }

  public String getAnswerText() {
    return answer_text;
  }

}

class HistoryHeader extends JPanel {
  Color gray = new Color(218, 229, 234);

  HistoryHeader() {
    BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
    this.setLayout(layout);
    this.setBackground(gray);
    // this.setMaximumSize(new Dimension(400, 30));
    setAlignmentY(TOP_ALIGNMENT);

    // Add History label above history scroll window
    JLabel historyText = new JLabel("History");
    historyText.setPreferredSize(new Dimension(80, 30));
    historyText.setFont(new Font("BrixSansBlack", Font.ITALIC, 20));
    //historyText.setBorder(BorderFactory.createLineBorder(Color.red));
    this.add(historyText);

    // Add filler space
    this.add(Box.createRigidArea(new Dimension(120, 30)));

    // Add ClearAll button
    JButton clearAll = new JButton();
    clearAll.setText("Clear All");
    clearAll.setPreferredSize(new Dimension(80, 30));
    clearAll.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
    this.add(clearAll);
  }
}

class HistoryWindow extends JPanel {
  Color gray = new Color(218, 229, 234);
  HistoryHeader historyHeader;
  List list;
  JScrollPane scrollWindow;

  HistoryWindow() {
    BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(layout);
    this.setBackground(gray);
    setAlignmentY(TOP_ALIGNMENT);

    // Add History Header
    historyHeader = new HistoryHeader();
    this.add(historyHeader);

    // Add History List
    list = new List();
    scrollWindow = new JScrollPane(list);
    this.add(scrollWindow);
  }

  public List getList() {
    return this.list;
  }

}




class List extends JPanel {
  Color backgroundColor = new Color(240, 248, 255);
  Color gray = new Color(218, 229, 234);
  Boolean empty;
  int components;

  List() {
    BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(layout);
    setAlignmentY(TOP_ALIGNMENT);
    empty = true;
    components = 0;
    this.add(Box.createVerticalGlue());
    // this.setPreferredSize(new Dimension(400, 160));
  }

  @Override
  public Component add(Component comp) {
    
    if (empty == true && !(comp instanceof JLabel)) {
        this.removeAll();
        empty = false;
        
    }
    if (!(comp instanceof JTextArea)) {
      removeDefault();
    }
    super.add(comp);
    components++;
    return comp;
  }

  public boolean isEmpty() {
    return (components == 0);
  }

  public void setDefault() {
    JTextArea defaultArea = new JTextArea("No history to show. Ask a question!");
    defaultArea.setBorder(BorderFactory.createEmptyBorder()); // remove border of text field
    defaultArea.setBackground(gray); // set background color of text 
    defaultArea.setEditable(false);
    defaultArea.setFont(new Font("Serif", Font.ITALIC, 16));
    defaultArea.setLineWrap(true);
    defaultArea.setWrapStyleWord(true);
    defaultArea.setPreferredSize(new Dimension(400, 160));
    add(defaultArea);
  }

  public void removeDefault() {
    for (Component c : getComponents()) {
      if (c instanceof JTextArea) {
        remove(c);
      }
    }
  }

  public Component[] getListComponents() {
    return getComponents();
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
  String dialogue_label;
  String dialogue_text;

  Color gray = new Color(218, 229, 234);

  ChatBox(String type_input, String dialogue_input) {

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

interface IQuestionHandler {
  public void startQuestion();
  public String getQuestion();
}

class MockQuestion implements IQuestionHandler {
  int index;
  String[] options = new String[5];

  MockQuestion() {
    index = 0;
    options[0] = "Who is Louis Braille?";
    options[1] = "What did Louis Braille do?";
    options[2] = "How did Louis Braille invent Braille?";
    options[3] = "When was Louis Braille born?";
    options[4] = "Where did Louis Braille live?";
  }

  public void startQuestion() {}

  public String getQuestion() {
    String toReturn = options[index];
    index = (index + 1) % 5;
    return toReturn;
  }
}



class Footer extends JPanel {

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

class Header extends JPanel {

  Color backgroundColor = new Color(240, 248, 255);

  Header() {

    // int rows = 2;
    // int cols = 3;
    // int size = rows * cols;
    // int components = 0;

    // JLabel fillerLabel;

    this.setPreferredSize(new Dimension(400, 60)); // Size of the header
    this.setBackground(backgroundColor);
    GridLayout layout = new GridLayout(1,1);
    this.setLayout(layout);


    // Add History label above history scroll window
    // JLabel historyText = new JLabel("History");
    // historyText.setPreferredSize(new Dimension(50, 50));
    // historyText.setFont(new Font("BrixSansBlack", Font.ITALIC, 20));
    // historyText.setHorizontalAlignment(JLabel.CENTER); // Align the text to the left
    // historyText.setVerticalAlignment(JLabel.TOP);
    //historyText.setBorder(BorderFactory.createLineBorder(Color.red));
    // this.add(historyText);
    // components++;

    
    // Add main title
    JLabel titleText = new JLabel("SayIt"); // Text of the header
    titleText.setPreferredSize(new Dimension(200, 60));
    titleText.setFont(new Font("BrixSansBlack", Font.ITALIC, 20));
    titleText.setHorizontalAlignment(JLabel.CENTER); // Align the text to the center
    //titleText.setBorder(BorderFactory.createLineBorder(Color.red));
    this.add(titleText); // Add the text to the header
    // components++;

    // Add a filler on the top right box
    // fillerLabel = new JLabel("");
    // this.add(fillerLabel);
    // components++;

    ImageIcon trashCan = new ImageIcon("/Users/rei_crzy/Documents/CSE 110/Final Project/cse-110-project-team-17/trashCan.png");
    // JButton clearAll = new JButton();
    // clearAll.setText("Clear All");
    // clearAll.setPreferredSize(new Dimension(50, 50));
    // clearAll.setFont(new Font("BrixSansBlack", Font.ITALIC, 10));
    // clearAll.setHorizontalAlignment(JButton.CENTER); // Align the text to the center
    // clearAll.setVerticalAlignment(JButton.BOTTOM);
    //clearAll.setBorder(BorderFactory.createLineBorder(Color.red));
    // this.add(clearAll);
    // components++;

    // while (components < size) {
      // fillerLabel = new JLabel("");
      // this.add(fillerLabel);
      // components++;
    // }
    
  }
}

class AppFrame extends JFrame {

  private Header header;
  private Footer footer;
  private IChatGPT chatGPT;
  private ChatList chatList;
  private List list;
  private HistoryWindow historyWindow;
  private IQuestionHandler qHandler;
  private String chat_gpt_answer;
  private JButton askQuestion;
  private JButton stopRecordingButton;

  AppFrame(IQuestionHandler qHandlerInput, IChatGPT ChatGPTInput) {
    this.revalidate();
    this.setSize(800, 600); // 400 width and 600 height
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
    this.setVisible(true); // Make visible

    header = new Header();
    footer = new Footer();
    historyWindow = new HistoryWindow();
    list = historyWindow.getList();
    chatList = new ChatList();
    qHandler = qHandlerInput;
    chatGPT = ChatGPTInput;

    this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
    this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
    this.add(historyWindow, BorderLayout.WEST); // Add history list in left of screen
    this.add(chatList, BorderLayout.CENTER); // Add chat list in middle of footer and title

    askQuestion = footer.getAskQuestion();
    stopRecordingButton = footer.getStopRecordingButton();

    list.setDefault();

    addListeners();
  }

  public void addListeners() {
    askQuestion.addMouseListener(
      new MouseAdapter() {
        @override
        public void mousePressed(MouseEvent e) {
          QuestionButtonHandler();
        }
      }
    );
    stopRecordingButton.addMouseListener(
      new MouseAdapter() {
        @override
        public void mousePressed(MouseEvent e) {
          StopButtonHandler();
        }
      }
    );
  }

  public void QuestionButtonHandler() {
    qHandler.startQuestion();
    askQuestion.setVisible(false);
    stopRecordingButton.setVisible(true);
    chatList.clearChatWindow();
    repaint();
  }

  public void StopButtonHandler() {
    String prompt = qHandler.getQuestion();
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
    //list.updateNumbers(); // Updates the numbers of the tasks
    JButton selectButton = historyQuestion.getDone();
    selectButton.addActionListener(
      (ActionEvent e2) -> {
          SelectButtonHandler(historyQuestion);
        }
    );

    stopRecordingButton.setVisible(false);
    askQuestion.setVisible(true);
  }

  public void SelectButtonHandler(HistoryQuestion historyQuestion) {
    historyQuestion.changeState(); // Change color of task
    //list.updateNumbers(); // Updates the numbers of the tasks
    if (historyQuestion.getState()) {
      for (Component c : list.getListComponents()) {
        if (c instanceof HistoryQuestion) {
          HistoryQuestion currQuestion = (HistoryQuestion) c;
          if (currQuestion.getState()) {
            currQuestion.changeState();
          }
        }
      }

      chatList.clearChatWindow();
      repaint();
      
      historyQuestion.changeState();
      ChatBox historyQuestionBox = 
        new ChatBox("Question", historyQuestion.getQuestionText());
      chatList.add(historyQuestionBox);
      ChatBox historyAnswerBox = 
        new ChatBox("Answer", historyQuestion.getAnswerText());
      chatList.add(historyAnswerBox);
    }
    else {
      chatList.clearChatWindow();
      repaint();
    }
    revalidate(); // Updates the frame
  }

  public Header getHeader() {
    return header;
  }

  public Footer getFooter() {
    return footer;
  }

  public IChatGPT getChatGPT() {
    return chatGPT;
  }

  public ChatList getChatList() {
    return chatList;
  }

  public List getHistoryList() {
    return list;
  }

  public JButton getAskButton() {
    return askQuestion;
  }

  public JButton getStopButton() {
    return stopRecordingButton;
  }

  public boolean getStopButtonVisibility() {
    return stopRecordingButton.isVisible();
  }

  public boolean getAskButtonVisibility() {
    return askQuestion.isVisible();
  }
}

public class SayItAssistant {
  public static void main(String args[]) {
    IQuestionHandler qHandler = new MockQuestion();
    IChatGPT chatGPT = new MockChatGPT();
    new AppFrame(qHandler, chatGPT); // Create the frame
  }
}

@interface override {
}
