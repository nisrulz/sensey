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

import static com.github.nisrulz.sensey.MovementDetector.MovementListener;
import static com.github.nisrulz.sensey.RotationAngleDetector.RotationAngleListener;
import static com.github.nisrulz.sensey.TiltDirectionDetector.TiltDirectionListener;
import static com.github.nisrulz.sensey.WristTwistDetector.WristTwistListener;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import androidx.annotation.RequiresPermission;
import android.view.MotionEvent;
import com.github.nisrulz.sensey.ChopDetector.ChopListener;
import com.github.nisrulz.sensey.FlipDetector.FlipListener;
import com.github.nisrulz.sensey.LightDetector.LightListener;
import com.github.nisrulz.sensey.OrientationDetector.OrientationListener;
import com.github.nisrulz.sensey.PickupDeviceDetector.PickupDeviceListener;
import com.github.nisrulz.sensey.PinchScaleDetector.PinchScaleListener;
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener;
import com.github.nisrulz.sensey.ScoopDetector.ScoopListener;
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener;
import com.github.nisrulz.sensey.SoundLevelDetector.SoundLevelListener;
import com.github.nisrulz.sensey.TouchTypeDetector.TouchTypListener;
import com.github.nisrulz.sensey.WaveDetector.WaveListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Sensey {

    private static class LazyHolder {

        private static final Sensey INSTANCE = new Sensey();
    }

    public static final int SAMPLING_PERIOD_FASTEST = SensorManager.SENSOR_DELAY_FASTEST;

    public static final int SAMPLING_PERIOD_GAME = SensorManager.SENSOR_DELAY_GAME;

    public static final int SAMPLING_PERIOD_NORMAL = SensorManager.SENSOR_DELAY_NORMAL;

    public static final int SAMPLING_PERIOD_UI = SensorManager.SENSOR_DELAY_UI;

    private final Map<Object, SensorDetector> defaultSensorsMap = new HashMap<>();

    private PinchScaleDetector pinchScaleDetector;

    private int samplingPeriod = SAMPLING_PERIOD_NORMAL;

    private SensorManager sensorManager;

    private SoundLevelDetector soundLevelDetector;

    private TouchTypeDetector touchTypeDetector;

    public static Sensey getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Sensey() {
    }

    public void init(Context context, int samplingPeriod) {
        init(context);
        this.samplingPeriod = samplingPeriod;
    }

    public void init(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void setupDispatchTouchEvent(MotionEvent event) {
        if (touchTypeDetector != null) {
            touchTypeDetector.onTouchEvent(event);
        }

        if (pinchScaleDetector != null) {
            pinchScaleDetector.onTouchEvent(event);
        }
    }

    public void startChopDetection(ChopListener chopListener) {
        startLibrarySensorDetection(new ChopDetector(chopListener), chopListener);
    }

    public void startChopDetection(float threshold, long timeForChopGesture,
            ChopListener chopListener) {
        startLibrarySensorDetection(new ChopDetector(threshold, timeForChopGesture, chopListener),
                chopListener);
    }

    public void startFlipDetection(FlipListener flipListener) {
        startLibrarySensorDetection(new FlipDetector(flipListener), flipListener);
    }

    public void startLightDetection(LightListener lightListener) {
        startLibrarySensorDetection(new LightDetector(lightListener), lightListener);
    }

    public void startLightDetection(float threshold, LightListener lightListener) {
        startLibrarySensorDetection(new LightDetector(threshold, lightListener), lightListener);
    }

    public void startMovementDetection(MovementListener movementListener) {
        startLibrarySensorDetection(new MovementDetector(movementListener), movementListener);
    }

    public void startMovementDetection(float threshold, long timeBeforeDeclaringStationary,
            MovementListener movementListener) {
        startLibrarySensorDetection(
                new MovementDetector(threshold, timeBeforeDeclaringStationary, movementListener),
                movementListener);
    }

    public void startOrientationDetection(OrientationListener orientationListener) {
        startLibrarySensorDetection(new OrientationDetector(orientationListener), orientationListener);
    }

    public void startOrientationDetection(int smoothness, OrientationListener orientationListener) {
        startLibrarySensorDetection(new OrientationDetector(smoothness, orientationListener),
                orientationListener);
    }

    public void startPickupDeviceDetection(PickupDeviceListener pickupDeviceListener) {
        startLibrarySensorDetection(new PickupDeviceDetector(pickupDeviceListener),
                pickupDeviceListener);
    }

    public void startPinchScaleDetection(Context context, PinchScaleListener pinchScaleListener) {
        if (pinchScaleListener != null) {
            pinchScaleDetector = new PinchScaleDetector(context, pinchScaleListener);
        }
    }

    public void startProximityDetection(ProximityListener proximityListener) {
        startLibrarySensorDetection(new ProximityDetector(proximityListener), proximityListener);
    }

    public void startRotationAngleDetection(RotationAngleListener rotationAngleListener) {
        startLibrarySensorDetection(new RotationAngleDetector(rotationAngleListener),
                rotationAngleListener);
    }

    public void startScoopDetection(ScoopListener scoopListener) {
        startLibrarySensorDetection(new ScoopDetector(scoopListener), scoopListener);
    }

    public void startScoopDetection(float threshold, ScoopListener scoopListener) {
        startLibrarySensorDetection(new ScoopDetector(threshold, scoopListener), scoopListener);
    }

    public void startShakeDetection(ShakeListener shakeListener) {
        startLibrarySensorDetection(new ShakeDetector(shakeListener), shakeListener);
    }

    public void startShakeDetection(float threshold, long timeBeforeDeclaringShakeStopped,
            ShakeListener shakeListener) {
        startLibrarySensorDetection(
                new ShakeDetector(threshold, timeBeforeDeclaringShakeStopped, shakeListener),
                shakeListener);
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void startSoundLevelDetection(Context context, SoundLevelListener soundLevelListener) {
        if (soundLevelListener != null && checkPermission(context, Manifest.permission.RECORD_AUDIO)) {
            soundLevelDetector = new SoundLevelDetector(soundLevelListener);
            soundLevelDetector.start();
        } else {
            System.out.println("Permission Required: RECORD_AUDIO");
        }
    }

    public void startStepDetection(Context context, StepListener stepListener, int gender) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && checkHardware(context,
                PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
            startLibrarySensorDetection(new StepDetectorPostKitKat(gender, stepListener), stepListener);
        } else {
            startLibrarySensorDetection(new StepDetectorPreKitKat(gender, stepListener), stepListener);
        }
    }

    public void startTiltDirectionDetection(TiltDirectionListener tiltDirectionListener) {
        startLibrarySensorDetection(new TiltDirectionDetector(tiltDirectionListener),
                tiltDirectionListener);
    }

    public void startTouchTypeDetection(Context context, TouchTypListener touchTypListener) {
        if (touchTypListener != null) {
            touchTypeDetector = new TouchTypeDetector(context, touchTypListener);
        }
    }

    public void startWaveDetection(WaveListener waveListener) {
        startLibrarySensorDetection(new WaveDetector(waveListener), waveListener);
    }

    public void startWaveDetection(float threshold, WaveListener waveListener) {
        startLibrarySensorDetection(new WaveDetector(threshold, waveListener), waveListener);
    }

    public void startWristTwistDetection(WristTwistListener wristTwistListener) {
        startLibrarySensorDetection(new WristTwistDetector(wristTwistListener), wristTwistListener);
    }

    public void startWristTwistDetection(float threshold, long timeForWristTwistGesture,
            WristTwistListener wristTwistListener) {
        startLibrarySensorDetection(
                new WristTwistDetector(threshold, timeForWristTwistGesture, wristTwistListener),
                wristTwistListener);
    }

    public void stop() {
        this.sensorManager = null;
    }

    public void stopChopDetection(ChopListener chopListener) {
        stopLibrarySensorDetection(chopListener);
    }

    public void stopFlipDetection(FlipListener flipListener) {
        stopLibrarySensorDetection(flipListener);
    }

    public void stopLightDetection(LightListener lightListener) {
        stopLibrarySensorDetection(lightListener);
    }

    public void stopMovementDetection(MovementListener movementListener) {
        stopLibrarySensorDetection(movementListener);
    }

    public void stopOrientationDetection(OrientationListener orientationListener) {
        stopLibrarySensorDetection(orientationListener);
    }

    public void stopPickupDeviceDetection(PickupDeviceListener pickupDeviceListener) {
        stopLibrarySensorDetection(pickupDeviceListener);
    }

    public void stopPinchScaleDetection() {
        pinchScaleDetector = null;
    }

    public void stopProximityDetection(ProximityListener proximityListener) {
        stopLibrarySensorDetection(proximityListener);
    }

    public void stopRotationAngleDetection(RotationAngleListener rotationAngleListener) {
        stopLibrarySensorDetection(rotationAngleListener);
    }

    public void stopScoopDetection(ScoopListener scoopListener) {
        stopLibrarySensorDetection(scoopListener);
    }

    public void stopShakeDetection(ShakeListener shakeListener) {
        stopLibrarySensorDetection(shakeListener);
    }

    public void stopSoundLevelDetection() {
        if (soundLevelDetector != null) {
            soundLevelDetector.stop();
        }
        soundLevelDetector = null;
    }

    public void stopStepDetection(StepListener stepListener) {
        stopLibrarySensorDetection(stepListener);
    }

    public void stopTiltDirectionDetection(TiltDirectionListener tiltDirectionListener) {
        stopLibrarySensorDetection(tiltDirectionListener);
    }

    public void stopTouchTypeDetection() {
        touchTypeDetector = null;
    }

    public void stopWaveDetection(WaveListener waveListener) {
        stopLibrarySensorDetection(waveListener);
    }

    public void stopWristTwistDetection(WristTwistListener wristTwistListener) {
        stopLibrarySensorDetection(wristTwistListener);
    }

    boolean checkHardware(Context context, String hardware) {
        return context.getPackageManager().hasSystemFeature(hardware);
    }

    boolean checkPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean areAllSensorsValid(Iterable<Sensor> sensors) {
        for (Sensor sensor : sensors) {
            if (sensor == null) {
                return false;
            }
        }

        return true;
    }

    private Iterable<Sensor> convertTypesToSensors(int... sensorTypes) {
        Collection<Sensor> sensors = new ArrayList<>();
        if (sensorManager != null) {
            for (int sensorType : sensorTypes) {
                sensors.add(sensorManager.getDefaultSensor(sensorType));
            }
        }
        return sensors;
    }

    private void registerDetectorForAllSensors(SensorDetector detector, Iterable<Sensor> sensors) {
        for (Sensor sensor : sensors) {
            sensorManager.registerListener(detector, sensor, samplingPeriod);
        }
    }

    private void startLibrarySensorDetection(SensorDetector detector, Object clientListener) {
        if (!defaultSensorsMap.containsKey(clientListener)) {
            defaultSensorsMap.put(clientListener, detector);
            startSensorDetection(detector);
        }
    }

    private void startSensorDetection(SensorDetector detector) {
        final Iterable<Sensor> sensors = convertTypesToSensors(detector.getSensorTypes());
        if (areAllSensorsValid(sensors)) {
            registerDetectorForAllSensors(detector, sensors);
        }
    }

    private void stopLibrarySensorDetection(Object clientListener) {
        SensorDetector detector = defaultSensorsMap.remove(clientListener);
        stopSensorDetection(detector);
    }

    private void stopSensorDetection(SensorDetector detector) {
        if (detector != null && sensorManager != null) {
            sensorManager.unregisterListener(detector);
        }
    }
}
