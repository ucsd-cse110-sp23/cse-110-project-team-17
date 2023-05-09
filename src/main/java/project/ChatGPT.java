package project;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPT {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "";
    // Joseph's API Token = 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
    private static final String MODEL = "text-davinci-003";

    public String returnedAnswer() throws IOException, InterruptedException {
        String prompt = "";
        int maxTokens = 100;


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
        HttpResponse<String> response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        // Process the response
        String responseBody = response.body();
        //System.out.println(responseBody);

        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        //String generatedText = choices.getJSONObject(0).getString("text");
        String generatedText = "whatever";

        //System.out.println("ChatGPT Response: " + generatedText);
        return generatedText;
    }

    // public static void main(String[] args) throws IOException, InterruptedException {
    //     String prompt = args[1];
    //     int maxTokens = Integer.parseInt(args[0]);


    //     JSONObject requestBody = new JSONObject();
    //     requestBody.put("model", MODEL);
    //     requestBody.put("prompt", prompt);
    //     requestBody.put("max_tokens", maxTokens);
    //     requestBody.put("temperature", 1.0);


    //     //Create the HTTP client
    //     HttpClient client = HttpClient.newHttpClient();

    //     // Create the request object
    //     HttpRequest request = HttpRequest
    //     .newBuilder()
    //     .uri(URI.create(API_ENDPOINT))
    //     .header("Content-type", "application/json")
    //     .header("Authorization", String.format("Bearer %s",API_KEY))
    //     .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
    //     .build();

    //     // Send the request and receive the response
    //     HttpResponse<String> response = client.send(
    //         request,
    //         HttpResponse.BodyHandlers.ofString()
    //     );

    //     // Process the response
    //     String responseBody = response.body();
    //     //System.out.println(responseBody);

    //     JSONObject responseJson = new JSONObject(responseBody);

    //     JSONArray choices = responseJson.getJSONArray("choices");
    //     //String generatedText = choices.getJSONObject(0).getString("text");
    //     String generatedText = "whatever";

    //     System.out.println("ChatGPT Response: " + generatedText);
    // }
}

