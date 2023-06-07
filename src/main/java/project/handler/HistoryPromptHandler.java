package project.handler;

import project.gui.HistoryPromptGUI;
import project.prompt_handler.PromptHandler;
import project.*;

public class HistoryPromptHandler {
    String index;
    HistoryPromptGUI historyPromptGUI;
    PromptHandler questionHandler;
    String prompt;
    String answer;
    HTTPRequestMaker httpRequestMaker;
    boolean selected;

    // Constructor, creates a corresponding historyPromptGUI object
    // and initializes with index, question, and answer
    public HistoryPromptHandler(String index, HTTPRequestMaker httpRequestMaker) {
        this.index = index;
        this.httpRequestMaker = httpRequestMaker;
        String[] chat_data = httpRequestMaker.getRequest(index);
        prompt = chat_data[0];
        answer = chat_data[1];
        this.historyPromptGUI = new HistoryPromptGUI(this);
        this.selected = false;
        questionHandler = new PromptHandler();
    }


    // Method to update data if needed
    public void updateData() {
        String[] chat_data = httpRequestMaker.getRequest(index);
        prompt = chat_data[0];
        answer = chat_data[1];
    }

    // Method to get index
    public String getIndex() {
        return index;
    }

    // Method to get question
    public String getPrompt() {
        return prompt;
    }

    // Method to get answer associated with question
    public String getAnswer() {
        return answer;
    }

    // Method to toggle selected state of history question
    public void toggleSelected() {
        historyPromptGUI.toggleSelectedGUI(selected);
        selected = !selected;
    }

    // Method to deselect history question
    public void deselect() {
        historyPromptGUI.deselectGUI();
        selected = false;
    }

    // Method to select history question
    public void select() {
        historyPromptGUI.selectGUI();
        selected = true;
    }

    // Method to test if history question is selected
    public boolean isSelected() {
        return selected;
    }

    // Method to get associated HistoryQuestionGUI object
    public HistoryPromptGUI getHistoryPromptGUI() {
        return historyPromptGUI;
    }

    // Method to get history question as one string, 
    // separated by regex
    public String getString(String regex) {
        String index = this.getIndex();
        String prompt = this.getPrompt();
        String answer = this.getAnswer();
        String fullString = index + regex + prompt + regex + answer;
        return fullString;
    }
}
