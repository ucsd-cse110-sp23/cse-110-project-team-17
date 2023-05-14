package project.audio_handler;

public class MockAudioHandler implements IAudioHandler {
    String[] options;
    int count;

    // MockAudioHandler constructor, prepares mocked data
    public MockAudioHandler() {
        count = 3;
        options = new String[4];
        options[0] = "project/dummy_audio/TestRecording0.wav";
        options[1] = "project/dummy_audio/TestRecording1.wav";
        options[2] = "project/dummy_audio/TestRecording2.wav";
        options[3] = "project/dummy_audio/TestRecording3.wav";
    }

    // Method to mimic starting a new recording, switches which 
    // of the mocked data is going to be used as input
    public void startRecording() {
        count = (count + 1) % 4;
    }

    // Method to mimic ending a new recording, returns the filename
    // of the mocked data file
    public String stopRecording() {
        return options[count];
    }
}
