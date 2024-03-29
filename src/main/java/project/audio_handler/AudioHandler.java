package project.audio_handler;

// Uses Java's Sound API to record audio
import javax.sound.sampled.*;
import java.io.*;

public class AudioHandler implements IAudioHandler {

    int count;

    TargetDataLine line;

    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    String filename;
    
    // AudioHandler constructor
    public AudioHandler() {
        count = 0;
    }

    // Method to start recording audio, uses thread to not freeze GUI
    public void startRecording() {
        Thread recordThread = new Thread(() -> {
            startRecordingHelper();
        });
        recordThread.start();
    }

    // Helper method to record audio and convert to .wav file
    private void startRecordingHelper() {
        try {
            String audio_folder_path = "project/audio";
            String dir_path = "src/main/java";
            File potential_dir = new File(dir_path);
            if (potential_dir.isDirectory()) {
                audio_folder_path = dir_path + "/" + audio_folder_path;
            }
            File audio_dir = new File(audio_folder_path);
            System.out.println(System.getProperty("user.dir"));
            if (!(audio_dir.isDirectory())) {
                System.out.println("Made directory.");
                audio_dir.mkdir();
            }
            filename = audio_folder_path + "/Recording" + count + ".wav";
            File wavFile = new File(filename);
            while (wavFile.isFile()) {
                count++;
                filename = audio_folder_path + "/Recording" + count + ".wav";
                wavFile = new File(filename);
            }
            count++;
            AudioFormat format = this.getFormatHelper();
            DataLine.Info info = 
                new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            AudioInputStream ais = new AudioInputStream(line);

            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException luex) {
            luex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    // Method to stop recording audio and return filename of audio file
    public String stopRecording() {
        line.stop();
        line.close();
        String temp = filename;
        filename = "";
        return temp;
    }

    // Helper method to format audio
    private AudioFormat getFormatHelper() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate,
            sampleSizeInBits, channels, signed, bigEndian);
        return format;
    }
}
