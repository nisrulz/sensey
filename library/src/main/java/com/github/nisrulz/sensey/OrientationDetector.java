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

public class OrientationDetector {

  private OrientationListener orientationListener;

  void init(OrientationListener orientationListener) {
    this.orientationListener = orientationListener;
  }

  SensorEventListener sensorEventListener = new SensorEventListener() {
    @Override public void onSensorChanged(SensorEvent sensorEvent) {
      if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
        float azimuth = sensorEvent.values[0];
        float pitch = sensorEvent.values[1];
        float roll = sensorEvent.values[2];
        if (pitch < -45 && pitch > -135) {
          orientationListener.onTopSideUp();
        } else if (pitch > 45 && pitch < 135) {
          orientationListener.onBottomSideUp();
        } else if (roll > 45) {
          orientationListener.onRightSideUp();
        } else if (roll < -45) {
          orientationListener.onLeftSideUp();
        }
      }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int i) {

    }
  };

  public interface OrientationListener {
    void onTopSideUp();

    void onBottomSideUp();

    void onRightSideUp();

    void onLeftSideUp();
  }
}

