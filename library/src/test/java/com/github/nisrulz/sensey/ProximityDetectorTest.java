package com.github.nisrulz.sensey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.github.nisrulz.sensey.ProximityDetector.ProximityListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.nisrulz.sensey.SensorUtils.testSensorEvent;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProximityDetectorTest {

    @Mock private ProximityListener mockListener;

    @Test
    public void detectOnNearWithLuxLessThanDefaultThreshold() {
        testDetector().sensorEventListener.onSensorChanged(testProximityEvent(new float[]{1}));
        verify(mockListener, only()).onNear();
    }

    @Test
    public void detectOnFarWithLuxMoreThanDefaultThreshold() {
        testDetector().sensorEventListener.onSensorChanged(testProximityEvent(new float[]{10}));
        verify(mockListener, only()).onFar();
    }

    @Test
    public void detectOnFarWithLuxEqualsToDefaultThreshold() {
        testDetector().sensorEventListener.onSensorChanged(testProximityEvent(new float[]{3}));
        verify(mockListener, only()).onFar();
    }

//    @Test
//    public void detectOnNearWithLuxLessThanCustomThreshold() {
//        testDetector(9).sensorEventListener.onSensorChanged(testProximityEvent(new float[]{3}));
//        verify(mockListener, only()).onNear();
//    }
//
//    @Test
//    public void detectOnFarWithLuxMoreThanCustomThreshold() {
//        testDetector(9).sensorEventListener.onSensorChanged(testProximityEvent(new float[]{12}));
//        verify(mockListener, only()).onFar();
//    }
//
//    @Test
//    public void detectOnFarWithLuxEqualsToCustomThreshold() {
//        testDetector(9).sensorEventListener.onSensorChanged(testProximityEvent(new float[]{9}));
//        verify(mockListener, only()).onFar();
//    }

    @Test
    public void detectOnFarWithExtraValues() {
        testDetector().sensorEventListener.onSensorChanged(testProximityEvent(new float[]{10, 0, 43, 3, -423}));
        verify(mockListener, only()).onFar();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void exceptionWithEmptyValues() {
        testDetector().sensorEventListener.onSensorChanged(testProximityEvent(new float[]{}));
    }

    private ProximityDetector testDetector() {
        return new ProximityDetector(mockListener);
    }

//    private ProximityDetector testDetector(float threshold) {
//        return new ProximityDetector(threshold, mockListener);
//    }

    private SensorEvent testProximityEvent(float[] values) {
        return testSensorEvent(values, Sensor.TYPE_PROXIMITY);
    }
}
