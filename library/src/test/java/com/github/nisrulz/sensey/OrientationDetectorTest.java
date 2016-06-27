package com.github.nisrulz.sensey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import com.github.nisrulz.sensey.OrientationDetector.OrientationListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.github.nisrulz.sensey.SensorUtils.testAccelerometerEvent;
import static com.github.nisrulz.sensey.SensorUtils.testSensorEvent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

//TODO add tests for smoothness > 1
@RunWith(RobolectricTestRunner.class) public class OrientationDetectorTest {

  private OrientationListener mockListener;
  private OrientationDetector testOrientationDetector;

  @Before public void setUp() {
    mockListener = mock(OrientationListener.class);
    testOrientationDetector = new OrientationDetector(mockListener);
  }

  @Test public void detectNothingForOnlyMagneticEvent() {
    testOrientationDetector.onSensorChanged(
        testMagneticEvent(new float[] { 1, 2, 3 }));
    verifyNoMoreInteractions(mockListener);
  }

  @Test public void detectNothingForOnlyAccelerometerEvent() {
    testOrientationDetector.onSensorChanged(
        testAccelerometerEvent(new float[] { 1, 2, 3 }));
    verifyNoMoreInteractions(mockListener);
  }

  @Test public void detectTopSideUp() {
    testOrientationDetector.onSensorChanged(
        testAccelerometerEvent(new float[] { 9.81f, 9.81f, 9.81f }));
    testOrientationDetector.onSensorChanged(
        testMagneticEvent(new float[] { 0, 0, 1 }));
    verify(mockListener, only()).onTopSideUp();
  }

  @Test public void detectBottomSideUp() {
    testOrientationDetector.onSensorChanged(
        testAccelerometerEvent(new float[] { -9.81f, -9.81f, -9.81f }));
    testOrientationDetector.onSensorChanged(
        testMagneticEvent(new float[] { 0, 0, 1 }));
    verify(mockListener, only()).onBottomSideUp();
  }

  @Test public void detectRightSideUp() {
    testOrientationDetector.onSensorChanged(
        testAccelerometerEvent(new float[] { 9.81f, 0, 9.81f }));
    testOrientationDetector.onSensorChanged(
        testMagneticEvent(new float[] { 0, 0, 1 }));
    verify(mockListener, only()).onRightSideUp();
  }

  @Test public void detectLeftSideUp() {
    testOrientationDetector.onSensorChanged(
        testAccelerometerEvent(new float[] { -9.81f, 0, -9.81f }));
    testOrientationDetector.onSensorChanged(
        testMagneticEvent(new float[] { 0, 0, 1 }));
    verify(mockListener, only()).onLeftSideUp();
  }

  private SensorEvent testMagneticEvent(float[] values) {
    return testSensorEvent(values, Sensor.TYPE_MAGNETIC_FIELD);
  }
}
