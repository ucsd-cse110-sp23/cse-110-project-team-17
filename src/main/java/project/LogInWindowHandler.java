package project;

import java.util.*;

import project.gui.LogInWindowGUI;

public class LogInWindowHandler {
    LogInWindowGUI logInWindow;
    //Vector<HistoryQuestionHandler> historyList;
    Map<String, String> dbMap;
    HTTPRequestMaker httpRequestMaker;

    // Constructor, initializes filepath and history list GUI component
    public LogInWindowHandler() {
        this.logInWindow = new LogInWindowGUI(this);
        this.dbMap = DBCreate.readUserInformation();
        // users = new HashMap<String, String>();
        // try {
        //     FileReader file = new FileReader("project/information.txt");
        //     BufferedReader br = new BufferedReader(file);
        //     String st;  
        //     while ((st = br.readLine()) != null) {
        //         String[] information = st.split(",");
        //         //System.out.println();
        //         users.put(information[0], information[1]);
        //     }
        //     br.close();
        //     //file.close();
        // }
        // catch(Exception e) {
        //     e.printStackTrace();
        // }
    }

    // Create an account for new users
    public boolean createAccount(String username, String password) {
        if (verifyUsername(username, password)) {
            return false;
        }
        if (username.equals("") || password.equals("")) {
            return false;
        }
        // users.put(username, password);
        // try {
        //     File filePath = new File("project/information.txt");
        //     FileWriter file = new FileWriter(filePath);
        //     for(Map.Entry<String,String> entry : users.entrySet()) {
        //         file.write(entry.getKey() + ',' + entry.getValue() + '\n');
        //         //file.flush();
        //     }
        //     //file.flush();
        //     file.close();
        //   }
        // catch(Exception e) {
        //     e.printStackTrace();
        // }
        DBCreate.createUser(username, password);
        return true;
    }

    // Login check username and password match
    public boolean logIn(String username, String password) {
        if (verifyPassword(username, password)) {
            return true;
        }
        return false;
    }

    // Use "GET" request from server to check if username has been taken
    public boolean verifyUsername(String username, String password) {
        return dbMap.containsKey(username);
    }

    // Use "GET" request from server to check the password matched withe the
    // corresponding username
    public boolean verifyPassword(String username, String password) {
        if(dbMap.containsKey(username) && (dbMap.get(username).equals(password)))
            return true;
        return false;
    }
    
    // Method to get associated LogInWindow GUI object
    public LogInWindowGUI getLogInWindowGUI() {
        return this.logInWindow;
    }

}
