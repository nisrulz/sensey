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
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector {

  private float mAccel;
  private float mAccelCurrent;
  private float mAccelLast;

  private final ShakeListener shakeListener;
  private final int threshold;
  final SensorEventListener sensorEventListener = new SensorEventListener() {
    @Override public void onSensorChanged(SensorEvent sensorEvent) {
      float[] mGravity = sensorEvent.values.clone();
      // Shake detection
      float x = mGravity[0];
      float y = mGravity[1];
      float z = mGravity[2];
      mAccelLast = mAccelCurrent;
      mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
      float delta = mAccelCurrent - mAccelLast;
      mAccel = mAccel * 0.9f + delta;
      // Make this higher or lower according to how much
      // motion you want to detect
      if (mAccel > threshold) {
        shakeListener.onShakeDetected();
      }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int i) {
      // do nothing
    }
  };

  public ShakeDetector(ShakeListener shakeListener) {
    mAccel = 0.00f;
    mAccelCurrent = SensorManager.GRAVITY_EARTH;
    mAccelLast = SensorManager.GRAVITY_EARTH;

    this.shakeListener = shakeListener;
    this.threshold = 3;
  }

  public ShakeDetector(int threshold, ShakeListener shakeListener) {
    mAccel = 0.00f;
    mAccelCurrent = SensorManager.GRAVITY_EARTH;
    mAccelLast = SensorManager.GRAVITY_EARTH;
    this.shakeListener = shakeListener;
    this.threshold = threshold;
  }

  public interface ShakeListener {
    void onShakeDetected();
  }
}
