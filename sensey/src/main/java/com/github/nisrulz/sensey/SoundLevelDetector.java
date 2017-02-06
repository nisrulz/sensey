package com.github.nisrulz.sensey;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * The type Sound level detector.
 */
public class SoundLevelDetector {
  private static final String LOGTAG = "SoundLevelDetector";

  private SoundLevelListener soundLevelListener;
  private Thread audioRecordingThread = null;

  private final int SAMPLE_RATE;
  private int bufferSize;
  private boolean shouldContinueProcessingAudio;
  private final int audioChannel = AudioFormat.CHANNEL_IN_MONO;
  private final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
  private final int audioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;

  /**
   * Instantiates a new Sound level detector.
   *
   * @param soundLevelListener
   *     the sound level listener
   */
  public SoundLevelDetector(SoundLevelListener soundLevelListener) {
    this.soundLevelListener = soundLevelListener;
    // Get valid sample rate and bufferSize
    SAMPLE_RATE = getValidSampleRates(audioChannel, audioEncoding);
    bufferSize = getValidBufferSize(audioSource, SAMPLE_RATE, audioChannel, audioEncoding);
  }

  /**
   * Start Recording
   */
  void start() {
    if (audioRecordingThread == null) {
      audioRecordingThread = new Thread(audioRecordRunnable);
      audioRecordingThread.start();
    }
    else if (audioRecordingThread.isAlive()) {
      stopThreadAndProcessing();
      audioRecordingThread = new Thread(audioRecordRunnable);
      audioRecordingThread.start();
    }
  }

  private void stopThreadAndProcessing() {
    // Stop audio processing
    shouldContinueProcessingAudio = false;
    // interrupt the thread
    if (audioRecordingThread != null) {
      audioRecordingThread.interrupt();
      audioRecordingThread = null;
    }
  }

  /**
   * Stop Recording
   */
  void stop() {
    stopThreadAndProcessing();
    soundLevelListener = null;
  }

  private final Runnable audioRecordRunnable = new Runnable() {
    @Override
    public void run() {
      // Setup thread priority
      android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

      if (SAMPLE_RATE == 0 || bufferSize == 0) {
        Log.e(LOGTAG, "Invalid SampleRate/BufferSize! AudioRecord cannot be initialized. Exiting!");
        return;
      }


      // If there was an error
      if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
        bufferSize = SAMPLE_RATE * 2;
      }

      short[] audioBuffer = new short[bufferSize / 2];

      AudioRecord audioRecord =
          new AudioRecord(audioSource, SAMPLE_RATE, audioChannel, audioEncoding, bufferSize);

      if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
        Log.e(LOGTAG, "AudioRecord could not be initialized. Exiting!");
        return;
      }

      // start recording
      audioRecord.startRecording();
      shouldContinueProcessingAudio = true;

      long shortsRead = 0;
      while (shouldContinueProcessingAudio) {
        // Read the recorded data from audio buffer
        int numberOfShort = audioRecord.read(audioBuffer, 0, audioBuffer.length);
        shortsRead += numberOfShort;

        double sumLevel = 0;
        for (int i = 0; i < numberOfShort; i++) {
          sumLevel += audioBuffer[i] / 32768.0;
        }
        // calculate the sound level
        double rms = Math.sqrt(Math.abs(sumLevel / numberOfShort));
        float soundLevel = (float) (20.0 * Math.log10(rms));

        // Check that the value is neither NaN nor infinite
        if (!Float.isNaN(soundLevel) && !Float.isInfinite(soundLevel) && shouldContinueProcessingAudio) {
          // only then pass it to the listener
          soundLevelListener.onSoundDetected(soundLevel);
        }
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

  private int getValidSampleRates(int channelConfiguration, int audioEncoding) {
    for (int rate : new int[] {
        8000, 11025, 16000, 22050, 44100, 48000
    }) {  // add the rates you wish to check against
      int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfiguration, audioEncoding);
      if (bufferSize > 0) {
        return rate;
      }
    }
    return 0;
  }

  private int getValidBufferSize(int audioSource, int fs, int channelConfiguration,
      int audioEncoding) {
    for (int bufferSize : new int[] {
        256, 512, 1024, 2048, 4096
    }) {  // add the rates you wish to check against
      AudioRecord audioRecordTemp =
          new AudioRecord(audioSource, fs, channelConfiguration, audioEncoding, bufferSize);
      if (audioRecordTemp != null && audioRecordTemp.getState() == AudioRecord.STATE_INITIALIZED) {
        return bufferSize;
      }
    }
    return 0;
  }

  /**
   * The interface Sound level listener.
   */
  public interface SoundLevelListener {
    /**
     * On sound detected.
     *
     * @param level
     *     the level
     */
    void onSoundDetected(float level);
  }
}