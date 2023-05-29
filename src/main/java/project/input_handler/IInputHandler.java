package project.input_handler;

import java.io.IOException;

public interface IInputHandler {
    // Method to obtain input from some file
    public String getInput(String filename) throws IOException;
}