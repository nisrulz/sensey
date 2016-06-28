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

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * The type Proximity detector.
 */
public class ProximityDetector extends SensorDetector {

  private final float threshold;
  private final ProximityListener proximityListener;

  /**
   * Instantiates a new Proximity detector.
   *
   * @param proximityListener the proximity listener
   */
  public ProximityDetector(ProximityListener proximityListener) {
    this(3f, proximityListener);
  }

  /**
   * Instantiates a new Proximity detector.
   *
   * @param threshold the threshold
   * @param proximityListener the proximity listener
   */
  public ProximityDetector(float threshold, ProximityListener proximityListener) {
    this.proximityListener = proximityListener;
    this.threshold = threshold;
  }

  @Override public void onSensorChanged(SensorEvent sensorEvent) {
    if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
      float distance = sensorEvent.values[0];
      if (distance < threshold) {
        proximityListener.onNear();
      } else {
        proximityListener.onFar();
      }
    }
  }

  /**
   * The interface Proximity listener.
   */
  public interface ProximityListener {
    /**
     * On near.
     */
    void onNear();

    /**
     * On far.
     */
    void onFar();
  }
}
