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

import static android.hardware.Sensor.TYPE_GYROSCOPE;

import android.hardware.SensorEvent;

public class TiltDirectionDetector extends SensorDetector {

    public interface TiltDirectionListener {

        void onTiltInAxisX(int direction);

        void onTiltInAxisY(int direction);

        void onTiltInAxisZ(int direction);
    }

    public static final int DIRECTION_CLOCKWISE = 0;

    public static final int DIRECTION_ANTICLOCKWISE = 1;

    private final TiltDirectionListener tiltDirectionListener;

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
            tiltDirectionListener.onTiltInAxisX(DIRECTION_ANTICLOCKWISE);
        } else if (x < -0.5f) {
            //clockwise
            tiltDirectionListener.onTiltInAxisX(DIRECTION_CLOCKWISE);
        }

        if (y > 0.5f) {
            // anticlockwise
            tiltDirectionListener.onTiltInAxisY(DIRECTION_ANTICLOCKWISE);
        } else if (y < -0.5f) {
            //clockwise
            tiltDirectionListener.onTiltInAxisY(DIRECTION_CLOCKWISE);
        }

        if (z > 0.5f) {
            // anticlockwise
            tiltDirectionListener.onTiltInAxisZ(DIRECTION_ANTICLOCKWISE);
        } else if (z < -0.5f) {
            //clockwise
            tiltDirectionListener.onTiltInAxisZ(DIRECTION_CLOCKWISE);
        }
    }
}
