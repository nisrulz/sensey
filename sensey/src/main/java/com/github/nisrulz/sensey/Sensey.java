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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import com.github.nisrulz.sensey.FlipDetector.FlipListener;
import com.github.nisrulz.sensey.LightDetector.LightListener;
import com.github.nisrulz.sensey.OrientationDetector.OrientationListener;
import com.github.nisrulz.sensey.PinchScaleDetector.PinchScaleListener;
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener;
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener;
import com.github.nisrulz.sensey.SoundLevelDetector.SoundLevelListener;
import com.github.nisrulz.sensey.TouchTypeDetector.TouchTypListener;
import com.github.nisrulz.sensey.WaveDetector.WaveListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Sensey.
 */
public class Sensey {

  /**
   * Map from any of default listeners (
   * {@link FlipListener flipListener},
   * {@link LightListener lightListener},
   * {@link OrientationListener orientationListener}
   * {@link ProximityListener proximityListener}
   * {@link WaveListener waveListner}
   * and {@link ShakeListener shakeListener})
   * to SensorDetectors created by those listeners.
   *
   * This map is needed to hold reference to all started detections <strong>NOT</strong>
   * through {@link Sensey#startSensorDetection(SensorDetector)}, because the last one
   * passes task to hold reference of {@link SensorDetector sensorDetector} to the client
   */
  private final Map<Object, SensorDetector> defaultSensorsMap = new HashMap<>();
  private SensorManager sensorManager;
  private TouchTypeDetector touchTypeDetector;
  private PinchScaleDetector pinchScaleDetector;
  private SoundLevelDetector soundLevelDetector;
  private Context context;

  private Sensey() {
  }

  private static class LazyHolder {
    private static final Sensey INSTANCE = new Sensey();
  }

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static Sensey getInstance() {
    return LazyHolder.INSTANCE;
  }

  /**
   * Init.
   *
   * @param context
   *     the context
   */
  public void init(Context context) {
    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    this.context = context;
  }

  private void startLibrarySensorDetection(SensorDetector detector, Object clientListener) {
    if (!defaultSensorsMap.containsKey(clientListener)) {
      defaultSensorsMap.put(clientListener, detector);
      startSensorDetection(detector);
    }
  }

  /**
   * Start sensor detection.
   *
   * @param detector
   *     the detector
   */
  public void startSensorDetection(SensorDetector detector) {
    final Iterable<Sensor> sensors = convertTypesToSensors(detector.getSensorTypes());
    if (areAllSensorsValid(sensors)) {
      registerDetectorForAllSensors(detector, sensors);
    }
  }

  private Iterable<Sensor> convertTypesToSensors(int... sensorTypes) {
    Collection<Sensor> sensors = new ArrayList<>();
    for (int sensorType : sensorTypes) {
      sensors.add(sensorManager.getDefaultSensor(sensorType));
    }
    return sensors;
  }

  private boolean areAllSensorsValid(Iterable<Sensor> sensors) {
    for (Sensor sensor : sensors) {
      if (sensor == null) {
        return false;
      }
    }

    return true;
  }

  private void registerDetectorForAllSensors(SensorDetector detector, Iterable<Sensor> sensors) {
    for (Sensor sensor : sensors) {
      sensorManager.registerListener(detector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  /**
   * Stop sensor detection.
   *
   * @param detector
   *     the detector
   */
  public void stopSensorDetection(SensorDetector detector) {
    if (detector != null) {
      sensorManager.unregisterListener(detector);
    }
  }

  /**
   * Start shake detection.
   *
   * @param shakeListener
   *     the shake listener
   */
  public void startShakeDetection(ShakeListener shakeListener) {
    startLibrarySensorDetection(new ShakeDetector(shakeListener), shakeListener);
  }

  /**
   * Start shake detection.
   *
   * @param threshold
   *     the threshold
   * @param shakeListener
   *     the shake listener
   */
  public void startShakeDetection(int threshold, ShakeListener shakeListener) {
    startLibrarySensorDetection(new ShakeDetector(threshold, shakeListener), shakeListener);
  }

  /**
   * Stop shake detection.
   *
   * @param shakeListener
   *     the shake listener
   */
  public void stopShakeDetection(ShakeListener shakeListener) {
    stopLibrarySensorDetection(shakeListener);
  }

  private void stopLibrarySensorDetection(Object clientListener) {
    SensorDetector detector = defaultSensorsMap.remove(clientListener);
    stopSensorDetection(detector);
  }

  /**
   * Start light detection.
   *
   * @param lightListener
   *     the light listener
   */
  public void startLightDetection(LightListener lightListener) {
    startLibrarySensorDetection(new LightDetector(lightListener), lightListener);
  }

  /**
   * Start light detection.
   *
   * @param threshold
   *     the threshold
   * @param lightListener
   *     the light listener
   */
  public void startLightDetection(int threshold, LightListener lightListener) {
    startLibrarySensorDetection(new LightDetector(threshold, lightListener), lightListener);
  }

  /**
   * Stop light detection.
   *
   * @param lightListener
   *     the light listener
   */
  public void stopLightDetection(LightListener lightListener) {
    stopLibrarySensorDetection(lightListener);
  }

  /**
   * Start flip detection.
   *
   * @param flipListener
   *     the flip listener
   */
  public void startFlipDetection(FlipListener flipListener) {
    startLibrarySensorDetection(new FlipDetector(flipListener), flipListener);
  }

  /**
   * Stop flip detection.
   *
   * @param flipListener
   *     the flip listener
   */
  public void stopFlipDetection(FlipListener flipListener) {
    stopLibrarySensorDetection(flipListener);
  }

  /**
   * Start orientation detection.
   *
   * @param orientationListener
   *     the orientation listener
   */
  public void startOrientationDetection(OrientationListener orientationListener) {
    startLibrarySensorDetection(new OrientationDetector(orientationListener), orientationListener);
  }

  /**
   * Start orientation detection.
   *
   * @param smoothness
   *     the smoothness
   * @param orientationListener
   *     the orientation listener
   */
  public void startOrientationDetection(int smoothness, OrientationListener orientationListener) {
    startLibrarySensorDetection(new OrientationDetector(smoothness, orientationListener),
        orientationListener);
  }

  /**
   * Stop orientation detection.
   *
   * @param orientationListener
   *     the orientation listener
   */
  public void stopOrientationDetection(OrientationListener orientationListener) {
    stopLibrarySensorDetection(orientationListener);
  }

  /**
   * Start proximity detection.
   *
   * @param proximityListener
   *     the proximity listener
   */
  public void startProximityDetection(ProximityListener proximityListener) {
    startLibrarySensorDetection(new ProximityDetector(proximityListener), proximityListener);
  }

  /**
   * Stop proximity detection.
   *
   * @param proximityListener
   *     the proximity listener
   */
  public void stopProximityDetection(ProximityListener proximityListener) {
    stopLibrarySensorDetection(proximityListener);
  }

  /**
   * Start proximity detection.
   *
   * @param waveListener
   *     the wave listener
   */
  public void startWaveDetection(WaveListener waveListener) {
    startLibrarySensorDetection(new WaveDetector(waveListener), waveListener);
  }

  /**
   * Start proximity detection.
   *
   * @param threshold
   *     the threshold
   * @param waveListener
   *     the wave listener
   */
  public void startWaveDetection(float threshold, WaveListener waveListener) {
    startLibrarySensorDetection(new WaveDetector(threshold, waveListener), waveListener);
  }

  /**
   * Stop proximity detection.
   *
   * @param waveListener
   *     the wave listener
   */
  public void stopWaveDetection(WaveListener waveListener) {
    stopLibrarySensorDetection(waveListener);
  }

  public void startSoundLevelDetection(SoundLevelListener soundLevelListener) {
    if (soundLevelListener != null) {
      soundLevelDetector = new SoundLevelDetector(context, soundLevelListener);
    }
  }

  public void stopSoundLevelDetection(SoundLevelListener soundLevelListener) {
    soundLevelDetector = null;
  }

  /**
   * Start pinch scale detection.
   *
   * @param pinchScaleListener
   *     the pinch scale listener
   */
  public void startPinchScaleDetection(PinchScaleListener pinchScaleListener) {
    if (pinchScaleListener != null) {
      pinchScaleDetector = new PinchScaleDetector(context, pinchScaleListener);
    }
  }

  /**
   * Stop pinch scale detection.
   */
  public void stopPinchScaleDetection() {
    pinchScaleDetector = null;
  }

  /**
   * Start touch type detection.
   *
   * @param touchTypListener
   *     the touch typ listener
   */
  public void startTouchTypeDetection(TouchTypListener touchTypListener) {
    if (touchTypListener != null) {
      touchTypeDetector = new TouchTypeDetector(context, touchTypListener);
    }
  }

  /**
   * Stop touch type detection.
   */
  public void stopTouchTypeDetection() {
    touchTypeDetector = null;
  }

  /**
   * Sets dispatch touch event.
   *
   * @param event
   *     the event
   */
  public void setupDispatchTouchEvent(MotionEvent event) {
    if (touchTypeDetector != null) {
      touchTypeDetector.onTouchEvent(event);
    }

    if (pinchScaleDetector != null) {
      pinchScaleDetector.onTouchEvent(event);
    }
  }
}
