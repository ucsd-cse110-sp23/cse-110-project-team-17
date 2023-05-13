package project.audio_handler;

public class MockAudioHandler implements IAudioHandler {
    String[] options;
    int count;

    public MockAudioHandler() {
        count = 1;
        options = new String[2];
        options[0] = "project/dummy_audio/TestRecording0.wav";
        options[1] = "project/dummy_audio/TestRecording1.wav";
    }

    public void startRecording() {
        count = (count + 1) % 2;
    }

    public String stopRecording() {
        return options[count];
    }
}
