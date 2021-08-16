package com.example.khushbakht.grocerjin.audio;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

import static android.R.attr.path;

public class AudioRecorder {

    private MediaRecorder recorder = new MediaRecorder();

    public AudioRecorder(){}

    public void startRecording() throws IOException {

        recorder = new MediaRecorder();
        recorder.reset();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        String filePath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM  + "/abc.mp3"+ File.separator;
        recorder.setOutputFile(filePath); // This is my file path to store data

        recorder.prepare();
        recorder.start();

        // make sure the directory we plan to store the recording in exists
        File directory = new File(Environment
                .getExternalStorageDirectory(), "/audio/");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");

        }

    }

    public void stopRecording() throws IOException {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

}
