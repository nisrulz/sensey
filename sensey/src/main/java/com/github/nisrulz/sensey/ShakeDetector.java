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
import android.hardware.SensorManager;

/**
 * The type Shake detector.
 */
public class ShakeDetector extends SensorDetector {

    /**
     * The interface Shake listener.
     */
    public interface ShakeListener {

        /**
         * On shake detected.
         */
        void onShakeDetected();

        void onShakeStopped();
    }

    private boolean isShaking = false;

    private long lastTimeShakeDetected = System.currentTimeMillis();

    private float mAccel;

    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;

    private final ShakeListener shakeListener;

    private final float threshold;

    private final long timeBeforeDeclaringShakeStopped;

    /**
     * Instantiates a new Shake detector.
     *
     * @param shakeListener the shake listener
     */
    public ShakeDetector(ShakeListener shakeListener) {
        this(3f, 1000, shakeListener);
    }

    /**
     * Instantiates a new Shake detector.
     *
     * @param threshold     the threshold
     * @param shakeListener the shake listener
     */
    public ShakeDetector(float threshold, long timeBeforeDeclaringShakeStopped,
            ShakeListener shakeListener) {
        super(TYPE_ACCELEROMETER);
        this.shakeListener = shakeListener;
        this.threshold = threshold;
        this.timeBeforeDeclaringShakeStopped = timeBeforeDeclaringShakeStopped;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        // Shake detection
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        // Make this higher or lower according to how much
        // motion you want to detect
        if (mAccel > threshold) {
            lastTimeShakeDetected = System.currentTimeMillis();
            isShaking = true;
            shakeListener.onShakeDetected();
        } else {
            long timeDelta = (System.currentTimeMillis() - lastTimeShakeDetected);
            if (timeDelta > timeBeforeDeclaringShakeStopped && isShaking) {
                isShaking = false;
                shakeListener.onShakeStopped();
            }
        }
    }
}
