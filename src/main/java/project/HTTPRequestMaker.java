package project;

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HTTPRequestMaker {
    String URL;
    String regex;

    // Empty constructor
    public HTTPRequestMaker(String URL, String regex) {
        this.URL = URL;
        this.regex = regex;
    }

    // Method to make a get request to server given index
    public String[] getRequest(String index) {
        String chat_data[] = new String[2];
        // Make "GET" request to server
        try {
            URL url = new URL(URL + "?=" + index);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
            );
            String chat_string = in.readLine();
            String temp_line;
            while ((temp_line = in.readLine()) != null) {
                chat_string = chat_string + "\n" + temp_line;
            }
            String[] temp = chat_string.split(regex);
            if (temp.length == 2) {
                chat_data = temp;
            }
            else {
                System.out.println("Query had incorrect format");
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return chat_data;
    }

    // Method to make a delete request to server given index
    public void deleteRequest(String index) {
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

    // Method to make a post request to server given index
    public void postRequest(String index, String question, String answer) {
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
            out.write(index + regex + question + regex + answer);
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
    }
}
