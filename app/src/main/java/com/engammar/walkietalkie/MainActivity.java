package com.engammar.walkietalkie;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private AudioRecord recorder;
    private AudioTrack player;
    private boolean isRecording = false;
    private int bufferSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button talkButton = new Button(this);
        talkButton.setText("اضغط وتكلم");
        setContentView(talkButton);

        bufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );

        recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
        );

        player = new AudioTrack(
                android.media.AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM
        );

        requestPermissions();

        talkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startTalking();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                stopTalking();
            }
            return true;
        });
    }

    private void startTalking() {
        isRecording = true;
        recorder.startRecording();
        player.play();

        new Thread(() -> {
            byte[] buffer = new byte[bufferSize];
            while (isRecording) {
                recorder.read(buffer, 0, buffer.length);
                player.write(buffer, 0, buffer.length);
            }
        }).start();
    }

    private void stopTalking() {
        isRecording = false;
        recorder.stop();
        player.stop();
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1
            );
        }
    }
}
