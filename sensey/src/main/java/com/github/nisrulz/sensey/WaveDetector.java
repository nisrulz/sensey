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

import static android.hardware.Sensor.TYPE_PROXIMITY;

import android.hardware.SensorEvent;

public class WaveDetector extends SensorDetector {

    public interface WaveListener {

        void onWave();
    }

    private long lastProximityEventTime = 0;

    private int lastProximityState;

    private final float threshold;

    private final WaveListener waveListener;

    public WaveDetector(WaveListener waveListener) {
        this(1000, waveListener);
    }

    public WaveDetector(float threshold, WaveListener waveListener) {
        super(TYPE_PROXIMITY);
        this.waveListener = waveListener;
        this.threshold = threshold;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        float distance = sensorEvent.values[0];
        int proximityState;
    /*
    The Proximity far.
   */
        int proximityFar = 0;
    /*
    The Proximity near.
   */
        int proximityNear = 1;
        if (distance == 0) {
            proximityState = proximityNear;
        } else {
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
}
