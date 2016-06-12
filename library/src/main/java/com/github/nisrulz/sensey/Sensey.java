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

public class Sensey {

  private SensorManager sensorManager;

  private ShakeDetector shakeDetector;
  private FlipDetector flipDetector;
  private OrientationDetector orientationDetector;
  private ProximityDetector proximityDetector;
  private LightDetector lightDetector;

  private Sensey() {
  }

  private static class LazyHolder {
    private static final Sensey INSTANCE = new Sensey();
  }

  public static Sensey getInstance() {
    return LazyHolder.INSTANCE;
  }

  public void init(Context context) {
    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
  }

  public void startShakeDetection(ShakeDetector.ShakeListener shakeListener) {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
      shakeDetector = new ShakeDetector(shakeListener);
      sensorManager.registerListener(shakeDetector.sensorEventListener,
          sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopShakeDetection() {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
        && shakeDetector != null) {
      sensorManager.unregisterListener(shakeDetector.sensorEventListener);
    }
  }

  public void startLightetection(LightDetector.LightListener lightListener) {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
      lightDetector = new LightDetector(lightListener);
      sensorManager.registerListener(lightDetector.sensorEventListener,
          sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopLightDetection() {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null && lightDetector != null) {
      sensorManager.unregisterListener(lightDetector.sensorEventListener);
    }
  }

  public void startFlipDetection(FlipDetector.FlipListener flipListener) {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
      flipDetector = new FlipDetector(flipListener);
      sensorManager.registerListener(flipDetector.sensorEventListener,
          sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
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
    if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {
      orientationDetector = new OrientationDetector(orientationListener);

      sensorManager.registerListener(orientationDetector.sensorEventListener,
          sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
          SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopOrientationDetection() {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null
        && orientationDetector != null) {
      sensorManager.unregisterListener(orientationDetector.sensorEventListener);
    }
  }

  public void startProximityDetection(ProximityDetector.ProximityListener proximityListener) {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {

      proximityDetector = new ProximityDetector(proximityListener);
      proximityDetector.max =
          sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY).getMaximumRange();

      sensorManager.registerListener(proximityDetector.sensorEventListener,
          sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }
  }

  public void stopProximityDetection() {
    if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null
        && proximityDetector != null) {
      sensorManager.unregisterListener(proximityDetector.sensorEventListener);
    }
  }
}
