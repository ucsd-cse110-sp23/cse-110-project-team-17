package project.question_handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONException;

public class QuestionHandler implements IQuestionHandler {

    // API Constants
    private static String API_ENDPOINT;
    private static String TOKEN;
    private static String MODEL;

    // QuestionHandler Constructor, prepares for use of Whisper API
    public QuestionHandler() {
        API_ENDPOINT = 
            "https://api.openai.com/v1/audio/transcriptions";
        TOKEN = "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";
        MODEL = "whisper-1";
    }

    // Helper method to write a parameter to the output stream in multipart form data format
    private static void writeParamaterToOutputStream(
        OutputStream outputStream,
        String parameterName,
        String parameterValue,
        String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"
            ).getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    // Helper method to write a file to the output stream in multipart form data format
    private static void writeFileToOutputStream(
        OutputStream outputStream,
        File file,
        String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
            (
                "Content-Disposition: form-data; name=\"file\"; filename=\"" +
                file.getName() + 
                "\"\r\n"
            ).getBytes()
        );
        outputStream.write(("Content-Type: audio/wav\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    // Helper method to handle a successful response
    private static String handleSuccessResponse(HttpURLConnection connection)
        throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject responseJson = new JSONObject(response.toString());
        String generatedText = responseJson.getString("text");

        // Print the transcription result
        String result = generatedText;
        return result;
    }

    // Helper method to handle an error response
    private static void handleErrorResponse(HttpURLConnection connection)
        throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        System.out.println("Error Result: " + errorResult);
    }




    // Main method to get question
    public String getQuestion(String filename) throws IOException {
        String FILE_PATH = filename;
        // Create file object from file path
        File file = new File(FILE_PATH);

        if (!(file.isFile())) {
            String errString = "Filepath sent to QuestionHandler wasn't a file.";
            errString = FILE_PATH + "\n" + errString;
            return errString;
        }

        
        // Set up HTTP connection
        URL url = new URL(API_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
            "Content-Type", 
            "multipart/form-data; boundary=" + boundary
        );
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();

        // Write model parameter to request body
        writeParamaterToOutputStream(outputStream, "model", MODEL, boundary);

        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);

        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        // Flush and close output stream
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = connection.getResponseCode();

        // Check response code and handle response accordingly
        String result = "";
        if (responseCode == HttpURLConnection.HTTP_OK) {
            result = handleSuccessResponse(connection);
        } else {
            handleErrorResponse(connection);
            result = "An error occurred in transcribing question.";
        }

        // Disconnect connection
        connection.disconnect();
        return result;
    }

    public String[] getCommand (String prompt) {
        String[] command = prompt.split(" ");
        return command;
    }
}
