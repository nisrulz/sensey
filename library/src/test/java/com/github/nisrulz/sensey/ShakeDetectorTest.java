package com.github.nisrulz.sensey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.github.nisrulz.sensey.ShakeDetector.ShakeListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.nisrulz.sensey.SensorUtils.testAccelerometerEvent;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@RunWith(MockitoJUnitRunner.class)
public class ShakeDetectorTest {

    @Mock private ShakeListener mockListener;

    @Test
    public void detectNothingWithZeroGravity() {
        testDetector().sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 0}));
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectShakeWithDoubleGravity() {
        testDetector().sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 2 * 9.81f}));
        verify(mockListener, only()).onShakeDetected();
    }

    @Test
    public void detectShakeWithSeveralGravitySensors() {
        ShakeDetector testDetector = testDetector();
        testDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 2 * 9.81f}));
        testDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 2 * -9.81f}));
        verify(mockListener, times(2)).onShakeDetected();
    }

    @Test
    public void detectNothingWithZeroGravityForCustomThreshold() {
        testDetector(10).sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 0}));
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectShakeWithDoubleGravityForCustomThreshold() {
        testDetector(9).sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 2 * 9.81f}));
        verify(mockListener, only()).onShakeDetected();
    }

    @Test
    public void detectShakeWithSeveralGravitySensorsForCustomThreshold() {
        ShakeDetector testDetector = testDetector(9);
        testDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 2 * 9.81f}));
        testDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 2 * -9.81f}));
        verify(mockListener, times(1)).onShakeDetected();
    }

    @Test
    public void detectShakeWithSeveralStrongGravitySensorsForCustomThreshold() {
        ShakeDetector testDetector = testDetector(9);
        testDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 2 * 9.81f}));
        testDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {0, 0, 3 * -9.81f}));
        verify(mockListener, times(2)).onShakeDetected();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void exceptionWithLessThanThreeElements() {
        testDetector().sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[] {2, 3}));
    }

    private ShakeDetector testDetector() {
        return new ShakeDetector(mockListener);
    }

    private ShakeDetector testDetector(int threshold) {
        return new ShakeDetector(threshold, mockListener);
    }
}
