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
 * The type Movement detector.
 */
public class MovementDetector extends SensorDetector {

    /**
     * The interface Movement listener.
     */
    public interface MovementListener {

        /**
         * On movement.
         */
        void onMovement();

        /**
         * On stationary.
         */
        void onStationary();
    }

    private boolean isMoving = false;

    private long lastTimeMovementDetected = System.currentTimeMillis();

    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;

    private final MovementListener movementListener;

    private final float threshold;

    private final long timeBeforeDeclaringStationary;

    /**
     * Instantiates a new Movement detector.
     *
     * @param movementListener the movement listener
     */
    public MovementDetector(MovementListener movementListener) {
        this(0.3f, 5000, movementListener);
    }

    /**
     * Instantiates a new Movement detector.
     *
     * @param threshold                     the threshold
     * @param timeBeforeDeclaringStationary the time before declaring stationary
     * @param movementListener              the movement listener
     */
    public MovementDetector(float threshold, long timeBeforeDeclaringStationary,
            MovementListener movementListener) {
        super(TYPE_ACCELEROMETER);
        this.movementListener = movementListener;
        this.threshold = threshold;
        this.timeBeforeDeclaringStationary = timeBeforeDeclaringStationary;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

    /* Logic:
    If there is no external force on the device, vector sum of accelerometer sensor values
    will be only gravity. If there is a change in vector sum of gravity, then there is a force.
    If this force is significant, you can assume device is moving.
    If vector sum is equal to gravity with +/- threshold its stable lying on table.
     */
        float mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = Math.abs(mAccelCurrent - mAccelLast);

        // Make this higher or lower according to how much
        // motion you want to detect
        if (delta > threshold) {
            lastTimeMovementDetected = System.currentTimeMillis();
            isMoving = true;
            movementListener.onMovement();
        } else {
            long timeDelta = (System.currentTimeMillis() - lastTimeMovementDetected);
            if (timeDelta > timeBeforeDeclaringStationary && isMoving) {
                isMoving = false;
                movementListener.onStationary();
            }
        }
    }
}
