
package com.github.nisrulz.sensey.gesture

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.SensorDetector
import com.github.nisrulz.sensey.TypedSensorDetector
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.audio.clap.ClapDetector
import com.github.nisrulz.sensey.gesture.audio.clap.ClapEvent
import com.github.nisrulz.sensey.gesture.audio.clap.ClapTrigger
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.chop.ChopTrigger
import com.github.nisrulz.sensey.gesture.devicespin.DeviceSpinEvent
import com.github.nisrulz.sensey.gesture.devicespin.DeviceSpinTrigger
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.flip.FlipTrigger
import com.github.nisrulz.sensey.gesture.headshake.HeadShakeEvent
import com.github.nisrulz.sensey.gesture.headshake.HeadShakeTrigger
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.light.LightTrigger
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.movement.MovementTrigger
import com.github.nisrulz.sensey.gesture.nodgesture.NodGestureEvent
import com.github.nisrulz.sensey.gesture.nodgesture.NodGestureTrigger
import com.github.nisrulz.sensey.gesture.orientation.OrientationDetector
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationTrigger
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceTrigger
import com.github.nisrulz.sensey.gesture.proximity.ProximityDetector
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.proximity.ProximityTrigger
import com.github.nisrulz.sensey.gesture.raisetoear.RaiseToEarDetector
import com.github.nisrulz.sensey.gesture.raisetoear.RaiseToEarEvent
import com.github.nisrulz.sensey.gesture.raisetoear.RaiseToEarTrigger
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
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackDetector
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackEvent
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackTrigger
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionTrigger
import com.github.nisrulz.sensey.gesture.touch.CornerSwipeConfig
import com.github.nisrulz.sensey.gesture.touch.EdgeSwipeConfig
import com.github.nisrulz.sensey.gesture.touch.LongPressDragConfig
import com.github.nisrulz.sensey.gesture.touch.PinchScaleConfig
import com.github.nisrulz.sensey.gesture.touch.SwipeConfig
import com.github.nisrulz.sensey.gesture.touch.TouchConfig
import com.github.nisrulz.sensey.gesture.touch.TouchEvent
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.CornerType
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.EdgeType
import com.github.nisrulz.sensey.gesture.touch.TouchPlugin
import com.github.nisrulz.sensey.gesture.touch.TwoFingerSwipeConfig
import com.github.nisrulz.sensey.gesture.turnover.TurnOverEvent
import com.github.nisrulz.sensey.gesture.turnover.TurnOverTrigger
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
    timeBeforeDeclaringStationary: Long = 1500L,
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
            TypedSensorDetector(ChopTrigger(threshold, timeForChopGesture), dispatcher, Sensor.TYPE_LINEAR_ACCELERATION)
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
                Sensor.TYPE_LINEAR_ACCELERATION,
            )
        },
    )

/**
 * Creates a turnover (gyro-based flip) detection plugin.
 *
 * More precise than the accelerometer-based [flipPlugin] since it directly
 * measures angular motion via the gyroscope rather than inferring orientation
 * from the gravity vector.
 */
fun turnOverPlugin(
    angleThreshold: Float = 150f,
    dispatcher: (TurnOverEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "TurnOverPlugin",
        detectorFactory = {
            TypedSensorDetector(
                TurnOverTrigger(angleThreshold = angleThreshold),
                dispatcher,
                Sensor.TYPE_GYROSCOPE,
            )
        },
    )

/**
 * Creates a device spin detection plugin.
 *
 * Detects rapid rotation on any axis exceeding [angleThreshold] within
 * a [timeWindowMs] window. Useful for "spin to shuffle" or similar features.
 */
fun deviceSpinPlugin(
    angleThreshold: Float = 270f,
    timeWindowMs: Long = 2000L,
    dispatcher: (DeviceSpinEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "DeviceSpinPlugin",
        detectorFactory = {
            TypedSensorDetector(
                DeviceSpinTrigger(angleThreshold = angleThreshold, timeWindowMs = timeWindowMs),
                dispatcher,
                Sensor.TYPE_GYROSCOPE,
            )
        },
    )

/**
 * Creates a raise-to-ear detection plugin.
 *
 * Fuses proximity and gravity sensor data. Fires when the device is held
 * near the ear (proximity near) and in an upright orientation (gravity
 * aligned with the Z-axis). Useful for call screen-off or audio routing.
 */
fun raiseToEarPlugin(
    maxProximityCm: Float = 5f,
    minGzRatio: Float = 0.3f,
    debounceMs: Long = 500L,
    dispatcher: (RaiseToEarEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "RaiseToEarPlugin",
        detectorFactory = {
            RaiseToEarDetector(
                trigger =
                    RaiseToEarTrigger(
                        maxProximityCm = maxProximityCm,
                        minGzRatio = minGzRatio,
                        debounceMs = debounceMs,
                    ),
                dispatcher = dispatcher,
            )
        },
    )

/**
 * Creates a clap detection plugin.
 *
 * Detects hand claps via the microphone using a multi‑stage pipeline:
 * RMS + ZCR weighting + adaptive noise floor + multi‑clap counting.
 * Requires `RECORD_AUDIO` permission at runtime. No audio data is
 * stored or transmitted.
 *
 * @param thresholdDb  absolute minimum dBFS a buffer must reach (default -45f)
 * @param requiredClaps  number of distinct claps needed to fire (default 2)
 * @param clapTimeframeMs  rolling window for multi‑clap counting (default 800ms)
 * @param dispatchEvents  callback receiving [ClapEvent.Clapped]
 */
fun clapPlugin(
    context: Context,
    thresholdDb: Float = -45f,
    requiredClaps: Int = 2,
    clapTimeframeMs: Long = 800L,
    dispatchEvents: (ClapEvent) -> Unit,
): GesturePlugin =
    ClapPlugin(
        context,
        ClapTrigger(
            thresholdDb = thresholdDb,
            requiredClaps = requiredClaps,
            clapTimeframeMs = clapTimeframeMs,
        ),
        dispatchEvents,
    )

fun wavePlugin(
    timeWindowMillis: Long = 1000L,
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

fun pickupDevicePlugin(
    settleTimeMs: Long = 1000L,
    dispatcher: (PickupDeviceEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "PickupDevicePlugin",
        detectorFactory = {
            TypedSensorDetector(PickupDeviceTrigger(settleTimeMs = settleTimeMs), dispatcher, Sensor.TYPE_ACCELEROMETER)
        },
    )

fun tapOnBackPlugin(
    accelThreshold: Float = 1.5f,
    minJerk: Float = 2.0f,
    preSettleMs: Long = 200L,
    settleWindowMs: Long = 100L,
    reboundGuardMs: Long = 180L,
    tapIntervalMs: Long = 400L,
    cooldownMs: Long = 1000L,
    dispatcher: (TapOnBackEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "TapOnBackPlugin",
        detectorFactory = {
            TapOnBackDetector(
                trigger =
                    TapOnBackTrigger(
                        accelThreshold = accelThreshold,
                        minJerk = minJerk,
                        preSettleMs = preSettleMs,
                        settleWindowMs = settleWindowMs,
                        reboundGuardMs = reboundGuardMs,
                        tapIntervalMs = tapIntervalMs,
                        cooldownMs = cooldownMs,
                    ),
                dispatcher = dispatcher,
            )
        },
    )

/**
 * Creates a nod gesture detection plugin.
 *
 * Detects a rapid pitch oscillation (nodding "yes"): tilting the device
 * top-forward (~30° down) then back past level, within 800ms.
 * Uses the gyroscope X-axis via [GyroIntegrator].
 */
fun nodGesturePlugin(
    angleThreshold: Float = 30f,
    timeWindowMs: Long = 800L,
    cooldownMs: Long = 1500L,
    dispatcher: (NodGestureEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "NodGesturePlugin",
        detectorFactory = {
            TypedSensorDetector(
                NodGestureTrigger(
                    angleThreshold = angleThreshold,
                    timeWindowMs = timeWindowMs,
                    cooldownMs = cooldownMs,
                ),
                dispatcher,
                Sensor.TYPE_GYROSCOPE,
            )
        },
    )

/**
 * Creates a head shake detection plugin.
 *
 * Detects a rapid yaw oscillation (shaking "no"): rotating the device
 * left then right past level (~30° each way), within 800ms.
 * Uses the gyroscope Z-axis via [GyroIntegrator].
 */
fun headShakePlugin(
    angleThreshold: Float = 30f,
    timeWindowMs: Long = 800L,
    cooldownMs: Long = 1500L,
    dispatcher: (HeadShakeEvent) -> Unit,
): GesturePlugin =
    SensorGesturePlugin(
        key = "HeadShakePlugin",
        detectorFactory = {
            TypedSensorDetector(
                HeadShakeTrigger(angleThreshold = angleThreshold, timeWindowMs = timeWindowMs, cooldownMs = cooldownMs),
                dispatcher,
                Sensor.TYPE_GYROSCOPE,
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

/**
 * Generic touch gesture plugin.
 *
 * Detects all touch gesture types based on [config]. By default (no
 * config) enables taps and basic swipe/scroll.
 *
 * Convenience wrappers ([edgeSwipePlugin], [cornerSwipePlugin], etc.)
 * configure this plugin internally with a pre-configured [TouchConfig].
 *
 * Example:
 * ```
 * touchPlugin(context) { event ->
 *     when (event) {
 *         is TouchEvent.Tap.Single -> // ...
 *         is TouchEvent.Swipe -> // ...
 *         else -> {}
 *     }
 * }
 * ```
 */
fun touchPlugin(
    context: Context,
    config: TouchConfig = TouchConfig(),
    dispatcher: (TouchEvent) -> Unit,
): GesturePlugin = TouchPlugin(config, dispatcher)

/**
 * Convenience wrapper configured for edge swipe detection only.
 *
 * Internally calls [touchPlugin] with [TouchConfig] setting
 * [EdgeSwipeConfig.enabled] to true.
 */
fun edgeSwipePlugin(
    context: Context,
    edgeThresholdDp: Dp = 48.dp,
    enabledEdges: Set<EdgeType> = EdgeType.entries.toSet(),
    dispatcher: (TouchEvent) -> Unit,
): GesturePlugin =
    TouchPlugin(
        TouchConfig(
            edgeSwipe = EdgeSwipeConfig(enabled = true, edgeThresholdDp = edgeThresholdDp, enabledEdges = enabledEdges),
        ),
        dispatcher,
    )

/**
 * Convenience wrapper configured for diagonal swipe detection only.
 *
 * Internally calls [touchPlugin] with [SwipeConfig.diagonalOnly] set to true.
 *
 * @param minDragDistance minimum distance in pixels for a swipe (default 80f)
 */
fun diagonalSwipePlugin(
    context: Context,
    minDragDistance: Float = 80f,
    dispatcher: (TouchEvent) -> Unit,
): GesturePlugin =
    TouchPlugin(
        TouchConfig(
            swipe = SwipeConfig(enabled = true, minDistance = minDragDistance, diagonalOnly = true),
        ),
        dispatcher,
    )

/**
 * Convenience wrapper configured for long-press-then-drag detection only.
 */
fun longPressDragPlugin(
    context: Context,
    minDragDistance: Float = 20f,
    dispatcher: (TouchEvent) -> Unit,
): GesturePlugin =
    TouchPlugin(
        TouchConfig(
            longPressDrag = LongPressDragConfig(enabled = true, minDistance = minDragDistance),
        ),
        dispatcher,
    )

/**
 * Convenience wrapper configured for two-finger swipe detection only.
 */
fun twoFingerSwipePlugin(
    context: Context,
    minDragDistance: Float = 80f,
    dispatcher: (TouchEvent) -> Unit,
): GesturePlugin =
    TouchPlugin(
        TouchConfig(
            twoFingerSwipe = TwoFingerSwipeConfig(enabled = true, minDistance = minDragDistance),
        ),
        dispatcher,
    )

/**
 * Convenience wrapper configured for corner swipe detection only.
 */
fun cornerSwipePlugin(
    context: Context,
    cornerRadiusDp: Dp = 48.dp,
    enabledCorners: Set<CornerType> = CornerType.entries.toSet(),
    dispatcher: (TouchEvent) -> Unit,
): GesturePlugin =
    TouchPlugin(
        TouchConfig(
            cornerSwipe =
                CornerSwipeConfig(
                    enabled = true,
                    cornerRadiusDp = cornerRadiusDp,
                    enabledCorners = enabledCorners,
                ),
        ),
        dispatcher,
    )

/**
 * Convenience wrapper configured for pinch/scale detection only.
 */
fun pinchScalePlugin(
    context: Context,
    dispatcher: (TouchEvent) -> Unit,
): GesturePlugin =
    TouchPlugin(
        TouchConfig(
            pinchScale = PinchScaleConfig(enabled = true),
        ),
        dispatcher,
    )

/**
 * Creates a sound level detection plugin.
 *
 * Requires `RECORD_AUDIO` permission at runtime. Captures raw audio from the
 * microphone via [android.media.AudioRecord] with `VOICE_RECOGNITION` source
 * to compute sound pressure levels (RMS → dB). No audio data is stored,
 * transmitted, or persisted — only the computed decibel level is exposed.
 *
 * `RECORD_AUDIO` is a runtime (dangerous) permission on all API levels 23+.
 * A runtime permission request must be shown to the user before this plugin
 * can start capturing audio.
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

private class ClapPlugin(
    private val context: Context,
    private val trigger: ClapTrigger,
    private val dispatcher: (ClapEvent) -> Unit,
) : GesturePlugin {
    override val key = ClapPlugin::class.java.name
    private var detector: ClapDetector? = null

    override fun onRegister(sensey: Sensey) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            android.util.Log.w("Sensey", "RECORD_AUDIO permission not granted — ClapPlugin disabled")
            return
        }
        detector = ClapDetector(trigger, dispatcher)
        detector?.start()
    }

    override fun onUnregister(sensey: Sensey) {
        detector?.stop()
        detector = null
    }
}
