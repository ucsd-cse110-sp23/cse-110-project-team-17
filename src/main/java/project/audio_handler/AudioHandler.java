package project.audio_handler;

import javax.sound.sampled.*;
import java.io.*;

public class AudioHandler implements IAudioHandler {

    int count;

    TargetDataLine line;

    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    String filename;
    
    public AudioHandler() {
        count = 0;
    }

    public void startRecording() {
        Thread recordThread = new Thread(() -> {
            startRecordingHelper();
        });
        recordThread.start();
    }

    private void startRecordingHelper() {
        try {
            filename = "project/audio/Recording" + count + ".wav";
            File wavFile = new File(filename);
            while (wavFile.isFile()) {
                count++;
                filename = "project/audio/Recording" + count + ".wav";
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

    public String stopRecording() {
        line.stop();
        line.close();
        String temp = filename;
        filename = "";
        return temp;
    }


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
