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
 * The type Scoop detector.
 */
public class ScoopDetector extends SensorDetector {

    /**
     * The interface Scoop listener.
     */
    public interface ScoopListener {

        /**
         * On scooped.
         */
        void onScooped();
    }

    private final ScoopListener scoopListener;

    private final float threshold;

    /**
     * Instantiates a new Scoop detector.
     *
     * @param scoopListener the scoop listener
     */
    public ScoopDetector(ScoopListener scoopListener) {
        this(15f, scoopListener);
    }

    /**
     * Instantiates a new Scoop detector.
     *
     * @param threshold     the threshold
     * @param scoopListener the scoop listener
     */
    public ScoopDetector(float threshold, ScoopListener scoopListener) {
        super(TYPE_ACCELEROMETER);
        this.scoopListener = scoopListener;
        this.threshold = threshold;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        if (y < (-threshold) && x > threshold && z > threshold) {
            scoopListener.onScooped();
        }
    }
}
