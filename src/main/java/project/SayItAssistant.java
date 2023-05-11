/**
 * This code was refactored from the original code found at:
 * https://copyassignment.com/to-do-list-app-in-java/
 */

package project;

import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.io.IOException;

// import javax.swing.ImageIcon;
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

  public HistoryList getHistoryList() {
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
