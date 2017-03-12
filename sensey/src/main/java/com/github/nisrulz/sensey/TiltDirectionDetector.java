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

import static android.hardware.Sensor.TYPE_GYROSCOPE;

public class TiltDirectionDetector extends SensorDetector {

  private final TiltDirectionListener tiltDirectionListener;
  private static final int DIRECTION_CLOCKWISE = 0;
  private static final int DIRECTION_ANTICLOCKWISE = 1;

  public TiltDirectionDetector(TiltDirectionListener tiltDirectionListener) {
    super(TYPE_GYROSCOPE);
    this.tiltDirectionListener = tiltDirectionListener;
  }

  @Override
  protected void onSensorEvent(SensorEvent sensorEvent) {
    // Unit = rad/sec, Clockwise = -ve, Anticlockwise = +ve
    float x = sensorEvent.values[0];
    float y = sensorEvent.values[1];
    float z = sensorEvent.values[2];

    if (x > 0.5f) {
      // anticlockwise
      tiltDirectionListener.onAxisX(DIRECTION_ANTICLOCKWISE);
    }
    else if (x < -0.5f) {
      //clockwise
      tiltDirectionListener.onAxisX(DIRECTION_CLOCKWISE);
    }

    if (y > 0.5f) {
      // anticlockwise
      tiltDirectionListener.onAxisY(DIRECTION_ANTICLOCKWISE);
    }
    else if (y < -0.5f) {
      //clockwise
      tiltDirectionListener.onAxisY(DIRECTION_CLOCKWISE);
    }

    if (z > 0.5f) {
      // anticlockwise
      tiltDirectionListener.onAxisZ(DIRECTION_ANTICLOCKWISE);
    }
    else if (z < -0.5f) {
      //clockwise
      tiltDirectionListener.onAxisZ(DIRECTION_CLOCKWISE);
    }
  }

  public interface TiltDirectionListener {
    void onAxisX(int direction);

    void onAxisY(int direction);

    void onAxisZ(int direction);
  }
}
