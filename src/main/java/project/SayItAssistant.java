/**
 * This code was refactored from the original code found at:
 * https://copyassignment.com/to-do-list-app-in-java/
 */

package project;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

import com.sun.net.httpserver.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import javax.swing.JButton;
import javax.swing.JFrame;

class AppFrame extends JFrame {

  private Header header;
  private Footer footer;
  private IChatGPT chatGPT;
  private ChatList chatList;
  private HistoryList list;
  private HistoryWindow historyWindow;
  private IQuestionHandler qHandler;
  private IAudioHandler audioHandler;
  private JButton askQuestion;
  private JButton stopRecordingButton;
  private JButton clearAll;
  private JButton deleteSelected;
  private HttpServer server;
  private String regex = ";;;";

  public final String URL = "http://localhost:8100/";

  // AppFrame Constructor, starts up HTTP server on localhost and adds GUI components
  // Also uses inputs to set up handlers for accepting and processing spoken questions
  // Adds listeners to buttons
  AppFrame(IQuestionHandler qHandler, IChatGPT chatGPT, IAudioHandler audioHandler) {

    // initialize server port and hostname
    final int SERVER_PORT = 8100;
    final String SERVER_HOSTNAME = "localhost";
    // create a thread pool to handle requests
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)
        Executors.newFixedThreadPool(10);
    
    // create a map to store data
    Map<String, String> data = new HashMap<>();

    try {
      // create a server
      server = HttpServer.create(
        new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
        0
      );
      // Set the context
      server.createContext("/", new SayItHandler(data, regex));

      // Set executor
      server.setExecutor(threadPoolExecutor);

      // Start the server
      server.start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Server started on port " + SERVER_PORT);

    // Sets basic frame GUI properties
    this.revalidate();
    this.setSize(800, 600); // 400 width and 600 height
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
    this.setVisible(true); // Make visible

    // Creates GUI components and updates based on old history
    header = new Header();
    footer = new Footer();
    historyWindow = new HistoryWindow(regex);
    list = historyWindow.getList();
    list.populateOldHistory();
    oldHistoryHandler();
    if (list.getComponentsNum() == 0) {
      list.setDefault();
    }
    chatList = new ChatList();

    // Uses input to set up necessary handlers
    this.qHandler = qHandler;
    this.chatGPT = chatGPT;
    this.audioHandler = audioHandler;

    // Adds initialized GUI components to the frame
    this.add(header, BorderLayout.NORTH); // Add title bar on top of the screen
    this.add(footer, BorderLayout.SOUTH); // Add footer on bottom of the screen
    this.add(historyWindow, BorderLayout.WEST); // Add history list in left of screen
    this.add(chatList, BorderLayout.CENTER); // Add chat list in middle of footer and title

    // Obtains buttons from GUI components for later use
    askQuestion = footer.getAskQuestion();
    stopRecordingButton = footer.getStopRecordingButton();
    clearAll = historyWindow.getHistoryHeader().getClearAll();
    deleteSelected = historyWindow.getHistoryHeader().getdeleteSelected();

    // Adds listeners to the added buttons
    addListeners();
  }

  // Method to add listeners to core buttons, using helper handler methods
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
    clearAll.addMouseListener(
      new MouseAdapter() {
        @override
        public void mousePressed(MouseEvent e) {
          clearAll();
        }
      }
    );
    deleteSelected.addMouseListener(
      new MouseAdapter() {
        @override
        public void mousePressed(MouseEvent e) {
          deleteSelectedHandler();
        }
      }
    );
  }

  // Method to handle clearing all history questions from history list
  public void clearAll() {

    // For each HistoryQuestion, makes a "DELETE" request to server
    for (Component c : list.getComponents()) {
      if (c instanceof HistoryQuestion) {
        HistoryQuestion historyQuestion = (HistoryQuestion) c;
        try {

          // Makes "DELETE" request to server
          String query = historyQuestion.getIndex();
          URL url = new URL(URL + "?=" + query);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("DELETE");
          BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
          );
          String response = in.readLine();
          in.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    // Clears list GUI component and updates frame
    list.removeEverything();
    chatList.removeAll();
    this.revalidate();
  }

  // Method to handle deleting the selected (if any) history question
  public void deleteSelectedHandler() {

    // Iterate through HistoryList, set index to index of selected history question
    // or -1 if none are selected
    String index = "-1";
    for (Component c : list.getComponents()) {
      if (c instanceof HistoryQuestion) {
        HistoryQuestion historyQuestion = (HistoryQuestion) c;
        if (historyQuestion.getState()) {
          index = historyQuestion.getIndex();
        }
      }
    }

    // If an index was found, make "DELETE" request to server
    if (!(index.equals("-1"))) {
      try {
        // Make "DELETE" request to server
        String query = index;
        URL url = new URL(URL + "?=" + query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getInputStream())
        );
        String response = in.readLine();
        in.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    // Delete selected history question GUI component 
    // from list and update frame
    list.deleteSelected();
    chatList.clearChatWindow();
    repaint();
    revalidate();
  }

  // Method to handle starting the recording to ask a question
  public void QuestionButtonHandler() {

    // Start recording
    audioHandler.startRecording();

    // Toggles visibility of the footer buttons and updates frame
    askQuestion.setVisible(false);
    stopRecordingButton.setVisible(true);
    chatList.clearChatWindow();
    repaint();
  }

  // Method to handle stopping the recording and displaying
  // the question and answer in chat window
  public void StopButtonHandler() {

    // Stop recording and get filename of audio file
    String filename = audioHandler.stopRecording();

    // Initialize prompt and answer variables
    String prompt = "";
    String chat_gpt_answer = "";

    // Get prompt from filename
    try {
      prompt = qHandler.getQuestion(filename);
    }
    catch (IOException io_e) {
      throw new RuntimeException("An IO Exception happened while getting question.");
    }

    // Create ChatBox from prompt and add to frame
    ChatBox question = new ChatBox("Question", prompt);
    chatList.add(question);
    revalidate();

    // Get answer from prompt, create ChatBox from answer, and add to frame
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

    // Get index from HistoryList variable
    String count_str = Integer.toString(list.getCount());

    // Post (Index, question + answer) as a pair to HTTP server
    // via "POST" request
    try {
      URL url = new URL(URL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      OutputStreamWriter out = new OutputStreamWriter(
        conn.getOutputStream()
      );
      out.write(count_str + regex + prompt + regex + chat_gpt_answer);
      out.flush();
      out.close();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      String response = in.readLine();
      in.close();

    }
    catch (Exception e) {
      e.printStackTrace();
    }

    // Create new HistoryQuestion and add to prompt
    HistoryQuestion historyQuestion = new HistoryQuestion(count_str, prompt, chat_gpt_answer);
    list.add(historyQuestion); // Add new task to list

    // Add listener to new history question's select button
    JButton selectButton = historyQuestion.getDone();
    selectButton.addActionListener(
      (ActionEvent e2) -> {
          SelectButtonHandler(historyQuestion);
        }
    );

    // Toggle footer button visibility
    stopRecordingButton.setVisible(false);
    askQuestion.setVisible(true);
  }

  // Method to add listener to select button of given history question
  public void SelectButtonHandler(HistoryQuestion historyQuestion) {

    // On selection, toggle select state
    historyQuestion.changeState(); // Change color of task

    // If we are selecting the history question
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

      // Fill ChatList with data from "GET" request to server, using
      // history question's index

      // Initialize variables to hold data to create ChatBox
      String index = historyQuestion.getIndex();
      String chat_string = "";
      String question = "";
      String answer = "";

      // Make "GET" request to server
      try {
        URL url = new URL(URL + "?=" + index);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
          new InputStreamReader(conn.getInputStream())
        );
        chat_string = in.readLine();
        String[] chat_data = chat_string.split(regex);
        question = chat_data[0];
        answer = chat_data[1];
        in.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      // Create ChatBoxes based on output of "GET" request and
      // add them to ChatList
      ChatBox historyQuestionBox = 
        new ChatBox("Question", question);
      chatList.add(historyQuestionBox);
      ChatBox historyAnswerBox = 
        new ChatBox("Answer", answer);
      chatList.add(historyAnswerBox);
    }

    // If we are deselecting the history question
    else {
      // Clear the chat window
      chatList.clearChatWindow();
      repaint();
    }
    revalidate(); // Updates the frame
  }

  // Method to add history questions from previous sessions
  // to HTTP server
  public void oldHistoryHandler() {
    // For all components
    for (Component c : list.getComponents()) {
      // If we are adding a HistoryQuestion
      if (c instanceof HistoryQuestion) {

        // Get history question's data (index, question, answer)
        // and create chat_data (question and answer)
        HistoryQuestion historyQuestion = (HistoryQuestion) c;
        String index = historyQuestion.getIndex();
        String question = historyQuestion.getQuestionText();
        String answer = historyQuestion.getAnswerText();
        String chat_data = question + regex + answer;

        // Make a "POST" request using index and chat_data
        try {
          URL url = new URL(URL);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("POST");
          conn.setDoOutput(true);
          OutputStreamWriter out = new OutputStreamWriter(
                  conn.getOutputStream()
                );
                out.write(index + regex + chat_data);
                out.flush();
                out.close();
          BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
          );
          String response = in.readLine();
          in.close();
    
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        // Add listener to select button for each history question
        JButton SelectButton = historyQuestion.getDone();
        SelectButton.addActionListener(
          (ActionEvent e2) -> {
            SelectButtonHandler(historyQuestion);
          }
        );
      }
    }
    revalidate(); // Update frame
  }

  // Method to get Header object
  public Header getHeader() {
    return header;
  }

  // Method to get Footer object
  public Footer getFooter() {
    return footer;
  }

  // Method to get ChatGPT handler
  public IChatGPT getChatGPT() {
    return chatGPT;
  }

  // Method to get ChatList object
  public ChatList getChatList() {
    return chatList;
  }

  // Method to get HistoryList object
  public HistoryList getHistoryList() {
    return list;
  }

  // Method to get "Ask a Question" button
  public JButton getAskButton() {
    return askQuestion;
  }

  // Method to get "Stop Recording" button
  public JButton getStopButton() {
    return stopRecordingButton;
  }

  // Method to get "Clear All" button
  public JButton getClearButton() {
    return clearAll;
  }

  // Method to get "Delete Selected" button
  public JButton getDeleteButton() {
    return deleteSelected;
  }

  // Method to get visibility of "Stop Recording" button
  public boolean getStopButtonVisibility() {
    return stopRecordingButton.isVisible();
  }

  // Method to get visibility of "Ask a Question" button
  public boolean getAskButtonVisibility() {
    return askQuestion.isVisible();
  }

  // Method to close frame
  public void closeFrame() {
    server.stop(1);
    this.dispose();
  }
}

// Actual SayItAssistant class
public class SayItAssistant {
  public static void main(String args[]) throws IOException {
    // Creates handlers
    IQuestionHandler qHandler = new QuestionHandler();
    IChatGPT chatGPT = new ChatGPT();
    IAudioHandler audioHandler = new AudioHandler();
    
    new AppFrame(qHandler, chatGPT, audioHandler); // Create the frame
  }
}

@interface override {
}
