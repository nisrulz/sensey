package com.github.nisrulz.sensey;

public class SoundLevelDetector {

  private final SoundLevelListener soundLevelListener;

  public SoundLevelDetector(SoundLevelListener soundLevelListener) {
    this.soundLevelListener = soundLevelListener;
  }

  public interface SoundLevelListener {
    void onSoundDetected(float level);
  }
}

