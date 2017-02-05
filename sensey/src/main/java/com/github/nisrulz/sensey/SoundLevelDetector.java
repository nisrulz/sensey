package com.github.nisrulz.sensey;

import android.content.Context;

public class SoundLevelDetector {

  private final SoundLevelListener soundLevelListener;

  public SoundLevelDetector(Context context, SoundLevelListener soundLevelListener) {
    this.soundLevelListener = soundLevelListener;
  }

  public interface SoundLevelListener {
    void onSoundDetected(float level);
  }
}