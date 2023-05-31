package project.handler;

import project.gui.*;

import java.io.*;
import java.util.Scanner;

public class AutomaticLogInHandler implements AutomaticLoginGUIObserver {
    String filename;

    // Constructor to create the file
    public AutomaticLogInHandler() {
        this.filename = "project/accounts.csv";
        String dir_path = "src/main/java";
        File potential_dir = new File(dir_path);
        if (potential_dir.isDirectory()) {
            filename = dir_path + "/" + filename;
        }
    }


    // Method to return login info
    public String[] getLogInInfo() {
        File accountFile = new File(filename);
        String[] account_array = new String[]{"",""};
        if (!accountFile.isFile()) {
            return account_array;
        }
        else {
            try {
                // Create Scanner to read file
                Scanner csv_scanner = new Scanner(accountFile);
                String username = "";
                String password = "";
                while (csv_scanner.hasNextLine()) {
                    // Parse line and get account info
                    String[] account_info = csv_scanner.nextLine().split(",");
                    if (account_info.length != 2) {
                        csv_scanner.close();
                        return account_array;
                    }
                    username = account_info[0];
                    password = account_info[1];
                }
                csv_scanner.close();
                if (username.equals("") || password.equals("")) {
                    return account_array;
                }
                else {
                    account_array[0] = username;
                    account_array[1] = password;
                    return account_array;
                }
            }
            catch (FileNotFoundException fnfe) {
                throw new RuntimeException("File not found for scanning.");
            }
        }
    }

    // Method to remember login info on this computer
    public void recordLogInInfo(String username, String password) {
        File accountFile = new File(filename);
        try {
            FileWriter file_writer = new FileWriter(accountFile, true);
            file_writer.write(username + "," + password + "\n");
            file_writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException("IO Exception writing account info.");
        }
    }

    // Method to update this observer
    public void update(String username, String password) {
        recordLogInInfo(username, password);
    }
}
