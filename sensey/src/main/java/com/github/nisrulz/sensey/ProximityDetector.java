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

public class ProximityDetector extends SensorDetector {

    public interface ProximityListener {

        void onFar();

        void onNear();
    }

    private final ProximityListener proximityListener;

    public ProximityDetector(ProximityListener proximityListener) {
        super(TYPE_PROXIMITY);
        this.proximityListener = proximityListener;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        float distance = sensorEvent.values[0];
        float maxRange = sensorEvent.sensor.getMaximumRange();

        if (distance < maxRange) {
            proximityListener.onNear();
        } else {
            proximityListener.onFar();
        }
    }
}
