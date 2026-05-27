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

import android.content.Context
import android.hardware.Sensor
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChange
import com.github.nisrulz.sensey.SensorDetector
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.gesture.chop.ChopDetector
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.chop.ChopTrigger
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider
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
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleEvent
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleTrigger
import com.github.nisrulz.sensey.gesture.proximity.ProximityDetector
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.proximity.ProximityTrigger
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
import com.github.nisrulz.sensey.gesture.step.StepDetectorPostKitKat
import com.github.nisrulz.sensey.gesture.step.StepEvent
import com.github.nisrulz.sensey.gesture.step.StepTrigger
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackDetector
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackEvent
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackTrigger
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionDetector
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionTrigger
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeEvent
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeTrigger
import com.github.nisrulz.sensey.gesture.wave.WaveDetector
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wave.WaveTrigger
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistDetector
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistTrigger

fun shakePlugin(
    threshold: Float = 3f,
    timeBeforeDeclaringShakeStopped: Long = 1000L,
    dispatcher: (ShakeEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "ShakePlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER),
    detectorFactory = { ShakeDetector(ShakeTrigger(threshold, timeBeforeDeclaringShakeStopped), dispatcher) },
)

fun flipPlugin(
    dispatcher: (FlipEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "FlipPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER),
    detectorFactory = { FlipDetector(FlipTrigger(), dispatcher) },
)

fun lightPlugin(
    darkThreshold: Float = 8f,
    dispatcher: (LightEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "LightPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_LIGHT),
    detectorFactory = { LightDetector(LightTrigger(darkThreshold = darkThreshold), dispatcher) },
)

fun proximityPlugin(
    dispatcher: (ProximityEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "ProximityPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_PROXIMITY),
    detectorFactory = { ProximityDetector(ProximityTrigger(), dispatcher) },
)

fun movementPlugin(
    threshold: Float = 0.3f,
    timeBeforeDeclaringStationary: Long = 5000L,
    dispatcher: (MovementEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "MovementPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER),
    detectorFactory = { MovementDetector(MovementTrigger(threshold, timeBeforeDeclaringStationary), dispatcher) },
)

fun orientationPlugin(
    smoothness: Int = 1,
    dispatcher: (OrientationEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "OrientationPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD),
    detectorFactory = { OrientationDetector(OrientationTrigger(smoothness), dispatcher) },
)

fun chopPlugin(
    threshold: Float = 25f,
    timeForChopGesture: Long = 700L,
    dispatcher: (ChopEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "ChopPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER),
    detectorFactory = { ChopDetector(ChopTrigger(threshold, timeForChopGesture), dispatcher) },
)

fun wristTwistPlugin(
    threshold: Float = 12f,
    timeForWristTwistGesture: Long = 1000L,
    dispatcher: (WristTwistEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "WristTwistPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER),
    detectorFactory = { WristTwistDetector(WristTwistTrigger(threshold, timeForWristTwistGesture), dispatcher) },
)

fun wavePlugin(
    timeWindowMillis: Float = 1000f,
    debounceMillis: Long = 1000L,
    dispatcher: (WaveEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "WavePlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_PROXIMITY),
    detectorFactory = { WaveDetector(WaveTrigger(timeWindowMillis, debounceMillis), dispatcher) },
)

fun scoopPlugin(
    threshold: Float = 10f,
    dispatcher: (ScoopEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "ScoopPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER),
    detectorFactory = { ScoopDetector(ScoopTrigger(threshold), dispatcher) },
)

fun pickupDevicePlugin(
    dispatcher: (PickupDeviceEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "PickupDevicePlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER),
    detectorFactory = { PickupDeviceDetector(PickupDeviceTrigger(), dispatcher) },
)

fun tapOnBackPlugin(
    angleThreshold: Float = 1.5f,
    minAngleJerk: Float = 1.5f,
    tapDebounceMs: Long = 250L,
    tapSequenceTimeoutMs: Long = 500L,
    dispatcher: (TapOnBackEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "TapOnBackPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ACCELEROMETER),
    detectorFactory = {
        TapOnBackDetector(
            TapOnBackTrigger(angleThreshold, minAngleJerk, tapDebounceMs, tapSequenceTimeoutMs),
            dispatcher,
        )
    },
)

fun tiltDirectionPlugin(
    threshold: Float = 0.5f,
    dispatcher: (TiltDirectionEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "TiltDirectionPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_GYROSCOPE),
    detectorFactory = { TiltDirectionDetector(TiltDirectionTrigger(threshold), dispatcher) },
)

fun rotationAnglePlugin(
    minAngleChange: Float = 1f,
    dispatcher: (RotationAngleEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "RotationAnglePlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_ROTATION_VECTOR),
    detectorFactory = { RotationAngleDetector(RotationAngleTrigger(minAngleChange), dispatcher) },
)

fun stepPlugin(
    gender: Int = 0,
    threshold: Float = 3f,
    dispatcher: (StepEvent) -> Unit,
): GesturePlugin = SensorGesturePlugin(
    key = "StepPlugin",
    sensorTypes = intArrayOf(Sensor.TYPE_STEP_COUNTER),
    detectorFactory = { StepDetectorPostKitKat(StepTrigger(gender, threshold), dispatcher) },
)

fun pinchScalePlugin(
    context: Context,
    dispatcher: (PinchScaleEvent) -> Unit,
): GesturePlugin = PinchScalePlugin(PinchScaleTrigger(), dispatcher)

fun touchTypePlugin(
    context: Context,
    dispatcher: (TouchTypeEvent) -> Unit,
): GesturePlugin = TouchTypePlugin(TouchTypeTrigger(), dispatcher)

fun soundLevelPlugin(
    context: Context,
    dispatcher: (SoundLevelEvent) -> Unit,
): GesturePlugin = SoundLevelPlugin(SoundLevelTrigger(), dispatcher)

private class SensorGesturePlugin(
    override val key: String,
    private val sensorTypes: IntArray,
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

    override fun onRegister(sensey: Sensey) {
        sensey.registerComposeGestureProvider(ComposeGestureProvider { installPinchScale() })
    }

    override fun onUnregister(sensey: Sensey) {}

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

    override fun onRegister(sensey: Sensey) {
        sensey.registerComposeGestureProvider(ComposeGestureProvider { installTouchType() })
        sensey.registerComposeGestureProvider(ComposeGestureProvider { installDragType() })
    }

    override fun onUnregister(sensey: Sensey) {}

    private suspend fun PointerInputScope.installTouchType() {
        detectTapGestures(
            onTap = {
                val now = System.currentTimeMillis()
                tapCount = if (now - lastTapTime <= 400L) tapCount + 1 else 1
                lastTapTime = now
                if (tapCount >= 3) {
                    tapCount = 0
                    dispatcher(TouchTypeEvent.NTap(3))
                } else {
                    dispatcher(TouchTypeEvent.SingleTap)
                }
            },
            onDoubleTap = {
                dispatcher(TouchTypeEvent.DoubleTap)
            },
            onLongPress = {
                dispatcher(TouchTypeEvent.LongPress)
            },
        )
    }

    private suspend fun PointerInputScope.installDragType() {
        detectDragGestures(
            onDragStart = { startOffset ->
                dragStart = startOffset
            },
            onDrag = { change, dragAmount ->
                change.consume()
                val totalDelta = change.position - dragStart
                val isSwipe = kotlin.math.abs(dragAmount.x) > 200f || kotlin.math.abs(dragAmount.y) > 200f
                val event = trigger.evaluate(
                    floatArrayOf(totalDelta.x, totalDelta.y, dragAmount.x, dragAmount.y),
                    System.currentTimeMillis(),
                )
                event?.let { e ->
                    when (e) {
                        is TouchTypeEvent.Swipe -> dispatcher(e)
                        is TouchTypeEvent.Scroll -> dispatcher(e)
                        else -> {}
                    }
                }
            },
            onDragEnd = {
                dragStart = Offset.Zero
            },
        )
    }

}

private class SoundLevelPlugin(
    private val trigger: SoundLevelTrigger,
    private val dispatcher: (SoundLevelEvent) -> Unit,
) : GesturePlugin {
    override val key = SoundLevelPlugin::class.java.name
    private var detector: SoundLevelDetector? = null

    override fun onRegister(sensey: Sensey) {
        detector = SoundLevelDetector(trigger, dispatcher)
        detector?.start()
    }

    override fun onUnregister(sensey: Sensey) {
        detector?.stop()
        detector = null
    }
}
