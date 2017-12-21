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
import android.support.annotation.RequiresPermission;
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

/**
 * The type Sensey.
 */
public class Sensey {

    private static class LazyHolder {

        private static final Sensey INSTANCE = new Sensey();
    }

    /**
     * The constant SAMPLING_PERIOD_FASTEST.
     */
    public static final int SAMPLING_PERIOD_FASTEST = SensorManager.SENSOR_DELAY_FASTEST;

    /**
     * The constant SAMPLING_PERIOD_GAME.
     */
    public static final int SAMPLING_PERIOD_GAME = SensorManager.SENSOR_DELAY_GAME;

    /**
     * The constant SAMPLING_PERIOD_NORMAL.
     */
    public static final int SAMPLING_PERIOD_NORMAL = SensorManager.SENSOR_DELAY_NORMAL;

    /**
     * The constant SAMPLING_PERIOD_UI.
     */
    public static final int SAMPLING_PERIOD_UI = SensorManager.SENSOR_DELAY_UI;

    /**
     * Map from any of default listeners (
     * {@link FlipListener flipListener},
     * {@link LightListener lightListener},
     * {@link OrientationListener orientationListener}
     * {@link ProximityListener proximityListener}
     * {@link ChopListener chopListener}
     * {@link MovementListener movementListner}
     * {@link WristTwistListener wristTwistListner}
     * and {@link ShakeListener shakeListener})
     * to SensorDetectors created by those listeners.
     *
     * This map is needed to hold reference to all started detections <strong>NOT</strong>
     * through {@link Sensey#startSensorDetection(SensorDetector)}, because the last one
     * passes task to hold reference of {@link SensorDetector sensorDetector} to the client
     */

    private final Map<Object, SensorDetector> defaultSensorsMap = new HashMap<>();

    private PinchScaleDetector pinchScaleDetector;

    private int samplingPeriod = SAMPLING_PERIOD_NORMAL;

    private SensorManager sensorManager;

    private SoundLevelDetector soundLevelDetector;

    private TouchTypeDetector touchTypeDetector;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Sensey getInstance() {
        return LazyHolder.INSTANCE;
    }

    private Sensey() {
    }

    /**
     * Init the lib
     *
     * @param context        the context
     * @param samplingPeriod the sampling period
     */
    public void init(Context context, int samplingPeriod) {
        init(context);
        this.samplingPeriod = samplingPeriod;
    }

    /**
     * Init the lib
     *
     * @param context the context
     */
    public void init(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    /**
     * Sets dispatch touch event.
     *
     * @param event the event
     */
    public void setupDispatchTouchEvent(MotionEvent event) {
        if (touchTypeDetector != null) {
            touchTypeDetector.onTouchEvent(event);
        }

        if (pinchScaleDetector != null) {
            pinchScaleDetector.onTouchEvent(event);
        }
    }

    /**
     * Start chop detection.
     *
     * @param chopListener the chop listener
     */
    public void startChopDetection(ChopListener chopListener) {
        startLibrarySensorDetection(new ChopDetector(chopListener), chopListener);
    }

    /**
     * Start chop detection.
     *
     * @param threshold          the threshold
     * @param timeForChopGesture the time for chop gesture
     * @param chopListener       the chop listener
     */
    public void startChopDetection(float threshold, long timeForChopGesture,
            ChopListener chopListener) {
        startLibrarySensorDetection(new ChopDetector(threshold, timeForChopGesture, chopListener),
                chopListener);
    }

    /**
     * Start flip detection.
     *
     * @param flipListener the flip listener
     */
    public void startFlipDetection(FlipListener flipListener) {
        startLibrarySensorDetection(new FlipDetector(flipListener), flipListener);
    }

    /**
     * Start light detection.
     *
     * @param lightListener the light listener
     */
    public void startLightDetection(LightListener lightListener) {
        startLibrarySensorDetection(new LightDetector(lightListener), lightListener);
    }

    /**
     * Start light detection.
     *
     * @param threshold     the threshold
     * @param lightListener the light listener
     */
    public void startLightDetection(float threshold, LightListener lightListener) {
        startLibrarySensorDetection(new LightDetector(threshold, lightListener), lightListener);
    }

    /**
     * Start movement detection.
     *
     * @param movementListener the movement listener
     */
    public void startMovementDetection(MovementListener movementListener) {
        startLibrarySensorDetection(new MovementDetector(movementListener), movementListener);
    }

    /**
     * Start movement detection.
     *
     * @param threshold                     the threshold
     * @param timeBeforeDeclaringStationary the time before declaring stationary
     * @param movementListener              the movement listener
     */
    public void startMovementDetection(float threshold, long timeBeforeDeclaringStationary,
            MovementListener movementListener) {
        startLibrarySensorDetection(
                new MovementDetector(threshold, timeBeforeDeclaringStationary, movementListener),
                movementListener);
    }

    /**
     * Start orientation detection.
     *
     * @param orientationListener the orientation listener
     */
    public void startOrientationDetection(OrientationListener orientationListener) {
        startLibrarySensorDetection(new OrientationDetector(orientationListener), orientationListener);
    }

    /**
     * Start orientation detection.
     *
     * @param smoothness          the smoothness
     * @param orientationListener the orientation listener
     */
    public void startOrientationDetection(int smoothness, OrientationListener orientationListener) {
        startLibrarySensorDetection(new OrientationDetector(smoothness, orientationListener),
                orientationListener);
    }

    /**
     * Start pickup device detection.
     *
     * @param pickupDeviceListener the pickup device listener
     */
    public void startPickupDeviceDetection(PickupDeviceListener pickupDeviceListener) {
        startLibrarySensorDetection(new PickupDeviceDetector(pickupDeviceListener),
                pickupDeviceListener);
    }

    /**
     * Start pinch scale detection.
     *
     * @param context            the context
     * @param pinchScaleListener the pinch scale listener
     */
    public void startPinchScaleDetection(Context context, PinchScaleListener pinchScaleListener) {
        if (pinchScaleListener != null) {
            pinchScaleDetector = new PinchScaleDetector(context, pinchScaleListener);
        }
    }

    /**
     * Start proximity detection.
     *
     * @param proximityListener the proximity listener
     */
    public void startProximityDetection(ProximityListener proximityListener) {
        startLibrarySensorDetection(new ProximityDetector(proximityListener), proximityListener);
    }

    /**
     * Start rotation angle detection.
     *
     * @param rotationAngleListener the rotation angle listener
     */
    public void startRotationAngleDetection(RotationAngleListener rotationAngleListener) {
        startLibrarySensorDetection(new RotationAngleDetector(rotationAngleListener),
                rotationAngleListener);
    }

    /**
     * Start scoop detection.
     *
     * @param scoopListener the scoop listener
     */
    public void startScoopDetection(ScoopListener scoopListener) {
        startLibrarySensorDetection(new ScoopDetector(scoopListener), scoopListener);
    }

    /**
     * Start scoop detection.
     *
     * @param threshold     the threshold
     * @param scoopListener the scoop listener
     */
    public void startScoopDetection(float threshold, ScoopListener scoopListener) {
        startLibrarySensorDetection(new ScoopDetector(threshold, scoopListener), scoopListener);
    }

    /**
     * Start shake detection.
     *
     * @param shakeListener the shake listener
     */
    public void startShakeDetection(ShakeListener shakeListener) {
        startLibrarySensorDetection(new ShakeDetector(shakeListener), shakeListener);
    }

    /**
     * Start shake detection.
     *
     * @param threshold                       the threshold
     * @param timeBeforeDeclaringShakeStopped the time before declaring shake stopped
     * @param shakeListener                   the shake listener
     */
    public void startShakeDetection(float threshold, long timeBeforeDeclaringShakeStopped,
            ShakeListener shakeListener) {
        startLibrarySensorDetection(
                new ShakeDetector(threshold, timeBeforeDeclaringShakeStopped, shakeListener),
                shakeListener);
    }

    /**
     * Start sound level detection.
     *
     * @param context            the context
     * @param soundLevelListener the sound level listener
     */
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void startSoundLevelDetection(Context context, SoundLevelListener soundLevelListener) {
        if (soundLevelListener != null && checkPermission(context, Manifest.permission.RECORD_AUDIO)) {
            soundLevelDetector = new SoundLevelDetector(soundLevelListener);
            soundLevelDetector.start();
        } else {
            System.out.println("Permission Required: RECORD_AUDIO");
        }
    }

    /**
     * Start step detection.
     *
     * @param context      the context
     * @param stepListener the step listener
     * @param gender       the gender
     */
    public void startStepDetection(Context context, StepListener stepListener, int gender) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && checkHardware(context,
                PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
            startLibrarySensorDetection(new StepDetectorPostKitKat(gender, stepListener), stepListener);
        } else {
            startLibrarySensorDetection(new StepDetectorPreKitKat(gender, stepListener), stepListener);
        }
    }

    /**
     * Start tilt direction detection.
     *
     * @param tiltDirectionListener the tilt direction listener
     */
    public void startTiltDirectionDetection(TiltDirectionListener tiltDirectionListener) {
        startLibrarySensorDetection(new TiltDirectionDetector(tiltDirectionListener),
                tiltDirectionListener);
    }

    /**
     * Start touch type detection.
     *
     * @param context          the context
     * @param touchTypListener the touch typ listener
     */
    public void startTouchTypeDetection(Context context, TouchTypListener touchTypListener) {
        if (touchTypListener != null) {
            touchTypeDetector = new TouchTypeDetector(context, touchTypListener);
        }
    }

    /**
     * Start wave detection.
     *
     * @param waveListener the wave listener
     */
    public void startWaveDetection(WaveListener waveListener) {
        startLibrarySensorDetection(new WaveDetector(waveListener), waveListener);
    }

    /**
     * Start wave detection.
     *
     * @param threshold    the threshold
     * @param waveListener the wave listener
     */
    public void startWaveDetection(float threshold, WaveListener waveListener) {
        startLibrarySensorDetection(new WaveDetector(threshold, waveListener), waveListener);
    }

    /**
     * Start wrist twist detection.
     *
     * @param wristTwistListener the wrist twist listener
     */
    public void startWristTwistDetection(WristTwistListener wristTwistListener) {
        startLibrarySensorDetection(new WristTwistDetector(wristTwistListener), wristTwistListener);
    }

    /**
     * Start wrist twist detection.
     *
     * @param threshold                the threshold
     * @param timeForWristTwistGesture the time for wrist twist gesture
     * @param wristTwistListener       the wrist twist listener
     */
    public void startWristTwistDetection(float threshold, long timeForWristTwistGesture,
            WristTwistListener wristTwistListener) {
        startLibrarySensorDetection(
                new WristTwistDetector(threshold, timeForWristTwistGesture, wristTwistListener),
                wristTwistListener);
    }

    /**
     * Stop.
     */
    public void stop() {
        this.sensorManager = null;
    }

    /**
     * Stop chop detection.
     *
     * @param chopListener the chop listener
     */
    public void stopChopDetection(ChopListener chopListener) {
        stopLibrarySensorDetection(chopListener);
    }

    /**
     * Stop flip detection.
     *
     * @param flipListener the flip listener
     */
    public void stopFlipDetection(FlipListener flipListener) {
        stopLibrarySensorDetection(flipListener);
    }

    /**
     * Stop light detection.
     *
     * @param lightListener the light listener
     */
    public void stopLightDetection(LightListener lightListener) {
        stopLibrarySensorDetection(lightListener);
    }

    /**
     * Stop movement detection.
     *
     * @param movementListener the movement listener
     */
    public void stopMovementDetection(MovementListener movementListener) {
        stopLibrarySensorDetection(movementListener);
    }

    /**
     * Stop orientation detection.
     *
     * @param orientationListener the orientation listener
     */
    public void stopOrientationDetection(OrientationListener orientationListener) {
        stopLibrarySensorDetection(orientationListener);
    }

    /**
     * Stop pickup device detection.
     *
     * @param pickupDeviceListener the pickup device listener
     */
    public void stopPickupDeviceDetection(PickupDeviceListener pickupDeviceListener) {
        stopLibrarySensorDetection(pickupDeviceListener);
    }

    /**
     * Stop pinch scale detection.
     */
    public void stopPinchScaleDetection() {
        pinchScaleDetector = null;
    }

    /**
     * Stop proximity detection.
     *
     * @param proximityListener the proximity listener
     */
    public void stopProximityDetection(ProximityListener proximityListener) {
        stopLibrarySensorDetection(proximityListener);
    }

    /**
     * Stop rotation angle detection.
     *
     * @param rotationAngleListener the rotation angle listener
     */
    public void stopRotationAngleDetection(RotationAngleListener rotationAngleListener) {
        stopLibrarySensorDetection(rotationAngleListener);
    }

    /**
     * Stop scoop detection.
     *
     * @param scoopListener the scoop listener
     */
    public void stopScoopDetection(ScoopListener scoopListener) {
        stopLibrarySensorDetection(scoopListener);
    }

    /**
     * Stop shake detection.
     *
     * @param shakeListener the shake listener
     */
    public void stopShakeDetection(ShakeListener shakeListener) {
        stopLibrarySensorDetection(shakeListener);
    }

    /**
     * Stop sound level detection.
     */
    public void stopSoundLevelDetection() {
        if (soundLevelDetector != null) {
            soundLevelDetector.stop();
        }
        soundLevelDetector = null;
    }

    /**
     * Stop step detection.
     *
     * @param stepListener the step listener
     */
    public void stopStepDetection(StepListener stepListener) {
        stopLibrarySensorDetection(stepListener);
    }

    /**
     * Stop tilt direction detection.
     *
     * @param tiltDirectionListener the tilt direction listener
     */
    public void stopTiltDirectionDetection(TiltDirectionListener tiltDirectionListener) {
        stopLibrarySensorDetection(tiltDirectionListener);
    }

    /**
     * Stop touch type detection.
     */
    public void stopTouchTypeDetection() {
        touchTypeDetector = null;
    }

    /**
     * Stop wave detection.
     *
     * @param waveListener the wave listener
     */
    public void stopWaveDetection(WaveListener waveListener) {
        stopLibrarySensorDetection(waveListener);
    }

    /**
     * Stop wrist twist detection.
     *
     * @param wristTwistListener the wrist twist listener
     */
    public void stopWristTwistDetection(WristTwistListener wristTwistListener) {
        stopLibrarySensorDetection(wristTwistListener);
    }

    /**
     * Check hardware boolean.
     *
     * @param context  the context
     * @param hardware the hardware
     * @return the boolean
     */
    boolean checkHardware(Context context, String hardware) {
        return context.getPackageManager().hasSystemFeature(hardware);
    }

    /**
     * Check permission boolean.
     *
     * @param context    the context
     * @param permission the permission
     * @return the boolean
     */
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
