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
package com.github.nisrulz.sensey

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.RequiresPermission
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.nisrulz.sensey.gesture.chop.ChopDetector
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.chop.ChopTrigger
import com.github.nisrulz.sensey.gesture.flip.FlipDetector
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.flip.FlipTrigger
import com.github.nisrulz.sensey.gesture.light.LightDetector
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.light.LightTrigger
import com.github.nisrulz.sensey.gesture.movement.MovementDetector
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.movement.MovementTrigger
import com.github.nisrulz.sensey.gesture.orientation.OrientationDetector
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationTrigger
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceDetector
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceTrigger
import com.github.nisrulz.sensey.gesture.proximity.ProximityDetector
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.proximity.ProximityTrigger
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleDetector
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleEvent
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleTrigger
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleDetector
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleEvent
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleTrigger
import com.github.nisrulz.sensey.gesture.scoop.ScoopDetector
import com.github.nisrulz.sensey.gesture.scoop.ScoopEvent
import com.github.nisrulz.sensey.gesture.scoop.ScoopTrigger
import com.github.nisrulz.sensey.gesture.shake.ShakeDetector
import com.github.nisrulz.sensey.gesture.shake.ShakeEvent
import com.github.nisrulz.sensey.gesture.shake.ShakeTrigger
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelDetector
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelEvent
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelTrigger
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackDetector
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackEvent
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackTrigger
import com.github.nisrulz.sensey.gesture.step.StepDetectorPostKitKat
import com.github.nisrulz.sensey.gesture.step.StepDetectorUtil
import com.github.nisrulz.sensey.gesture.step.StepEvent
import com.github.nisrulz.sensey.gesture.step.StepTrigger
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionDetector
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionTrigger
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeDetector
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeEvent
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeTrigger
import com.github.nisrulz.sensey.gesture.wave.WaveDetector
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wave.WaveTrigger
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistDetector
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistTrigger

object Sensey {

    const val SAMPLING_PERIOD_FASTEST = SensorManager.SENSOR_DELAY_FASTEST
    const val SAMPLING_PERIOD_GAME = SensorManager.SENSOR_DELAY_GAME
    const val SAMPLING_PERIOD_NORMAL = SensorManager.SENSOR_DELAY_NORMAL
    const val SAMPLING_PERIOD_UI = SensorManager.SENSOR_DELAY_UI

    private val defaultSensorsMap = mutableMapOf<String, SensorDetector>()
    private var pinchScaleDetector: PinchScaleDetector? = null
    private var soundLevelDetector: SoundLevelDetector? = null
    private var touchTypeDetector: TouchTypeDetector? = null
    private var samplingPeriod = SAMPLING_PERIOD_NORMAL
    private var sensorManager: SensorManager? = null
    private var lifecycleObserver: LifecycleEventObserver? = null
    private var registeredLifecycle: Lifecycle? = null
    private const val LOGTAG = "Sensey"

    var sensorDataLoggingEnabled: Boolean = false
        private set

    fun init(
        context: Context,
        sensorDataLoggingEnabled: Boolean = false,
    ) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.sensorDataLoggingEnabled = sensorDataLoggingEnabled
    }

    fun init(
        context: Context,
        lifecycle: Lifecycle,
        sensorDataLoggingEnabled: Boolean = false,
    ) {
        init(context)
        registerLifecycleObserver(lifecycle)
        this.sensorDataLoggingEnabled = sensorDataLoggingEnabled
    }

    fun init(
        context: Context,
        samplingPeriod: Int,
        sensorDataLoggingEnabled: Boolean = false,
    ) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.samplingPeriod = samplingPeriod
        this.sensorDataLoggingEnabled = sensorDataLoggingEnabled
    }

    fun init(
        context: Context,
        lifecycle: Lifecycle,
        samplingPeriod: Int,
        sensorDataLoggingEnabled: Boolean = false,
    ) {
        init(context)
        registerLifecycleObserver(lifecycle)
        this.samplingPeriod = samplingPeriod
        this.sensorDataLoggingEnabled = sensorDataLoggingEnabled
    }

    fun setupDispatchTouchEvent(event: MotionEvent) {
        touchTypeDetector?.onTouchEvent(event)
        pinchScaleDetector?.onTouchEvent(event)
    }

    fun startChopDetection(dispatcher: (ChopEvent) -> Unit) {
        startLibrarySensorDetection(ChopDetector(ChopTrigger(), dispatcher))
    }

    fun startChopDetection(threshold: Float, timeForChopGesture: Long, dispatcher: (ChopEvent) -> Unit) {
        startLibrarySensorDetection(
            ChopDetector(ChopTrigger(threshold = threshold, timeForChopGesture = timeForChopGesture), dispatcher),
        )
    }

    fun startFlipDetection(dispatcher: (FlipEvent) -> Unit) {
        startLibrarySensorDetection(FlipDetector(FlipTrigger(), dispatcher))
    }

    fun startLightDetection(dispatcher: (LightEvent) -> Unit) {
        startLibrarySensorDetection(LightDetector(LightTrigger(), dispatcher))
    }

    fun startLightDetection(darkThreshold: Float, dispatcher: (LightEvent) -> Unit) {
        startLibrarySensorDetection(
            LightDetector(LightTrigger(darkThreshold = darkThreshold), dispatcher),
        )
    }

    fun startMovementDetection(dispatcher: (MovementEvent) -> Unit) {
        startLibrarySensorDetection(MovementDetector(MovementTrigger(), dispatcher))
    }

    fun startMovementDetection(threshold: Float, timeBeforeDeclaringStationary: Long, dispatcher: (MovementEvent) -> Unit) {
        startLibrarySensorDetection(
            MovementDetector(MovementTrigger(threshold, timeBeforeDeclaringStationary), dispatcher),
        )
    }

    fun startOrientationDetection(dispatcher: (OrientationEvent) -> Unit) {
        startLibrarySensorDetection(OrientationDetector(OrientationTrigger(), dispatcher))
    }

    fun startOrientationDetection(smoothness: Int, dispatcher: (OrientationEvent) -> Unit) {
        startLibrarySensorDetection(
            OrientationDetector(OrientationTrigger(smoothness), dispatcher),
        )
    }

    fun startPickupDeviceDetection(dispatcher: (PickupDeviceEvent) -> Unit) {
        startLibrarySensorDetection(
            PickupDeviceDetector(PickupDeviceTrigger(), dispatcher),
        )
    }

    fun startPinchScaleDetection(context: Context, dispatcher: (PinchScaleEvent) -> Unit) {
        pinchScaleDetector = PinchScaleDetector(context, PinchScaleTrigger(), dispatcher)
    }

    fun startProximityDetection(dispatcher: (ProximityEvent) -> Unit) {
        startLibrarySensorDetection(ProximityDetector(ProximityTrigger(), dispatcher))
    }

    fun startRotationAngleDetection(dispatcher: (RotationAngleEvent) -> Unit) {
        startLibrarySensorDetection(
            RotationAngleDetector(RotationAngleTrigger(), dispatcher),
        )
    }

    fun startScoopDetection(dispatcher: (ScoopEvent) -> Unit) {
        startLibrarySensorDetection(ScoopDetector(ScoopTrigger(), dispatcher))
    }

    fun startScoopDetection(threshold: Float, dispatcher: (ScoopEvent) -> Unit) {
        startLibrarySensorDetection(ScoopDetector(ScoopTrigger(threshold), dispatcher))
    }

    fun startShakeDetection(dispatcher: (ShakeEvent) -> Unit) {
        startLibrarySensorDetection(ShakeDetector(ShakeTrigger(), dispatcher))
    }

    fun startShakeDetection(threshold: Float, timeBeforeDeclaringShakeStopped: Long, dispatcher: (ShakeEvent) -> Unit) {
        startLibrarySensorDetection(
            ShakeDetector(ShakeTrigger(threshold, timeBeforeDeclaringShakeStopped), dispatcher),
        )
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startSoundLevelDetection(context: Context, dispatcher: (SoundLevelEvent) -> Unit) {
        if (checkPermission(context, Manifest.permission.RECORD_AUDIO)) {
            val detector = SoundLevelDetector(SoundLevelTrigger(), dispatcher)
            soundLevelDetector = detector
            detector.start()
        } else {
            println("Permission Required: RECORD_AUDIO")
        }
    }

    fun startStepDetection(context: Context, dispatcher: (StepEvent) -> Unit, gender: Int) {
        val trigger = StepTrigger(gender)
        startLibrarySensorDetection(StepDetectorPostKitKat(trigger, dispatcher))
    }

    fun startTapOnBackDetection(dispatcher: (TapOnBackEvent) -> Unit) {
        startLibrarySensorDetection(TapOnBackDetector(TapOnBackTrigger(), dispatcher))
    }

    fun startTapOnBackDetection(
        angleThreshold: Float,
        tapDebounceMs: Long,
        tapSequenceTimeoutMs: Long,
        dispatcher: (TapOnBackEvent) -> Unit,
    ) {
        startLibrarySensorDetection(
            TapOnBackDetector(TapOnBackTrigger(angleThreshold, tapDebounceMs = tapDebounceMs, tapSequenceTimeoutMs = tapSequenceTimeoutMs), dispatcher),
        )
    }

    fun startTiltDirectionDetection(dispatcher: (TiltDirectionEvent) -> Unit) {
        startLibrarySensorDetection(
            TiltDirectionDetector(TiltDirectionTrigger(), dispatcher),
        )
    }

    fun startTouchTypeDetection(context: Context, dispatcher: (TouchTypeEvent) -> Unit) {
        touchTypeDetector = TouchTypeDetector(context, TouchTypeTrigger(), dispatcher)
    }

    fun startWaveDetection(dispatcher: (WaveEvent) -> Unit) {
        startLibrarySensorDetection(WaveDetector(WaveTrigger(), dispatcher))
    }

    fun startWaveDetection(timeWindowMillis: Float, dispatcher: (WaveEvent) -> Unit) {
        startLibrarySensorDetection(
            WaveDetector(WaveTrigger(timeWindowMillis = timeWindowMillis), dispatcher),
        )
    }

    fun startWristTwistDetection(dispatcher: (WristTwistEvent) -> Unit) {
        startLibrarySensorDetection(
            WristTwistDetector(WristTwistTrigger(), dispatcher),
        )
    }

    fun startWristTwistDetection(threshold: Float, timeForWristTwistGesture: Long, dispatcher: (WristTwistEvent) -> Unit) {
        startLibrarySensorDetection(
            WristTwistDetector(
                WristTwistTrigger(threshold = threshold, timeForWristTwistGesture = timeForWristTwistGesture),
                dispatcher,
            ),
        )
    }

    fun stop() {
        for (sensor in defaultSensorsMap.values) {
            stopSensorDetection(sensor)
        }
        defaultSensorsMap.clear()
        lifecycleObserver?.let { registeredLifecycle?.removeObserver(it) }
        lifecycleObserver = null
        registeredLifecycle = null
        sensorManager = null
    }

    fun stopChopDetection() {
        stopLibrarySensorDetection("ChopDetector")
    }

    fun stopFlipDetection() {
        stopLibrarySensorDetection("FlipDetector")
    }

    fun stopLightDetection() {
        stopLibrarySensorDetection("LightDetector")
    }

    fun stopMovementDetection() {
        stopLibrarySensorDetection("MovementDetector")
    }

    fun stopOrientationDetection() {
        stopLibrarySensorDetection("OrientationDetector")
    }

    fun stopPickupDeviceDetection() {
        stopLibrarySensorDetection("PickupDeviceDetector")
    }

    fun stopPinchScaleDetection() {
        pinchScaleDetector = null
    }

    fun stopProximityDetection() {
        stopLibrarySensorDetection("ProximityDetector")
    }

    fun stopRotationAngleDetection() {
        stopLibrarySensorDetection("RotationAngleDetector")
    }

    fun stopScoopDetection() {
        stopLibrarySensorDetection("ScoopDetector")
    }

    fun stopShakeDetection() {
        stopLibrarySensorDetection("ShakeDetector")
    }

    fun stopSoundLevelDetection() {
        soundLevelDetector?.stop()
        soundLevelDetector = null
    }

    fun stopStepDetection() {
        stopLibrarySensorDetection("StepDetectorPostKitKat")
    }

    fun stopTapOnBackDetection() {
        stopLibrarySensorDetection("TapOnBackDetector")
    }

    fun stopTiltDirectionDetection() {
        stopLibrarySensorDetection("TiltDirectionDetector")
    }

    fun stopTouchTypeDetection() {
        touchTypeDetector = null
    }

    fun stopWaveDetection() {
        stopLibrarySensorDetection("WaveDetector")
    }

    fun stopWristTwistDetection() {
        stopLibrarySensorDetection("WristTwistDetector")
    }

    fun checkHardware(context: Context, hardware: String): Boolean {
        return context.packageManager.hasSystemFeature(hardware)
    }

    fun checkPermission(context: Context, permission: String): Boolean {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun registerLifecycleObserver(lifecycle: Lifecycle) {
        lifecycleObserver?.let { registeredLifecycle?.removeObserver(it) }
        lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                stop()
            }
        }
        registeredLifecycle = lifecycle
        lifecycleObserver?.let { lifecycle.addObserver(it) }
    }

    private fun areAllSensorsValid(sensors: Iterable<Sensor>): Boolean {
        for (sensor in sensors) {
            if (sensor == null) return false
        }
        return true
    }

    private fun convertTypesToSensors(vararg sensorTypes: Int): List<Sensor> {
        val sensors = mutableListOf<Sensor>()
        sensorManager?.let { manager ->
            for (sensorType in sensorTypes) {
                val sensor = manager.getDefaultSensor(sensorType)
                if (sensor != null) {
                    sensors.add(sensor)
                } else {
                    Log.w(LOGTAG, "Sensor type $sensorType not available on this device")
                }
            }
        }
        return sensors
    }



    private fun registerDetectorForAllSensors(detector: SensorDetector, sensors: Iterable<Sensor>) {
        for (sensor in sensors) {
            sensorManager?.registerListener(detector, sensor, samplingPeriod)
        }
    }

    private fun startLibrarySensorDetection(detector: SensorDetector) {
        val key = detector::class.simpleName ?: return
        if (!defaultSensorsMap.containsKey(key)) {
            defaultSensorsMap[key] = detector
            startSensorDetection(detector)
        }
    }

    private fun startSensorDetection(detector: SensorDetector) {
        val sensors = convertTypesToSensors(*detector.sensorTypes)
        registerDetectorForAllSensors(detector, sensors)
    }

    private fun stopLibrarySensorDetection(key: String) {
        val detector = defaultSensorsMap.remove(key)
        stopSensorDetection(detector)
    }

    private fun stopSensorDetection(detector: SensorDetector?) {
        if (detector != null && sensorManager != null) {
            sensorManager?.unregisterListener(detector)
        }
    }
}
