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

public class Sensey {

  private SensorManager sensorManager;

  private ShakeDetector shakeDetector;
  private FlipDetector flipDetector;
  private OrientationDetector orientationDetector;
  private ProximityDetector proximityDetector;
  private LightDetector lightDetector;
  private TouchTypeDetector touchTypeDetector;
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

  public void startShakeDetection(int threshold, ShakeDetector.ShakeListener shakeListener) {
    final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    if (sensor != null) {
      shakeDetector = new ShakeDetector(threshold, shakeListener);
      sensorManager.registerListener(shakeDetector.sensorEventListener, sensor,
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void startShakeDetection(ShakeDetector.ShakeListener shakeListener) {
    final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    if (sensor != null) {
      shakeDetector = new ShakeDetector(shakeListener);
      sensorManager.registerListener(shakeDetector.sensorEventListener, sensor,
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopShakeDetection() {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
        && shakeDetector != null) {
      sensorManager.unregisterListener(shakeDetector.sensorEventListener);
    }
  }

  public void startLightDetection(LightDetector.LightListener lightListener) {
    final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    if (sensor != null) {
      lightDetector = new LightDetector(lightListener);
      sensorManager.registerListener(lightDetector.sensorEventListener, sensor,
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void startLightDetection(int threshold, LightDetector.LightListener lightListener) {
    final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    if (sensor != null) {
      lightDetector = new LightDetector(threshold, lightListener);
      sensorManager.registerListener(lightDetector.sensorEventListener, sensor,
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopLightDetection() {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null && lightDetector != null) {
      sensorManager.unregisterListener(lightDetector.sensorEventListener);
    }
  }

  public void startFlipDetection(FlipDetector.FlipListener flipListener) {
    final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    if (sensor != null) {
      flipDetector = new FlipDetector(flipListener);
      sensorManager.registerListener(flipDetector.sensorEventListener, sensor,
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopFlipDetection() {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null && flipDetector != null) {
      sensorManager.unregisterListener(flipDetector.sensorEventListener);
    }
  }

  public void startOrientationDetection(
      OrientationDetector.OrientationListener orientationListener) {

    Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    if (accelerometer != null && magnetometer != null) {
      orientationDetector = new OrientationDetector(orientationListener);
      sensorManager.registerListener(orientationDetector.sensorEventListener, accelerometer,
          SensorManager.SENSOR_DELAY_NORMAL);
      sensorManager.registerListener(orientationDetector.sensorEventListener, magnetometer,
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopOrientationDetection() {
    if (sensorManager != null && orientationDetector != null) {
      sensorManager.unregisterListener(orientationDetector.sensorEventListener);
    }
  }

  public void startProximityDetection(ProximityDetector.ProximityListener proximityListener) {
    final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    if (sensor != null) {
      proximityDetector = new ProximityDetector(proximityListener);
      sensorManager.registerListener(proximityDetector.sensorEventListener, sensor,
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopProximityDetection() {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null
        && proximityDetector != null) {
      sensorManager.unregisterListener(proximityDetector.sensorEventListener);
    }
  }

  public void startTouchTypeDetection(TouchTypeDetector.TouchTypListener touchTypListener) {
    if (touchTypListener != null) {
      touchTypeDetector = new TouchTypeDetector(context, touchTypListener);
    }
  }

  public void setupDispatchTouchEvent(MotionEvent event) {
    if (touchTypeDetector != null) {
      touchTypeDetector.onTouchEvent(event);
    }
  }

  public void stopTouchTypeDetection() {
    touchTypeDetector = null;
  }

  private static class LazyHolder {
    private static final Sensey INSTANCE = new Sensey();
  }
}
