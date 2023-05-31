package project.handler;

import project.gui.HistoryQuestionGUI;

import project.*;

public class HistoryQuestionHandler {
    String index;
    HistoryQuestionGUI historyQuestionGUI;
    String question;
    String answer;
    HTTPRequestMaker httpRequestMaker;
    boolean selected;

    // Constructor, creates a corresponding HistoryQuestionGUI object
    // and initializes with index, question, and answer
    public HistoryQuestionHandler(String index, HTTPRequestMaker httpRequestMaker) {
        this.index = index;
        this.httpRequestMaker = httpRequestMaker;
        String[] chat_data = httpRequestMaker.getRequest(index);
        question = chat_data[0];
        answer = chat_data[1];
        this.historyQuestionGUI = new HistoryQuestionGUI(this);
        this.selected = false;
    }


    // Method to update data if needed
    public void updateData() {
        String[] chat_data = httpRequestMaker.getRequest(index);
        question = chat_data[0];
        answer = chat_data[1];
    }

    // Method to get index
    public String getIndex() {
        return index;
    }

    // Method to get question
    public String getQuestion() {
        return question;
    }

    // Method to get answer associated with question
    public String getAnswer() {
        return answer;
    }

    // Method to toggle selected state of history question
    public void toggleSelected() {
        historyQuestionGUI.toggleSelectedGUI(selected);
        selected = !selected;
    }

    // Method to deselect history question
    public void deselect() {
        historyQuestionGUI.deselectGUI();
        selected = false;
    }

    // Method to select history question
    public void select() {
        historyQuestionGUI.selectGUI();
        selected = true;
    }

    // Method to test if history question is selected
    public boolean isSelected() {
        return selected;
    }

    // Method to get associated HistoryQuestionGUI object
    public HistoryQuestionGUI getHistoryQuestionGUI() {
        return historyQuestionGUI;
    }
}
