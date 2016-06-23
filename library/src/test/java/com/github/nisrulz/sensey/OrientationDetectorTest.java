package com.github.nisrulz.sensey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.github.nisrulz.sensey.OrientationDetector.OrientationListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static com.github.nisrulz.sensey.SensorUtils.testAccelerometerEvent;
import static com.github.nisrulz.sensey.SensorUtils.testSensorEvent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Michael Spitsin
 * @since 2016-06-19
 */
@RunWith(RobolectricTestRunner.class)
public class OrientationDetectorTest {

    private OrientationListener mockListener;
    private OrientationDetector testOrientationDetector;

    @Before
    public void setUp() {
        mockListener = mock(OrientationListener.class);
        testOrientationDetector = new OrientationDetector(mockListener);
    }

    @Test
    public void detectNothingForOnlyMagneticEvent() {
        testOrientationDetector.sensorEventListener.onSensorChanged(testMagneticEvent(new float[]{1, 2, 3}));
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectNothingForOnlyAccelerometerEvent() {
        testOrientationDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[]{1, 2, 3}));
        verifyNoMoreInteractions(mockListener);
    }

    @Test
    public void detectTopSideUp() {
        testOrientationDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[]{9.81f, 9.81f, 9.81f}));
        testOrientationDetector.sensorEventListener.onSensorChanged(testMagneticEvent(new float[]{0, 0, 1}));
        verify(mockListener, only()).onTopSideUp();
    }

    @Test
    public void detectBottomSideUp() {
        testOrientationDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[]{-9.81f, -9.81f, -9.81f}));
        testOrientationDetector.sensorEventListener.onSensorChanged(testMagneticEvent(new float[]{0, 0, 1}));
        verify(mockListener, only()).onBottomSideUp();
    }

    @Test
    public void detectRightSideUp() {
        testOrientationDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[]{9.81f, 0, 9.81f}));
        testOrientationDetector.sensorEventListener.onSensorChanged(testMagneticEvent(new float[]{0, 0, 1}));
        verify(mockListener, only()).onRightSideUp();
    }

    @Test
    public void detectLeftSideUp() {
        testOrientationDetector.sensorEventListener.onSensorChanged(testAccelerometerEvent(new float[]{-9.81f, 0, -9.81f}));
        testOrientationDetector.sensorEventListener.onSensorChanged(testMagneticEvent(new float[]{0, 0, 1}));
        verify(mockListener, only()).onLeftSideUp();
    }

    private SensorEvent testMagneticEvent(float[] values) {
        return testSensorEvent(values, Sensor.TYPE_MAGNETIC_FIELD);
    }
}
