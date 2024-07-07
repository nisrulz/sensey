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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.lang.reflect.Field;

enum SensorUtils {
    ;

    public static SensorEvent testAccelerometerEvent(final float[] values) {
        return SensorUtils.testSensorEvent(values, Sensor.TYPE_ACCELEROMETER);
    }

    public static SensorEvent testSensorEvent(final float[] values, final int type) {
        final SensorEvent sensorEvent = mock(SensorEvent.class);

        try {
            final Field valuesField = SensorEvent.class.getField("values");
            valuesField.setAccessible(true);
            try {
                valuesField.set(sensorEvent, values);
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (final NoSuchFieldException e) {
            e.printStackTrace();
        }

        sensorEvent.sensor = SensorUtils.testSensor(type);

        return sensorEvent;
    }

    private static Sensor testSensor(final int type) {
        final Sensor sensor = mock(Sensor.class);
        when(sensor.getType()).thenReturn(type);
        return sensor;
    }
}
