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

import static android.hardware.Sensor.TYPE_ROTATION_VECTOR;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

/**
 * The type Rotation angle detector.
 */
public class RotationAngleDetector extends SensorDetector {

    /**
     * The interface Rotation angle listener.
     */
    public interface RotationAngleListener {

        /**
         * On rotation.
         *
         * @param angleInAxisX the angle in axis x
         * @param angleInAxisY the angle in axis y
         * @param angleInAxisZ the angle in axis z
         */
        void onRotation(float angleInAxisX, float angleInAxisY, float angleInAxisZ);
    }

    private final RotationAngleListener rotationAngleListener;

    /**
     * Instantiates a new Rotation angle detector.
     *
     * @param rotationAngleListener the rotation angle listener
     */
    public RotationAngleDetector(RotationAngleListener rotationAngleListener) {
        super(TYPE_ROTATION_VECTOR);
        this.rotationAngleListener = rotationAngleListener;
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        // Get rotation matrix
        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

        // Remap coordinate system
        float[] remappedRotationMatrix = new float[16];
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z,
                remappedRotationMatrix);

        // Convert to orientations
        float[] orientations = new float[3];
        SensorManager.getOrientation(remappedRotationMatrix, orientations);

        // Convert values in radian to degrees
        for (int i = 0; i < 3; i++) {
            orientations[i] = (float) (Math.toDegrees(orientations[i]));
        }

        rotationAngleListener.onRotation(orientations[0], orientations[1], orientations[2]);
    }
}
