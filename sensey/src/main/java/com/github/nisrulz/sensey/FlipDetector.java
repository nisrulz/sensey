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

/**
 * The type Flip detector.
 */
public class FlipDetector extends SensorDetector {

    /**
     * The interface Flip listener.
     */
    public interface FlipListener {

        /**
         * On face down.
         */
        void onFaceDown();

        /**
         * On face up.
         */
        void onFaceUp();
    }

    private int eventOccurred;

    private final FlipListener flipListener;

    /**
     * Instantiates a new Flip detector.
     *
     * @param flipListener the flip listener
     */
    public FlipDetector(FlipListener flipListener) {
        super(TYPE_ACCELEROMETER);
        this.flipListener = flipListener;
        this.eventOccurred = 0;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        float z = sensorEvent.values[2];
        if (z > 9 && z < 10 && eventOccurred != 1) {
            eventOccurred = 1;
            flipListener.onFaceUp();
        } else if (z > -10 && z < -9 && eventOccurred != 2) {
            eventOccurred = 2;
            flipListener.onFaceDown();
        }
    }
}
