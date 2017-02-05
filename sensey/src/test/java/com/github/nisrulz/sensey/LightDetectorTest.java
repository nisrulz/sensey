package com.github.nisrulz.sensey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import com.github.nisrulz.sensey.LightDetector.LightListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.github.nisrulz.sensey.SensorUtils.testSensorEvent;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LightDetectorTest {

  @Mock private LightListener mockListener;

  @Test
  public void detectOnDarkWithLuxLessThanDefaultThreshold() {
    testDetector().onSensorChanged(testLightEvent(new float[] { 1 }));
    verify(mockListener, only()).onDark();
  }

  private LightDetector testDetector() {
    return new LightDetector(mockListener);
  }

  private SensorEvent testLightEvent(float[] values) {
    return testSensorEvent(values, Sensor.TYPE_LIGHT);
  }

  @Test
  public void detectOnLightWithLuxMoreThanDefaultThreshold() {
    testDetector().onSensorChanged(testLightEvent(new float[] { 10 }));
    verify(mockListener, only()).onLight();
  }

  @Test
  public void detectOnLightWithLuxEqualsToDefaultThreshold() {
    testDetector().onSensorChanged(testLightEvent(new float[] { 3 }));
    verify(mockListener, only()).onLight();
  }

  @Test
  public void detectOnDarkWithLuxLessThanCustomThreshold() {
    testDetector(9).onSensorChanged(testLightEvent(new float[] { 3 }));
    verify(mockListener, only()).onDark();
  }

  private LightDetector testDetector(float threshold) {
    return new LightDetector(threshold, mockListener);
  }

  @Test
  public void detectOnLightWithLuxMoreThanCustomThreshold() {
    testDetector(9).onSensorChanged(testLightEvent(new float[] { 12 }));
    verify(mockListener, only()).onLight();
  }

  @Test
  public void detectOnLightWithLuxEqualsToCustomThreshold() {
    testDetector(9).onSensorChanged(testLightEvent(new float[] { 9 }));
    verify(mockListener, only()).onLight();
  }

  @Test
  public void detectOnLightWithExtraValues() {
    testDetector().onSensorChanged(testLightEvent(new float[] { 10, 0, 43, 3, -423 }));
    verify(mockListener, only()).onLight();
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void exceptionWithEmptyValues() {
    testDetector().onSensorChanged(testLightEvent(new float[] {}));
  }
}
