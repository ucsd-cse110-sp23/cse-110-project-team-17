package project;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.gui.*;
import project.handler.*;
import project.prompt_handler.*;

import com.sun.net.httpserver.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import java.io.IOException;

public class AppHandler implements IAppHandler {

    IAudioHandler audioHandler;
    IChatGPT chatGPT;
    IPromptHandler promptHandler;
    private HttpServer server;
    private String regex = ";;;";
    HistoryListHandler historyListHandler;
    HTTPRequestMaker httpRequestMaker;
    public final String URL = "http://localhost:8100/";
    AppGUI appGUI;
    LogInWindowHandler loginWindowHandler;
    AutomaticLogInHandler alHandler;
    setupEmailHandler setupEmailHandler;
    sendEmailHandler sendEmailHandler;


    // Constructor, initializes handlers and adds listeners
    // Also creates associated app GUI object
    public AppHandler(IPromptHandler promptHandler, 
            IChatGPT chatGPT, IAudioHandler audioHandler) {
        
        // Initialize handlers
        this.audioHandler = audioHandler;
        this.chatGPT = chatGPT;
        this.promptHandler = promptHandler;
        this.httpRequestMaker = new HTTPRequestMaker(URL, regex);
        this.historyListHandler = new HistoryListHandler(regex, httpRequestMaker);
        this.loginWindowHandler = new LogInWindowHandler();
        this.alHandler = new AutomaticLogInHandler();
        this.setupEmailHandler = new setupEmailHandler();
        this.sendEmailHandler = new sendEmailHandler();

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
    public void createGUI(AppGUI appGUI) {
        // Create GUI object
        this.appGUI = appGUI;
        this.appGUI.beginLogIn();
    }

    // Method to start recording to get question
    public void startRecording() {
        // Start recording
        audioHandler.startRecording();
    }

    // Method to stop recording and receive answer
    public void stopRecording() {

        // Initialize prompt and command variables
        String prompt = "";
        String command;
        
        // Stop recording and get filename of audio file
        String filename = audioHandler.stopRecording();

        // Get prompt from filename
        try {
            prompt = promptHandler.getPrompt(filename);
            command = promptHandler.getCommand(prompt);
        }
        catch (IOException io_e) {
            throw new RuntimeException("An IO Exception happened while getting question.");
        }

        handleCommand(prompt, command);
    }

    // Helper method to handle cases for each prompt
    public void handleCommand(String prompt, String command) {

        // Get index from HistoryList variable
        String count_str = (historyListHandler.getCount());

        // Initialize answer variable
        String chat_gpt_answer = "";

        // Initialize history question
        HistoryPromptHandler historyPrompt;

        switch (command) {
            case "Question":

                // Deselect any selected questions
                for (HistoryPromptHandler hqh : historyListHandler.getHistoryList()) {
                    hqh.deselect();
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

                // Create new historyPrompt and add to prompt
                historyPrompt = 
                    new HistoryPromptHandler(count_str, httpRequestMaker);
                historyListHandler.add(historyPrompt, false); // Add new task to list

                // Make the created history question selectable in history list
                appGUI.makeSelectable(historyPrompt.getHistoryPromptGUI());

                selectPrompt(historyPrompt);

                // Display the new history question in chat window
                display(prompt, chat_gpt_answer);
                break;

            case "Delete":
                //call delete
                deleteSelected();

                // Post (Index, question + answer) as a pair to HTTP server
                // via "POST" request
                // int count_str_updated = Integer.parseInt(count_str) -1;
                // httpRequestMaker.postRequest(String.valueOf(count_str_updated), prompt, "Deleted");

                // Create new historyPrompt and add to prompt
                // historyPrompt = 
                //     new historyPromptHandler(String.valueOf(count_str_updated), httpRequestMaker);
                // historyListHandler.add(historyPrompt, false); // Add new task to list

                // Make the created history question selectable in history list
                // appGUI.makeSelectable(historyPrompt.gethistoryPromptGUI());

                // Display the new history question in chat window
                // display(prompt, chat_gpt_answer);


                break;
            case "Clear":
                //call clear
                clearAll();

                // Post (Index, question + answer) as a pair to HTTP server
                // via "POST" request
                // httpRequestMaker.postRequest("0", prompt, "Deleted");

                // Create new historyPrompt and add to prompt
                // historyPrompt = 
                //     new historyPromptHandler("0", httpRequestMaker);
                // historyListHandler.add(historyPrompt, false); // Add new task to list

                // Make the created history question selectable in history list
                // appGUI.makeSelectable(historyPrompt.gethistoryPromptGUI());

                // Display the new history question in chat window
                // display(prompt, chat_gpt_answer);
                break;
            
            case "Setup email":
                setupEmail();
                break; 
                
            case "Create email":
                // Deselect any selected questions
                for (HistoryPromptHandler hqh : historyListHandler.getHistoryList()) {
                    hqh.deselect();
                }
                try {
                    // chat box ui is not displaying the whole email, problem can be either size of chat box
                    // or the trim() in ChatGPT.java
                    chat_gpt_answer = chatGPT.ask(prompt); 
                    // [Your name] : length = 11
                    if (chat_gpt_answer.toUpperCase().contains("YOUR NAME")) {
                        chat_gpt_answer = chat_gpt_answer.substring(0, chat_gpt_answer.length() - 11);
                    }
                    else if (chat_gpt_answer.toUpperCase().contains("NAME")) {
                        chat_gpt_answer = chat_gpt_answer.substring(0, chat_gpt_answer.length()-6);
                    }
                    Map<String, String> accountEmail = DBCreate.readEmailInformation(historyListHandler.getUsername());
                    String firstName = accountEmail.get("displayName_id");
                    // Adding first name of user at the end of email
                    chat_gpt_answer = chat_gpt_answer +  firstName;
                }
                catch (IOException io_e) {
                    throw new RuntimeException("An IO Exception happened on click.");
                }
                catch (InterruptedException int_e) {
                    throw new RuntimeException("An Interruption Exception happened on click.");
                }


                System.out.println(chat_gpt_answer);

                // Post (Index, question + answer) as a pair to HTTP server
                // via "POST" request
                httpRequestMaker.postRequest(count_str, prompt, chat_gpt_answer);

                // Create new historyPrompt and add to prompt
                historyPrompt = 
                    new HistoryPromptHandler(count_str, httpRequestMaker);
                historyListHandler.add(historyPrompt, false); // Add new task to list

                // Make the created history question selectable in history list
                appGUI.makeSelectable(historyPrompt.getHistoryPromptGUI());

                selectPrompt(historyPrompt);

                // Display the new history question in chat window
                display(prompt, chat_gpt_answer);
                break;

            case "Send email":
                sendEmail(prompt, count_str);
                break;
                

            default:
                display(prompt, "Unable to parse command, available commands are " +
                    "Question, Delete, Clear, Setup Email, Create Email, and Send Email");
                break;

        }
    }

    public void setupEmail() {
        appGUI.beginSetupEmail();
    }

    // Method to handle selecting a history button
    public void selectPrompt(HistoryPromptHandler historyPromptHandler) {
        // Obtain current state of question
        boolean selected = historyPromptHandler.isSelected();

        // If deselecting
        if (selected) {
            historyPromptHandler.deselect();
            clearChat(); // clear window
        }
        // If selecting
        else {
            for (HistoryPromptHandler hqh : historyListHandler.getHistoryList()) {
                hqh.deselect();
            }
            historyPromptHandler.select();
            
            // Display question and answer
            String question = historyPromptHandler.getPrompt();
            String answer = historyPromptHandler.getAnswer();
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
        for (HistoryPromptHandler hqh : historyListHandler.getHistoryList()) {
            if (hqh == null) {
                System.out.println("hqh was null.");
            }
            if (appGUI == null) {
                System.out.println("appGUI was null.");
            }
            appGUI.makeSelectable(hqh.getHistoryPromptGUI());
        }
    }

    // Method to display a propmt and answer in chat window
    public void display(String question, String answer) {
        appGUI.display(question, answer);
    }

    // Method that automatically logs in if possible
    // and returns whether or not you can autologin on this computer
    public void autoLogin() {
        String[] login_info = this.alHandler.getLogInInfo();
        if (login_info.length != 2) {
            return;
        }
        String username = login_info[0];
        String password = login_info[1];
        if (username.equals("") || password.equals("")) {
            return;
        }
        else {
            historyListHandler.setUsername(username);
            setupEmailHandler.setUsername(username);
            sendEmailHandler.setUsername(username);
            // Populate old list as necessary
            historyListHandler.populateOldHistory();
            oldHistoryHandler();
            return;
        }
    }

    // Method to return whether or not you can autologin on this computer
    public boolean canAutoLogin() {
        String[] login_info = this.alHandler.getLogInInfo();
        if (login_info.length != 2) {
            return false;
        }
        String username = login_info[0];
        String password = login_info[1];
        if (username.equals("") || password.equals("")) {
            return false;
        }
        else {
            return true;
        }
    }

    // Method that logs in if possible and returns whether or not
    // the login attempt was valid
    public boolean LogIn(String username, String password) {

        boolean verify = getLogInWindowHandler().verifyPassword(username, password);
        if (verify) {
            historyListHandler.setUsername(username);
            setupEmailHandler.setUsername(username);
            sendEmailHandler.setUsername(username);
            // Populate old list as necessary
            historyListHandler.populateOldHistory();
            oldHistoryHandler();
            return true;
        }
        else {
            return false;
        }
    }

    // Method to handle sending an email
    public void sendEmail(String prompt, String count_str) {
        
        String body = historyListHandler.getEmailBody();
        String subject = historyListHandler.getEmailSubject();
        String result;
        if (body.equals("No message selected.\n") || 
                subject.equals("No message selected.\n")) {
            result = "No email selected.";
        }

        else {
            String emailRecipient = "No email found";
            if (prompt.contains("to") && prompt.length() > 14) {
                String emailRecipientString = prompt.substring(14);
                emailRecipientString = emailRecipientString.toLowerCase();
                if (emailRecipientString.contains(" ")) {
                    String[] emailAddressParts = emailRecipientString.split(" ");
                    emailRecipientString = "";
                    for (int i = 0; i < emailAddressParts.length; i++) {
                        if (emailAddressParts[i].equals("at")) {
                            emailRecipientString = emailRecipientString + "@";
                        }
                        else if (emailAddressParts[i].equals("dot")) {
                            emailRecipientString = emailRecipientString + ".";
                        }
                        else if (emailAddressParts[i].equals("underscore")) {
                            emailRecipientString = emailRecipientString + "_";
                        }
                        else {
                            emailRecipientString = 
                                emailRecipientString + emailAddressParts[i];
                        }
                        
                    }
                    int lastIndex = emailRecipientString.length() - 1;
                    if (emailRecipientString.substring(lastIndex).equals(".")) {
                        emailRecipientString = emailRecipientString.substring(0, lastIndex);
                    }

                }
                emailRecipient = emailRecipientString;
            }
            
            result = sendEmailHandler.sendEmailHelper(emailRecipient,
                subject, body);
        }
        

        // Deselect any selected questions
        for (HistoryPromptHandler hqh : historyListHandler.getHistoryList()) {
            hqh.deselect();
        }

        // Initialize history question
        HistoryPromptHandler historyPrompt;

        // Post (Index, question + answer) as a pair to HTTP server
        // via "POST" request
        httpRequestMaker.postRequest(count_str, prompt, result);

        // Create new historyPrompt and add to prompt
        historyPrompt = 
            new HistoryPromptHandler(count_str, httpRequestMaker);
        historyListHandler.add(historyPrompt, false); // Add new task to list

        // Make the created history question selectable in history list
        appGUI.makeSelectable(historyPrompt.getHistoryPromptGUI());

        selectPrompt(historyPrompt);

        // Display the new history question in chat window
        display(prompt, result);

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

    // Method to return HistoryListHandler element
    public HistoryListHandler getHistoryList() {
        return this.historyListHandler;
    }

    // Method to return LogInWIndow Handler element
    public LogInWindowHandler getLogInWindowHandler() {
        return this.loginWindowHandler;
    }

    public setupEmailHandler getSetupEmailHandler() {
        return this.setupEmailHandler;
    }
    
    // Method to get automatic login handler
    public AutomaticLogInHandler getAutomaticLogInHandler() {
        return this.alHandler;
    }
}
