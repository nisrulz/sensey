package com.github.nisrulz.sensey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.github.nisrulz.sensey.OrientationDetector.OrientationListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.nisrulz.sensey.SensorUtils.testAccelerometerEvent;
import static com.github.nisrulz.sensey.SensorUtils.testSensorEvent;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Michael Spitsin
 * @since 2016-06-19
 */
@RunWith(MockitoJUnitRunner.class)
public class OrientationDetectorTest {

    @Mock private OrientationListener mockListener;
    private OrientationDetector testOrientationDetector;

    @Before
    public void setUp() {
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

    private SensorEvent testMagneticEvent(float[] values) {
        return testSensorEvent(values, Sensor.TYPE_MAGNETIC_FIELD);
    }
}
