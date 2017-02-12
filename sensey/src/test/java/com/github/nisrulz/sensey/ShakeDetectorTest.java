package com.github.nisrulz.sensey;

import com.github.nisrulz.sensey.ShakeDetector.ShakeListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
    testDetector().onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 0 }));
    verifyNoMoreInteractions(mockListener);
  }

  private ShakeDetector testDetector() {
    return new ShakeDetector(mockListener);
  }

  @Test
  public void detectShakeWithDoubleGravity() {
    testDetector().onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 2 * 9.81f }));
    verify(mockListener, only()).onShakeDetected();
  }

  @Test
  public void detectShakeWithSeveralGravitySensors() {
    ShakeDetector testDetector = testDetector();
    testDetector.onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 2 * 9.81f }));
    testDetector.onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 2 * -9.81f }));
    verify(mockListener, times(2)).onShakeDetected();
  }

  @Test
  public void detectNothingWithZeroGravityForCustomThreshold() {
    testDetector(10,1000).onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 0 }));
    verifyNoMoreInteractions(mockListener);
  }

  private ShakeDetector testDetector(float threshold, long timeBeforeDeclaringShakeStopped) {
    return new ShakeDetector(threshold,timeBeforeDeclaringShakeStopped, mockListener);
  }

  @Test
  public void detectShakeWithDoubleGravityForCustomThreshold() {
    testDetector(9,1000).onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 2 * 9.81f }));
    verify(mockListener, only()).onShakeDetected();
  }

  @Test
  public void detectShakeWithSeveralGravitySensorsForCustomThreshold() {
    ShakeDetector testDetector = testDetector(9,1000);
    testDetector.onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 2 * 9.81f }));
    testDetector.onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 2 * -9.81f }));
    verify(mockListener, times(1)).onShakeDetected();
  }

  @Test
  public void detectShakeWithSeveralStrongGravitySensorsForCustomThreshold() {
    ShakeDetector testDetector = testDetector(9,1000);
    testDetector.onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 2 * 9.81f }));
    testDetector.onSensorChanged(testAccelerometerEvent(new float[] { 0, 0, 3 * -9.81f }));
    verify(mockListener, times(2)).onShakeDetected();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void exceptionWithLessThanThreeElements() {
    testDetector().onSensorChanged(testAccelerometerEvent(new float[] { 2, 3 }));
  }
}
