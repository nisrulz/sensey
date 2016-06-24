package com.github.nisrulz.sensey;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Michael Spitsin
 * @since 2016-06-18
 */
public class SensorUtils {

    public static SensorEvent testAccelerometerEvent(float[] values) {
        return testSensorEvent(values, Sensor.TYPE_ACCELEROMETER);
    }

    public static SensorEvent testSensorEvent(float[] values, int type) {
        SensorEvent sensorEvent = mock(SensorEvent.class);

        try {
            Field valuesField = SensorEvent.class.getField("values");
            valuesField.setAccessible(true);
            try {
                valuesField.set(sensorEvent, values);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        sensorEvent.sensor = testSensor(type);

        return sensorEvent;
    }

    private static Sensor testSensor(int type) {
        Sensor sensor = mock(Sensor.class);
        when(sensor.getType()).thenReturn(type);
        return sensor;
    }
}
