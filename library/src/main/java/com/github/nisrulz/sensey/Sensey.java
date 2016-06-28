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
import java.util.ArrayList;
import java.util.Collection;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_LIGHT;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_PROXIMITY;

public class Sensey {

  private SensorManager sensorManager;

  private ShakeDetector shakeDetector;
  private FlipDetector flipDetector;
  private OrientationDetector orientationDetector;
  private ProximityDetector proximityDetector;
  private LightDetector lightDetector;
  private TouchTypeDetector touchTypeDetector;
  private PinchScaleDetector pinchScaleDetector;
  private Context context;

  private Sensey() {
  }

  public static Sensey getInstance() {
    return LazyHolder.INSTANCE;
  }

  public void init(Context context) {
    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    this.context = context;
  }

  public void startShakeDetection(ShakeDetector.ShakeListener shakeListener) {
    startShakeDetection(new ShakeDetector(shakeListener));
  }

  public void startShakeDetection(int threshold, ShakeDetector.ShakeListener shakeListener) {
    startShakeDetection(new ShakeDetector(threshold, shakeListener));
  }

  private void startShakeDetection(ShakeDetector detector) {
    shakeDetector = detector;
    startSensorDetection(detector, TYPE_ACCELEROMETER);
  }

  public void stopShakeDetection() {
    stopSensorDetection(shakeDetector);
  }

  public void startLightDetection(LightDetector.LightListener lightListener) {
    startLightDetection(new LightDetector(lightListener));
  }

  public void startLightDetection(int threshold, LightDetector.LightListener lightListener) {
    startLightDetection(new LightDetector(threshold, lightListener));
  }

  private void startLightDetection(LightDetector detector) {
    lightDetector = detector;
    startSensorDetection(detector, TYPE_LIGHT);
  }

  public void stopLightDetection() {
    stopSensorDetection(lightDetector);
  }

  public void startFlipDetection(FlipDetector.FlipListener flipListener) {
    flipDetector = new FlipDetector(flipListener);
    startSensorDetection(flipDetector, TYPE_ACCELEROMETER);
  }

  public void stopFlipDetection() {
    stopSensorDetection(flipDetector);
  }

  public void startOrientationDetection(
      OrientationDetector.OrientationListener orientationListener) {
    startOrientationDetection(new OrientationDetector(orientationListener));
  }

  public void startOrientationDetection(int smoothness,
      OrientationDetector.OrientationListener orientationListener) {
    startOrientationDetection(new OrientationDetector(smoothness, orientationListener));
  }

  private void startOrientationDetection(OrientationDetector detector) {
    orientationDetector = detector;
    startSensorDetection(detector, TYPE_ACCELEROMETER, TYPE_MAGNETIC_FIELD);
  }

  public void stopOrientationDetection() {
    stopSensorDetection(orientationDetector);
  }

  public void startProximityDetection(ProximityDetector.ProximityListener proximityListener) {
    startProximityDetection(new ProximityDetector(proximityListener));
  }

  public void startProximityDetection(float threshold,
      ProximityDetector.ProximityListener proximityListener) {
    startProximityDetection(new ProximityDetector(threshold, proximityListener));
  }

  private void startProximityDetection(ProximityDetector detector) {
    proximityDetector = detector;
    startSensorDetection(detector, TYPE_PROXIMITY);
  }

  public void stopProximityDetection() {
    stopSensorDetection(proximityDetector);
  }

  public void startSensorDetection(SensorDetector detector, int... sensorTypes) {
    final Iterable<Sensor> sensors = convertTypesToSensors(sensorTypes);
    if (areAllSensorsValid(sensors)) {
      registerDetectorForAllSensors(detector, sensors);
    }
  }

  public void stopSensorDetection(SensorDetector detector) {
    if (detector != null) {
      sensorManager.unregisterListener(detector);
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

  public void startPinchScaleDetection(PinchScaleDetector.PinchScaleListener pinchScaleListener) {
    if (pinchScaleListener != null) {
      pinchScaleDetector = new PinchScaleDetector(context, pinchScaleListener);
    }
  }

  public void stopPinchScaleDetection() {
    pinchScaleDetector = null;
  }

  public void startTouchTypeDetection(TouchTypeDetector.TouchTypListener touchTypListener) {
    if (touchTypListener != null) {
      touchTypeDetector = new TouchTypeDetector(context, touchTypListener);
    }
  }

  public void stopTouchTypeDetection() {
    touchTypeDetector = null;
  }

  public void setupDispatchTouchEvent(MotionEvent event) {
    if (touchTypeDetector != null) {
      touchTypeDetector.onTouchEvent(event);
    }

    if (pinchScaleDetector != null) {
      pinchScaleDetector.onTouchEvent(event);
    }
  }

  private static class LazyHolder {
    private static final Sensey INSTANCE = new Sensey();
  }
}
