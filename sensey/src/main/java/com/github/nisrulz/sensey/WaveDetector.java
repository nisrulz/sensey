/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nisrulz.sensey;

import android.hardware.SensorEvent;

import static android.hardware.Sensor.TYPE_PROXIMITY;

/**
 * The type Wave detector.
 */
public class WaveDetector extends SensorDetector {

  /**
   * The Wave listener.
   */
  private final WaveListener waveListener;
  private final float threshold;
  /**
   * The Last proximity event time.
   */
  private long lastProximityEventTime = 0;
  /**
   * The Proximity far.
   */
  private final int proximityFar = 0;
  /**
   * The Proximity near.
   */
  private final int proximityNear = 1;
  /**
   * The Last proximity state.
   */
  private int lastProximityState;

  /**
   * Instantiates a new Wave detector.
   *
   * @param waveListener
   *     the wave listener
   */
  public WaveDetector(WaveListener waveListener) {
    this(1000, waveListener);
  }

  /**
   * Instantiates a new Wave detector.
   *
   * @param threshold
   *     the threshold
   * @param waveListener
   *     the wave listener
   */
  public WaveDetector(float threshold, WaveListener waveListener) {
    super(TYPE_PROXIMITY);
    this.waveListener = waveListener;
    this.threshold = threshold;
  }

  @Override
  protected void onSensorEvent(SensorEvent sensorEvent) {
    float distance = sensorEvent.values[0];
    int proximityState;
    if (distance == 0) {
      proximityState = proximityNear;
    }
    else {
      proximityState = proximityFar;
    }

    final long now = System.currentTimeMillis();
    final long eventDeltaMillis = now - this.lastProximityEventTime;
    if (eventDeltaMillis < threshold
        && proximityNear == lastProximityState
        && proximityFar == proximityState) {

      // Wave detected
      waveListener.onWave();
    }
    this.lastProximityEventTime = now;
    this.lastProximityState = proximityState;
  }

  /**
   * The interface Wave listener.
   */
  public interface WaveListener {
    /**
     * On wave.
     */
    void onWave();
  }
}
