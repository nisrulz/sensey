package com.github.nisrulz.sensey;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundLevelDetector {
  private static final String LOGTAG = "SoundLevelDetector";

  private final SoundLevelListener soundLevelListener;
  private final int SAMPLE_RATE = 44100;
  private AudioRecord audioRecord;
  private int bufferSize;
  private boolean shouldContinueProcessingAudio;
  private Thread audioRecordingThread = null;

  public SoundLevelDetector(SoundLevelListener soundLevelListener) {
    this.soundLevelListener = soundLevelListener;

    initAudioRecord();
  }

  void initAudioRecord() {
    int audioChannel = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    int audioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;

    bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, audioChannel, audioEncoding);
    audioRecord =
        new AudioRecord(audioSource, SAMPLE_RATE, audioChannel, audioEncoding, bufferSize);
  }

  void start() {
    if (audioRecordingThread == null) {
      audioRecordingThread = new Thread(audioRecordRunnable);
      audioRecordingThread.start();
    }
    else if (audioRecordingThread.isAlive()) {
      stop();
      audioRecordingThread = new Thread(audioRecordRunnable);
      audioRecordingThread.start();
    }
  }

  void stop() {
    shouldContinueProcessingAudio = false;
    if (audioRecordingThread != null) {
      audioRecordingThread.interrupt();
      audioRecordingThread = null;
    }
  }

  private Runnable audioRecordRunnable = new Runnable() {
    @Override
    public void run() {
      // Setup thread priority
      android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
      // If there was an error
      if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
        bufferSize = SAMPLE_RATE * 2;
      }

      short[] audioBuffer = new short[bufferSize / 2];

      if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
        Log.e(LOGTAG, "Audio Record can't initialize!");
        return;
      }

      //
      audioRecord.startRecording();
      shouldContinueProcessingAudio = true;

      long shortsRead = 0;
      while (shouldContinueProcessingAudio) {
        // Read the recorded data from audio buffer
        int numberOfShort = audioRecord.read(audioBuffer, 0, audioBuffer.length);
        shortsRead += numberOfShort;

        double sumLevel = 0;
        for (int i = 0; i < numberOfShort; i++) {
          sumLevel += audioBuffer[i];
        }
        // calculate the sound level
        float soundLevel = (float) Math.abs((sumLevel / numberOfShort));
        // pass it to the listener
        soundLevelListener.onSoundDetected(soundLevel);
      }

      // stop recording and release the microphone
      try {
        if (audioRecord != null) {
          audioRecord.stop();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (audioRecord != null) {
          audioRecord.release();
          audioRecord = null;
        }
      }
    }
  };

  public interface SoundLevelListener {
    void onSoundDetected(float level);
  }
}