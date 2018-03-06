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

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

import android.hardware.SensorEvent;

public class ChopDetector extends SensorDetector {

    public interface ChopListener {

        void onChop();
    }

    private final ChopListener chopListener;

    private boolean isGestureInProgress = false;

    private long lastTimeChopDetected = System.currentTimeMillis();

    private final float threshold;

    private final long timeForChopGesture;

    public ChopDetector(ChopListener chopListener) {
        this(35f, 700, chopListener);
    }

    public ChopDetector(float threshold, long timeForChopGesture, ChopListener chopListener) {
        super(TYPE_ACCELEROMETER);
        this.chopListener = chopListener;
        this.threshold = threshold;
        this.timeForChopGesture = timeForChopGesture;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // Make this higher or lower according to how much
        // motion you want to detect
        if (x > threshold && y < (-threshold) && z > threshold) {
            lastTimeChopDetected = System.currentTimeMillis();
            isGestureInProgress = true;
        } else {
            long timeDelta = (System.currentTimeMillis() - lastTimeChopDetected);
            if (timeDelta > timeForChopGesture && isGestureInProgress) {
                isGestureInProgress = false;
                chopListener.onChop();
            }
        }
    }
}
