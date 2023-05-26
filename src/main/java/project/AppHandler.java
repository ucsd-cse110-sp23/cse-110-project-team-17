package project;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;
import project.gui.*;

import com.sun.net.httpserver.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import java.io.IOException;

public class AppHandler {

    IAudioHandler audioHandler;
    IChatGPT chatGPT;
    IQuestionHandler questionHandler;
    private HttpServer server;
    private String regex = ";;;";
    HistoryListHandler historyListHandler;
    HTTPRequestMaker httpRequestMaker;
    public final String URL = "http://localhost:8100/";
    AppGUI appGUI;
    LogInWindowHandler loginWindowHandler;


    // Constructor, initializes handlers and adds listeners
    // Also creates associated app GUI object
    public AppHandler(IQuestionHandler questionHandler, 
            IChatGPT chatGPT, IAudioHandler audioHandler) {
        
        // Initialize handlers
        this.audioHandler = audioHandler;
        this.chatGPT = chatGPT;
        this.questionHandler = questionHandler;
        this.httpRequestMaker = new HTTPRequestMaker(URL, regex);
        this.historyListHandler = new HistoryListHandler(regex, httpRequestMaker);
        this.loginWindowHandler = new LogInWindowHandler(httpRequestMaker);

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
    }


    // Method to create and attach GUI app component
    public void createGUI() {
        // Create GUI object
        this.appGUI = new AppGUI(this);
        
        // Populate old list as necessary
        historyListHandler.populateOldHistory();
        oldHistoryHandler();
    }

    // Method to return HistoryListHandler element
    public HistoryListHandler getHistoryList() {
        return this.historyListHandler;
    }

    // Method to return LogInWIndow Handler element
    public LogInWindowHandler getLogInWindowHandler() {
        return this.loginWindowHandler;
    }

    // Method to start recording to get question
    public void startRecording() {
        // Start recording
        audioHandler.startRecording();
    }

    // Method to stop recording and receive answer
    public void stopRecording() {

        // Get index from HistoryList variable
        String count_str = (historyListHandler.getCount());
        
        // Stop recording and get filename of audio file
        String filename = audioHandler.stopRecording();

        // Initialize prompt and answer variables
        String prompt = "";
        String chat_gpt_answer = "";

        // Get prompt from filename
        try {
            prompt = questionHandler.getQuestion(filename);
        }
        catch (IOException io_e) {
            throw new RuntimeException("An IO Exception happened while getting question.");
        }

        // Get answer from prompt
        try {
            chat_gpt_answer = chatGPT.ask(prompt);
        }
        catch (IOException io_e) {
            throw new RuntimeException("An IO Exception happened on click.");
        }
        catch (InterruptedException int_e) {
            throw new RuntimeException("An Interruption Exception happened on click.");
        }

        // Post (Index, question + answer) as a pair to HTTP server
        // via "POST" request
        httpRequestMaker.postRequest(count_str, prompt, chat_gpt_answer);

        // Create new HistoryQuestion and add to prompt
        HistoryQuestionHandler historyQuestion = 
            new HistoryQuestionHandler(count_str, httpRequestMaker);
        historyListHandler.add(historyQuestion, false); // Add new task to list

        // Make the created history question selectable in history list
        appGUI.makeSelectable(historyQuestion.getHistoryQuestionGUI());

        // Display the new history question in chat window
        display(prompt, chat_gpt_answer);
    }

    // Method to handle selecting a history button
    public void selectQuestion(HistoryQuestionHandler historyQuestionHandler) {
        // Obtain current state of question
        boolean selected = historyQuestionHandler.isSelected();

        // If deselecting
        if (selected) {
            historyQuestionHandler.deselect();
            clearChat(); // clear window
        }
        // If selecting
        else {
            for (HistoryQuestionHandler hqh : historyListHandler.getHistoryList()) {
                hqh.deselect();
            }
            historyQuestionHandler.select();
            
            // Display question and answer
            String question = historyQuestionHandler.getQuestion();
            String answer = historyQuestionHandler.getAnswer();
            display(question, answer);
        }
    }

    // Method to clear all history from list
    public void clearAll() {
        historyListHandler.removeEverything();
    }

    // Method to delete selected question, if any
    public void deleteSelected() {
        historyListHandler.deleteSelected();
    }

    // Method to add listeners to select buttons from old history questions
    public void oldHistoryHandler() {
        // For all history questions
        for (HistoryQuestionHandler hqh : historyListHandler.getHistoryList()) {
            appGUI.makeSelectable(hqh.getHistoryQuestionGUI());
        }
    }

    // Method to display a propmt and answer in chat window
    public void display(String question, String answer) {
        appGUI.display(question, answer);
    }

    // Method to clear chat window
    public void clearChat() {
        appGUI.clearChat();
    }

    // Method to get ChatGPT handler
    public IChatGPT getChatGPT() {
        return chatGPT;
    }

    // Method to close server
    public void stopServer() {
        server.stop(1);
    }

    // Method to close app
    public void closeApp() {
        appGUI.closeFrame();
    }

    // Method to get associated GUI object
    public AppGUI getAppGUI() {
        return appGUI;
    }

    // Method to get HTTPRequestMaker object
    public HTTPRequestMaker getRequestMaker() {
        return httpRequestMaker;
    }
}