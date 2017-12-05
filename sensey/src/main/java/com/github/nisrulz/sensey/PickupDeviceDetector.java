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

public class PickupDeviceDetector extends SensorDetector {

    public interface PickupDeviceListener {

        void onDevicePickedUp();

        void onDevicePutDown();
    }

    private final PickupDeviceListener pickupDeviceListener;

    public PickupDeviceDetector(PickupDeviceListener pickupDeviceListener) {
        super(TYPE_ACCELEROMETER);
        this.pickupDeviceListener = pickupDeviceListener;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        double vectorSum = Math.sqrt(x * x + y * y + z * z);
        if (vectorSum > 11) {
            pickupDeviceListener.onDevicePickedUp();
        }
    }
}
