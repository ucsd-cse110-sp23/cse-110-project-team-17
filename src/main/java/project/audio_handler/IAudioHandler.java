package project.audio_handler;

public interface IAudioHandler {
    // Method to start recording audio
    public void startRecording();
    // Method to stop recording audio, returns filename of audio file
    public String stopRecording();
}
