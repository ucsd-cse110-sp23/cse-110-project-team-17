package project;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.question_handler.*;

import java.io.IOException;

public class SayItAssistant {
    public static void main(String args[]) throws IOException {
        // Creates handlers
        IQuestionHandler qHandler = new MockQuestionHandler();
        IChatGPT chatGPT = new MockChatGPT();
        IAudioHandler audioHandler = new MockAudioHandler();
        
        AppHandler appHandler = 
            new AppHandler(qHandler, chatGPT, audioHandler); // Create the app instance
        appHandler.createGUI();        
    }
}
