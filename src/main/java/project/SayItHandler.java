package project;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class SayItHandler implements HttpHandler {
    private final Map<String, String> data;
    
    public SayItHandler(Map<String, String> data) {
        this.data = data;
    }

    // Method to handle requests
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    // Helper method to handle "GET" requests
    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            String chat_answer = data.get(value); // Retrieve data from hashmap
            if (chat_answer != null) {
                response = chat_answer;
                // System.out.println("Queried for " + value + " and found " + chat_answer);
            } else {
                response = "No data found for " + value;
            }
        }
        return response;
    }

    // Helper method to handle "POST" requests
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String[] chat_data = postData.split(",");
        String index = chat_data[0];
        String question = chat_data[1];
        String answer = chat_data[2];
        String chat_string = question + "," + answer;

        // Store data in hashmap
        data.put(index, chat_string);

        String response = "Posted entry {" + index + ", " + chat_string + "}";
        // System.out.println(response);
        scanner.close();

        return response;
    }

    // Helper method to handle "PUT" requests
    private String handlePut(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String[] chat_data = postData.split(",");
        String index = chat_data[0];
        String question = chat_data[1];
        String answer = chat_data[2];
        String chat_string = question + "," + answer;


        String response;

        if (data.get(index) != null) {
            response = "Updated entry {" + index + ", " +  
            chat_string + "} (previous string: " + data.get(index) + ")";
        }
        else {
            response = "Added entry {" + index + ", " + chat_string + "}";
        }

        // Store data in hashmap
        data.put(index, chat_string);

        
        // System.out.println(response);
        scanner.close();

        return response;
    }

    // Helper method to handle "DELETE" requests
    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            String chat_answer = data.get(value); // Retrieve data from hashmap
            if (chat_answer != null) {
                response = "Deleted entry {" + value + ", " + chat_answer + "}";
                System.out.println(response);
                data.remove(value);
            } else {
                response = "No data found for " + value;
            }
        }
        return response;
    }
}
