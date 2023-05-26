package project.chat_gpt;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatGPT implements IChatGPT {


    // API Constants
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    // Joseph's API Token = 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
    private static final String MODEL = "text-davinci-003";

    // Empty constructor
    public ChatGPT() {}

    // Method that uses ChatGPT to return an answer to the given prompt
    public String ask(String prompt) throws IOException, InterruptedException {

        // If no prompt was received, display default message
        if (prompt.equals("")) {
            String default_msg = "Sorry, no audio was detected.";
            return default_msg;
        }


        int maxTokens = 100;
        String generatedText = "";

        // Prepares JSON requestBody
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

        // Returns properly formatted answer String
        return generatedText.trim();
    }
}