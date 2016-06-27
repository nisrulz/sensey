package com.github.nisrulz.sensey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * @author Michael Spitsin
 * @since 2016-06-26
 */
public abstract class SensorDetector implements SensorEventListener {
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {}

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
