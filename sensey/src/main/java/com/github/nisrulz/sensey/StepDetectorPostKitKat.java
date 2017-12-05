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

import static android.hardware.Sensor.TYPE_STEP_COUNTER;

import android.hardware.SensorEvent;

/**
 * The type Step detector post kit kat.
 */
class StepDetectorPostKitKat extends SensorDetector {

    private int baseStepCount = 0;

    private int gender;

    private final StepListener stepListener;

    private int steps = 0;

    private long timeDelta = 0, startTime;

    /**
     * Instantiates a new Step detector post kit kat.
     *
     * @param gender       the gender
     * @param stepListener the step listener
     */
    public StepDetectorPostKitKat(int gender, StepListener stepListener) {
        super(TYPE_STEP_COUNTER);
        this.stepListener = stepListener;
        this.gender = gender;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        if (baseStepCount < 1) {
            // initial value
            baseStepCount = (int) sensorEvent.values[0];
        }

        // Calculate steps taken based on first counter value received.
        steps = (int) sensorEvent.values[0] - baseStepCount;

        // Calculate the distance moved
        float distance = StepDetectorUtil.getDistanceCovered(steps, gender);

        // Calculate the timedelta
        timeDelta = (System.currentTimeMillis() - startTime);
        // reset start time
        startTime = System.currentTimeMillis();

        stepListener.stepInformation(steps, distance, StepDetectorUtil.getStepActivityType(distance, timeDelta));
    }


}
