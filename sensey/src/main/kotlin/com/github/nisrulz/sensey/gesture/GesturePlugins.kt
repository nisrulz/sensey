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
package com.github.nisrulz.sensey.gesture

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.SensorDetector
import com.github.nisrulz.sensey.TypedSensorDetector
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.chop.ChopTrigger
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.flip.FlipTrigger
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.light.LightTrigger
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.movement.MovementTrigger
import com.github.nisrulz.sensey.gesture.orientation.OrientationDetector
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationTrigger
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceTrigger
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleEvent
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleTrigger
import com.github.nisrulz.sensey.gesture.proximity.ProximityDetector
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.proximity.ProximityTrigger
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleDetector
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleEvent
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleTrigger
import com.github.nisrulz.sensey.gesture.scoop.ScoopEvent
import com.github.nisrulz.sensey.gesture.scoop.ScoopTrigger
import com.github.nisrulz.sensey.gesture.shake.ShakeEvent
import com.github.nisrulz.sensey.gesture.shake.ShakeTrigger
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelDetector
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelEvent
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelTrigger
import com.github.nisrulz.sensey.gesture.step.StepEvent
import com.github.nisrulz.sensey.gesture.step.StepTrigger
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackEvent
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackTrigger
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionTrigger
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeEvent
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeTrigger
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wave.WaveTrigger
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistTrigger

fun shakePlugin(
    threshold: Float = 3f,
    timeBeforeDeclaringShakeStopped: Long = 1000L,
    dispatcher: (ShakeEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "ShakePlugin",
        detectorFactory = {
            TypedSensorDetector(
                ShakeTrigger(threshold, timeBeforeDeclaringShakeStopped),
                dispatcher,
                Sensor.TYPE_ACCELEROMETER,
            )
        },
    )

fun flipPlugin(
    faceUpLowerBound: Float = 8f,
    faceUpUpperBound: Float = 10.5f,
    faceDownLowerBound: Float = -10.5f,
    faceDownUpperBound: Float = -8f,
    dispatcher: (FlipEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "FlipPlugin",
        detectorFactory = {
            TypedSensorDetector(
                FlipTrigger(faceUpLowerBound, faceUpUpperBound, faceDownLowerBound, faceDownUpperBound),
                dispatcher,
                Sensor.TYPE_ACCELEROMETER,
            )
        },
    )

fun lightPlugin(
    darkThreshold: Float = 8f,
    dispatcher: (LightEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "LightPlugin",
        detectorFactory = {
            TypedSensorDetector(
                LightTrigger(darkThreshold = darkThreshold),
                dispatcher,
                Sensor.TYPE_LIGHT,
            )
        },
    )

fun proximityPlugin(
    debounceMillis: Long = 200L,
    dispatcher: (ProximityEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "ProximityPlugin",
        detectorFactory = { ProximityDetector(ProximityTrigger(debounceMillis), dispatcher) },
    )

fun movementPlugin(
    threshold: Float = 0.3f,
    timeBeforeDeclaringStationary: Long = 5000L,
    dispatcher: (MovementEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "MovementPlugin",
        detectorFactory = {
            TypedSensorDetector(
                MovementTrigger(threshold, timeBeforeDeclaringStationary),
                dispatcher,
                Sensor.TYPE_ACCELEROMETER,
            )
        },
    )

fun orientationPlugin(
    smoothness: Int = 1,
    dispatcher: (OrientationEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "OrientationPlugin",
        detectorFactory = { OrientationDetector(OrientationTrigger(smoothness), dispatcher) },
    )

fun chopPlugin(
    threshold: Float = 25f,
    timeForChopGesture: Long = 700L,
    dispatcher: (ChopEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "ChopPlugin",
        detectorFactory = {
            TypedSensorDetector(ChopTrigger(threshold, timeForChopGesture), dispatcher, Sensor.TYPE_ACCELEROMETER)
        },
    )

fun wristTwistPlugin(
    threshold: Float = 12f,
    timeForWristTwistGesture: Long = 1000L,
    dispatcher: (WristTwistEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "WristTwistPlugin",
        detectorFactory = {
            TypedSensorDetector(
                WristTwistTrigger(threshold, timeForWristTwistGesture),
                dispatcher,
                Sensor.TYPE_ACCELEROMETER,
            )
        },
    )

fun wavePlugin(
    timeWindowMillis: Float = 1000f,
    debounceMillis: Long = 1000L,
    dispatcher: (WaveEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "WavePlugin",
        detectorFactory = {
            TypedSensorDetector(WaveTrigger(timeWindowMillis, debounceMillis), dispatcher, Sensor.TYPE_PROXIMITY)
        },
    )

fun scoopPlugin(
    threshold: Float = 10f,
    dispatcher: (ScoopEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "ScoopPlugin",
        detectorFactory = { TypedSensorDetector(ScoopTrigger(threshold), dispatcher, Sensor.TYPE_ACCELEROMETER) },
    )

fun pickupDevicePlugin(dispatcher: (PickupDeviceEvent) -> Unit): GesturePlugin =
    SensorGesturePlugin(
        key = "PickupDevicePlugin",
        detectorFactory = { TypedSensorDetector(PickupDeviceTrigger(), dispatcher, Sensor.TYPE_ACCELEROMETER) },
    )

fun tapOnBackPlugin(
    angleThreshold: Float = 1.5f,
    minAngleJerk: Float = 1.5f,
    tapDebounceMs: Long = 250L,
    tapSequenceTimeoutMs: Long = 500L,
    dispatcher: (TapOnBackEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "TapOnBackPlugin",
        detectorFactory = {
            TypedSensorDetector(
                TapOnBackTrigger(angleThreshold, minAngleJerk, tapDebounceMs, tapSequenceTimeoutMs),
                dispatcher,
                Sensor.TYPE_ACCELEROMETER,
            )
        },
    )

fun tiltDirectionPlugin(
    threshold: Float = 0.5f,
    dispatcher: (TiltDirectionEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "TiltDirectionPlugin",
        detectorFactory = { TypedSensorDetector(TiltDirectionTrigger(threshold), dispatcher, Sensor.TYPE_GYROSCOPE) },
    )

fun rotationAnglePlugin(
    minAngleChange: Float = 1f,
    dispatcher: (RotationAngleEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "RotationAnglePlugin",
        detectorFactory = { RotationAngleDetector(RotationAngleTrigger(minAngleChange), dispatcher) },
    )

fun stepPlugin(
    gender: Int = 0,
    threshold: Float = 3f,
    dispatcher: (StepEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "StepPlugin",
        detectorFactory = { TypedSensorDetector(StepTrigger(gender, threshold), dispatcher, Sensor.TYPE_STEP_COUNTER) },
    )

fun pinchScalePlugin(
    context: Context,
    dispatcher: (PinchScaleEvent) -> Unit,
): GesturePlugin = PinchScalePlugin(PinchScaleTrigger(), dispatcher)

fun touchTypePlugin(
    context: Context,
    dispatcher: (TouchTypeEvent) -> Unit,
): GesturePlugin = TouchTypePlugin(TouchTypeTrigger(), dispatcher)

/**
 * Creates a sound level detection plugin.
 *
 * Requires `RECORD_AUDIO` permission at runtime. Captures raw audio from the
 * microphone via [android.media.AudioRecord] with `VOICE_RECOGNITION` source
 * to compute sound pressure levels (RMS → dB). No audio data is stored,
 * transmitted, or persisted — only the computed decibel level is exposed.
 *
 * On API 33+ the system grants `RECORD_AUDIO` at install time for apps
 * targeting the permission via manifest, so no runtime prompt is shown.
 */
fun soundLevelPlugin(
    context: Context,
    dispatcher: (SoundLevelEvent) -> Unit,
): GesturePlugin = SoundLevelPlugin(context, SoundLevelTrigger(), dispatcher)

private class SensorGesturePlugin(
    override val key: String,
    private val detectorFactory: () -> SensorDetector,
) : GesturePlugin {
    private var detector: SensorDetector? = null

    override fun onRegister(sensey: Sensey) {
        detector = detectorFactory()
        sensey.registerSensorDetector(detector!!)
    }

    override fun onUnregister(sensey: Sensey) {
        detector?.let { sensey.unregisterSensorDetector(it) }
        detector = null
    }
}

private class PinchScalePlugin(
    private val trigger: PinchScaleTrigger,
    private val dispatcher: (PinchScaleEvent) -> Unit,
) : GesturePlugin {
    override val key = PinchScalePlugin::class.java.name
    private val provider = ComposeGestureProvider { installPinchScale() }

    override fun onRegister(sensey: Sensey) {
        sensey.registerComposeGestureProvider(provider)
    }

    override fun onUnregister(sensey: Sensey) {
        sensey.unregisterComposeGestureProvider(provider)
    }

    private suspend fun PointerInputScope.installPinchScale() {
        detectTransformGestures { _, _, zoom, _ ->
            val event = trigger.evaluate(floatArrayOf(zoom), System.currentTimeMillis())
            event?.let(dispatcher)
        }
    }
}

private class TouchTypePlugin(
    private val trigger: TouchTypeTrigger,
    private val dispatcher: (TouchTypeEvent) -> Unit,
) : GesturePlugin {
    override val key = TouchTypePlugin::class.java.name
    private var tapCount = 0
    private var lastTapTime = 0L
    private var dragStart = Offset.Zero
    private val tapProvider = ComposeGestureProvider { installTapGestures() }
    private val dragProvider = ComposeGestureProvider { installDragGestures() }

    override fun onRegister(sensey: Sensey) {
        sensey.registerComposeGestureProvider(tapProvider)
        sensey.registerComposeGestureProvider(dragProvider)
    }

    override fun onUnregister(sensey: Sensey) {
        sensey.unregisterComposeGestureProvider(tapProvider)
        sensey.unregisterComposeGestureProvider(dragProvider)
    }

    private suspend fun PointerInputScope.installTapGestures() {
        detectTapGestures(
            onTap = {
                val now = System.currentTimeMillis()
                tapCount = if (now - lastTapTime <= TAP_GAP_MS) tapCount + 1 else 1
                lastTapTime = now
                if (tapCount >= 3) {
                    tapCount = 0
                    dispatcher(TouchTypeEvent.NTap(3))
                } else {
                    dispatcher(TouchTypeEvent.SingleTap)
                }
            },
            onDoubleTap = { dispatcher(TouchTypeEvent.DoubleTap) },
            onLongPress = { dispatcher(TouchTypeEvent.LongPress) },
        )
    }

    private suspend fun PointerInputScope.installDragGestures() {
        detectDragGestures(
            onDragStart = { dragStart = it },
            onDrag = { change, dragAmount ->
                change.consume()
                val event =
                    trigger.evaluate(
                        floatArrayOf(
                            (change.position - dragStart).x,
                            (change.position - dragStart).y,
                            dragAmount.x,
                            dragAmount.y,
                        ),
                        System.currentTimeMillis(),
                    )
                when (event) {
                    is TouchTypeEvent.Swipe -> dispatcher(event)
                    is TouchTypeEvent.Scroll -> dispatcher(event)
                    else -> {}
                }
            },
            onDragEnd = { dragStart = Offset.Zero },
            onDragCancel = { dragStart = Offset.Zero },
        )
    }

    companion object {
        private const val TAP_GAP_MS = 400L
    }
}

private class SoundLevelPlugin(
    private val context: Context,
    private val trigger: SoundLevelTrigger,
    private val dispatcher: (SoundLevelEvent) -> Unit,
) : GesturePlugin {
    override val key = SoundLevelPlugin::class.java.name
    private var detector: SoundLevelDetector? = null

    override fun onRegister(sensey: Sensey) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            android.util.Log.w("Sensey", "RECORD_AUDIO permission not granted — SoundLevelPlugin disabled")
            return
        }
        detector = SoundLevelDetector(trigger, dispatcher)
        detector?.start()
    }

    override fun onUnregister(sensey: Sensey) {
        detector?.stop()
        detector = null
    }
}
