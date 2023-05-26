package project;

import project.audio_handler.*;
import project.chat_gpt.*;
import project.gui.AppGUI;
import project.question_handler.*;

import java.io.IOException;

public class SayItAssistant {
    public static void main(String args[]) throws IOException {
        // Creates handlers
        IQuestionHandler qHandler = new QuestionHandler();
        IChatGPT chatGPT = new ChatGPT();
        IAudioHandler audioHandler = new AudioHandler();
        
        IAppHandler appHandler = 
            new AppHandler(qHandler, chatGPT, audioHandler); // Create the app instance
        // IAppHandler appHandler2 = new MockAppHandler();
        AppGUI appGUI = new AppGUI(appHandler);
        appHandler.createGUI(appGUI);
    }
}
