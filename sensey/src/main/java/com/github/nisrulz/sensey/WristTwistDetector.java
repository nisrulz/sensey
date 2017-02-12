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

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

/**
 * The type Wrist twist detector.
 */
public class WristTwistDetector extends SensorDetector {

  private final WristTwistListener wristTwistListener;
  private final float threshold;
  private final long timeForWristTwistGesture;
  private long lastTimeWristTwistDetected = System.currentTimeMillis();
  private boolean isGestureInProgress = false;

  /**
   * Instantiates a new Wrist twist detector.
   *
   * @param wristTwistListener
   *     the wrist twist listener
   */
  public WristTwistDetector(WristTwistListener wristTwistListener) {
    this(15f, 1000, wristTwistListener);
  }

  /**
   * Instantiates a new Wrist twist detector.
   *
   * @param threshold
   *     the threshold
   * @param timeForWristTwistGesture
   *     the time for wrist twist gesture
   * @param wristTwistListener
   *     the wrist twist listener
   */
  public WristTwistDetector(float threshold, long timeForWristTwistGesture,
      WristTwistListener wristTwistListener) {
    super(TYPE_ACCELEROMETER);
    this.wristTwistListener = wristTwistListener;
    this.threshold = threshold;
    this.timeForWristTwistGesture = timeForWristTwistGesture;
  }

  @Override
  protected void onSensorEvent(SensorEvent sensorEvent) {
    float x = sensorEvent.values[0];
    float y = sensorEvent.values[1];
    float z = sensorEvent.values[2];

    // Make this higher or lower according to how much
    // motion you want to detect
    if (x < -9.8f && y > -3f && z < (-threshold)) {
      lastTimeWristTwistDetected = System.currentTimeMillis();
      isGestureInProgress = true;
    }
    else {
      long timeDelta = (System.currentTimeMillis() - lastTimeWristTwistDetected);
      if (timeDelta > timeForWristTwistGesture && isGestureInProgress) {
        isGestureInProgress = false;
        wristTwistListener.onWristTwist();
      }
    }
  }

  /**
   * The interface Wrist twist listener.
   */
  public interface WristTwistListener {
    /**
     * On wrist twist.
     */
    void onWristTwist();
  }
}
