package com.github.nisrulz.senseysample

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.audio.clap.ClapEvent
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.chopPlugin
import com.github.nisrulz.sensey.gesture.clapPlugin
import com.github.nisrulz.sensey.gesture.cornerSwipePlugin
import com.github.nisrulz.sensey.gesture.deviceSpinPlugin
import com.github.nisrulz.sensey.gesture.devicespin.DeviceSpinEvent
import com.github.nisrulz.sensey.gesture.diagonalSwipePlugin
import com.github.nisrulz.sensey.gesture.edgeSwipePlugin
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.flipPlugin
import com.github.nisrulz.sensey.gesture.headShakePlugin
import com.github.nisrulz.sensey.gesture.headshake.HeadShakeEvent
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.lightPlugin
import com.github.nisrulz.sensey.gesture.longPressDragPlugin
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.movementPlugin
import com.github.nisrulz.sensey.gesture.nodGesturePlugin
import com.github.nisrulz.sensey.gesture.nodgesture.NodGestureEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.orientationPlugin
import com.github.nisrulz.sensey.gesture.pickupDevicePlugin
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.pinchScalePlugin
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.proximityPlugin
import com.github.nisrulz.sensey.gesture.raiseToEarPlugin
import com.github.nisrulz.sensey.gesture.raisetoear.RaiseToEarEvent
import com.github.nisrulz.sensey.gesture.rotationAnglePlugin
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleEvent
import com.github.nisrulz.sensey.gesture.scoop.ScoopEvent
import com.github.nisrulz.sensey.gesture.scoopPlugin
import com.github.nisrulz.sensey.gesture.shake.ShakeEvent
import com.github.nisrulz.sensey.gesture.shakePlugin
import com.github.nisrulz.sensey.gesture.soundLevelPlugin
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelEvent
import com.github.nisrulz.sensey.gesture.step.StepDetectorUtil
import com.github.nisrulz.sensey.gesture.step.StepEvent
import com.github.nisrulz.sensey.gesture.stepPlugin
import com.github.nisrulz.sensey.gesture.tapOnBackPlugin
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackEvent
import com.github.nisrulz.sensey.gesture.tiltDirectionPlugin
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.sensey.gesture.touch.TouchEvent
import com.github.nisrulz.sensey.gesture.touchPlugin
import com.github.nisrulz.sensey.gesture.turnOverPlugin
import com.github.nisrulz.sensey.gesture.turnover.TurnOverEvent
import com.github.nisrulz.sensey.gesture.twoFingerSwipePlugin
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wavePlugin
import com.github.nisrulz.sensey.gesture.wristTwistPlugin
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent
import com.github.nisrulz.senseysample.utils.HapticUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

internal class SenseySensorManager(
    private val activity: Activity,
    private val logTag: String,
    val onSensorUnavailable: (String) -> Unit = {},
    private val onSensorResult: (sensor: String, result: String) -> Unit = { _, _ -> },
) {
    companion object {
        const val SHAKE = "Shake Gesture"
        const val FLIP = "Flip Gesture"
        const val ORIENTATION = "Orientation Gesture"
        const val PROXIMITY = "Proximity Gesture"
        const val LIGHT = "Light Detection"
        const val WAVE = "Wave Detection"
        const val SOUND_LEVEL = "Sound Level Detection"
        const val MOVEMENT = "Movement Detection"
        const val CHOP = "Chop Detector"
        const val WRIST_TWIST = "Wrist Twist Detection"
        const val ROTATION_ANGLE = "Rotation Angle Detection"
        const val TILT_DIRECTION = "Tilt Direction Detection"
        const val STEP = "Step Detector"
        const val PICKUP_DEVICE = "Pickup Device Detector"
        const val SCOOP = "Scoop Detector"
        const val TAP_ON_BACK = "Tap On Back"
        const val TURN_OVER = "Turn Over"
        const val DEVICE_SPIN = "Device Spin"
        const val RAISE_TO_EAR = "Raise To Ear"
        const val CLAP = "Clap Detection"
        const val NOD_GESTURE = "Nod Gesture"
        const val HEAD_SHAKE = "Head Shake"
        const val TOUCH_DETECTION = "Touch Detection"
        const val PINCH_SCALE = "Pinch Scale"
        const val EDGE_SWIPE = "Edge Swipe"
        const val DIAGONAL_SWIPE = "Diagonal Swipe"
        const val LONG_PRESS_DRAG = "Long Press Drag"
        const val TWO_FINGER_SWIPE = "Two Finger Swipe"
        const val CORNER_SWIPE = "Corner Swipe"
    }

    var selectedSensor by mutableStateOf<String?>(null)
    private val resultsMap = mutableStateMapOf<String, String>()
    var sensey: Sensey? = null

    private var currentPlugin: GesturePlugin? = null
    private var pendingSensor: String? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val clearJobs = mutableMapOf<String, Job>()

    val sensors: List<String> =
        listOf(
            SHAKE,
            FLIP,
            ROTATION_ANGLE,
            TILT_DIRECTION,
            ORIENTATION,
            PROXIMITY,
            WAVE,
            LIGHT,
            MOVEMENT,
            CHOP,
            WRIST_TWIST,
            STEP,
            PICKUP_DEVICE,
            SCOOP,
            TAP_ON_BACK,
            TURN_OVER,
            DEVICE_SPIN,
            RAISE_TO_EAR,
            SOUND_LEVEL,
            CLAP,
            NOD_GESTURE,
            HEAD_SHAKE,
            TOUCH_DETECTION,
            PINCH_SCALE,
            EDGE_SWIPE,
            DIAGONAL_SWIPE,
            LONG_PRESS_DRAG,
            TWO_FINGER_SWIPE,
            CORNER_SWIPE,
        )

    fun getResult(sensor: String): String = resultsMap[sensor] ?: ""

    // ── Sensor selection lifecycle ──────────────────────────────────────

    fun onSensorSelected(
        sensor: String,
        hasRecordAudioPermission: Boolean,
        onPermissionNeeded: () -> Unit,
    ) {
        val previous = selectedSensor
        if (previous == sensor) {
            stopDetector(sensor)
            return
        }
        if (previous != null) stopDetector(previous)
        if ((sensor == SOUND_LEVEL || sensor == CLAP) && !hasRecordAudioPermission) {
            pendingSensor = sensor
            onPermissionNeeded()
            return
        }
        selectedSensor = sensor
        startDetector(sensor)
    }

    fun startAfterPermissionGranted() {
        val sensor = pendingSensor ?: return
        if (sensey == null) return
        pendingSensor = null
        selectedSensor = sensor
        startDetector(sensor)
    }

    fun clearPendingPermission() {
        pendingSensor = null
        selectedSensor = null
    }

    fun stopSelectedDetector() {
        currentPlugin?.let { sensey?.unregister(it) }
        currentPlugin = null
        selectedSensor = null
    }

    fun cancel() {
        scope.cancel()
    }

    // ── Detector lifecycle ──────────────────────────────────────────────

    private fun startDetector(sensor: String) {
        if (!isSensorAvailable(sensor)) {
            selectedSensor = null
            onSensorUnavailable(sensor)
            return
        }
        val plugin = createPlugin(sensor)
        sensey?.register(plugin)
        currentPlugin = plugin
    }

    private fun stopDetector(sensor: String) {
        currentPlugin?.let { sensey?.unregister(it) }
        currentPlugin = null
        resultsMap.remove(sensor)
    }

    private fun isSensorAvailable(sensor: String): Boolean {
        val sensorType = sensorTypeFor(sensor) ?: return true
        val manager = activity.getSystemService(SensorManager::class.java)
        return manager?.getDefaultSensor(sensorType) != null
    }

    // ── Sensor type mapping ─────────────────────────────────────────────

    private fun sensorTypeFor(sensor: String): Int? =
        when (sensor) {
            SHAKE, FLIP, MOVEMENT, SCOOP, PICKUP_DEVICE, TAP_ON_BACK, ORIENTATION -> Sensor.TYPE_ACCELEROMETER
            CHOP, WRIST_TWIST -> Sensor.TYPE_LINEAR_ACCELERATION
            LIGHT -> Sensor.TYPE_LIGHT
            PROXIMITY, WAVE, RAISE_TO_EAR -> Sensor.TYPE_PROXIMITY
            TURN_OVER, DEVICE_SPIN, TILT_DIRECTION, NOD_GESTURE, HEAD_SHAKE -> Sensor.TYPE_GYROSCOPE
            STEP -> Sensor.TYPE_STEP_COUNTER
            ROTATION_ANGLE -> Sensor.TYPE_ROTATION_VECTOR
            else -> null
        }

    // ── Plugin creation ─────────────────────────────────────────────────

    private fun createPlugin(sensor: String): GesturePlugin =
        when (sensor) {
            SHAKE -> shakePlugin(threshold = 10f, timeBeforeDeclaringShakeStopped = 2000, dispatcher = shakeDispatcher)
            FLIP -> flipPlugin(dispatcher = flipDispatcher)
            ORIENTATION -> orientationPlugin(dispatcher = orientationDispatcher)
            PROXIMITY -> proximityPlugin(dispatcher = proximityDispatcher)
            LIGHT -> lightPlugin(darkThreshold = 10f, dispatcher = lightDispatcher)
            WAVE -> wavePlugin(dispatcher = waveDispatcher)
            SOUND_LEVEL -> soundLevelPlugin(activity, dispatcher = soundLevelDispatcher)
            MOVEMENT -> movementPlugin(dispatcher = movementDispatcher)
            CHOP -> chopPlugin(threshold = 30f, timeForChopGesture = 500, dispatcher = chopDispatcher)
            WRIST_TWIST -> wristTwistPlugin(dispatcher = wristTwistDispatcher)
            ROTATION_ANGLE -> rotationAnglePlugin(dispatcher = rotationAngleDispatcher)
            TILT_DIRECTION -> tiltDirectionPlugin(dispatcher = tiltDirectionDispatcher)
            STEP -> stepPlugin(gender = StepDetectorUtil.MALE, dispatcher = stepDispatcher)
            PICKUP_DEVICE -> pickupDevicePlugin(dispatcher = pickupDeviceDispatcher)
            SCOOP -> scoopPlugin(dispatcher = scoopDispatcher)
            TAP_ON_BACK -> tapOnBackPlugin(dispatcher = tapOnBackDispatcher)
            TURN_OVER -> turnOverPlugin(dispatcher = turnOverDispatcher)
            DEVICE_SPIN -> deviceSpinPlugin(dispatcher = deviceSpinDispatcher)
            RAISE_TO_EAR -> raiseToEarPlugin(dispatcher = raiseToEarDispatcher)
            CLAP -> clapPlugin(activity, dispatchEvents = clapDispatcher, requiredClaps = 2)
            NOD_GESTURE -> nodGesturePlugin(dispatcher = nodGestureDispatcher)
            HEAD_SHAKE -> headShakePlugin(dispatcher = headShakeDispatcher)
            TOUCH_DETECTION -> touchPlugin(activity, dispatcher = touchDispatcher)
            PINCH_SCALE -> pinchScalePlugin(activity, dispatcher = pinchScaleDispatcher)
            EDGE_SWIPE -> edgeSwipePlugin(activity, dispatcher = edgeSwipeDispatcher)
            DIAGONAL_SWIPE -> diagonalSwipePlugin(activity, dispatcher = diagonalSwipeDispatcher)
            LONG_PRESS_DRAG -> longPressDragPlugin(activity, dispatcher = longPressDragDispatcher)
            TWO_FINGER_SWIPE -> twoFingerSwipePlugin(activity, dispatcher = twoFingerSwipeDispatcher)
            CORNER_SWIPE -> cornerSwipePlugin(activity, dispatcher = cornerSwipeDispatcher)
            else -> error("Unknown sensor: $sensor")
        }

    // ── Haptic helper ───────────────────────────────────────────────────

    private fun <T> withHaptic(dispatcher: (T) -> Unit): (T) -> Unit =
        { event ->
            HapticUtil.quickTap(activity)
            dispatcher(event)
        }

    // ── Dispatchers (event → text) ───────────────────────────────────────

    private val soundLevelDispatcher: (SoundLevelEvent) -> Unit =
        { setResultText("${DecimalFormat("##.##").format(it.level.toDouble())} dB") }

    private val clapDispatcher: (ClapEvent) -> Unit =
        withHaptic { setResultText("Clap Detected!") }

    private val shakeDispatcher: (ShakeEvent) -> Unit =
        withHaptic {
            setResultText(
                when (it) {
                    ShakeEvent.Detected -> "Shake Detected!"
                    ShakeEvent.Stopped -> "Shake Stopped!"
                },
            )
        }

    private val flipDispatcher: (FlipEvent) -> Unit =
        withHaptic {
            setResultText(
                when (it) {
                    FlipEvent.FaceUp -> "Face UP"
                    FlipEvent.FaceDown -> "Face Down"
                },
            )
        }

    private val lightDispatcher: (LightEvent) -> Unit =
        withHaptic {
            setResultText(
                when (it) {
                    LightEvent.Dark -> "Dark"
                    LightEvent.Light -> "Not Dark"
                },
            )
        }

    private val orientationDispatcher: (OrientationEvent) -> Unit =
        withHaptic {
            setResultText(
                when (it) {
                    OrientationEvent.TopSideUp -> "Top Side UP"
                    OrientationEvent.BottomSideUp -> "Bottom Side UP"
                    OrientationEvent.LeftSideUp -> "Left Side UP"
                    OrientationEvent.RightSideUp -> "Right Side UP"
                },
            )
        }

    private val proximityDispatcher: (ProximityEvent) -> Unit =
        withHaptic {
            setResultText(
                when (it) {
                    ProximityEvent.Near -> "Near"
                    ProximityEvent.Far -> "Far"
                },
            )
        }

    private val waveDispatcher: (WaveEvent) -> Unit =
        withHaptic { setResultText("Wave Detected!") }

    private val movementDispatcher: (MovementEvent) -> Unit =
        withHaptic {
            setResultText(
                when (it) {
                    is MovementEvent.Moved -> "Movement Detected!"
                    is MovementEvent.Stationary -> "Device Stationary!"
                },
            )
        }

    private val chopDispatcher: (ChopEvent) -> Unit =
        withHaptic { setResultText("Chop Detected!") }

    private val wristTwistDispatcher: (WristTwistEvent) -> Unit =
        withHaptic { setResultText("Wrist Twist Detected!") }

    private val rotationAngleDispatcher: (RotationAngleEvent) -> Unit =
        withHaptic {
            setResultText(
                "Rotation in Axis Detected(deg):\nX=${it.angleInAxisX},\nY=${it.angleInAxisY},\nZ=${it.angleInAxisZ}",
            )
        }

    private val tiltDirectionDispatcher: (TiltDirectionEvent) -> Unit =
        withHaptic {
            val (label, axis) =
                when (it) {
                    is TiltDirectionEvent.AxisXTilt -> Pair(it.direction, "X")
                    is TiltDirectionEvent.AxisYTilt -> Pair(it.direction, "Y")
                    is TiltDirectionEvent.AxisZTilt -> Pair(it.direction, "Z")
                }
            val dir = if (label == TiltDirectionEvent.Direction.CLOCKWISE) "ClockWise" else "AntiClockWise"
            setResultText("Tilt in $axis Axis: $dir")
        }

    private val stepDispatcher: (StepEvent) -> Unit =
        withHaptic {
            val activity =
                when (it.activityType) {
                    StepDetectorUtil.ACTIVITY_RUNNING -> "Running"
                    StepDetectorUtil.ACTIVITY_WALKING -> "Walking"
                    else -> "Still"
                }
            setResultText("Steps: ${it.steps}\nDistance: ${it.distanceInMeters} m\nActivity Type: $activity")
        }

    private val pickupDeviceDispatcher: (PickupDeviceEvent) -> Unit =
        withHaptic {
            setResultText(
                when (it) {
                    PickupDeviceEvent.PickedUp -> "Device Picked up Detected!"
                    PickupDeviceEvent.PutDown -> "Device Put down Detected!"
                },
            )
        }

    private val scoopDispatcher: (ScoopEvent) -> Unit =
        withHaptic { setResultText("Scoop Gesture Detected!") }

    private val tapOnBackDispatcher: (TapOnBackEvent) -> Unit =
        withHaptic { setResultText("Tap On Back Detected!") }

    private val edgeSwipeDispatcher: (TouchEvent) -> Unit =
        withHaptic {
            if (it is TouchEvent.Swipe) {
                val edge = (it.origin as TouchEvent.SwipeOrigin.Edge).type
                setResultText("Edge Swipe: $edge → ${it.direction}")
            }
        }

    private val diagonalSwipeDispatcher: (TouchEvent) -> Unit =
        withHaptic {
            val direction =
                when (it) {
                    is TouchEvent.Swipe -> it.direction
                    is TouchEvent.Scroll -> it.direction
                    else -> null
                }
            if (direction != null) {
                setResultText("Diagonal Swipe: $direction")
            }
        }

    private val turnOverDispatcher: (TurnOverEvent) -> Unit =
        withHaptic { setResultText("Turn Over Detected!") }

    private val nodGestureDispatcher: (NodGestureEvent) -> Unit =
        withHaptic { setResultText("Nod Detected!") }

    private val headShakeDispatcher: (HeadShakeEvent) -> Unit =
        withHaptic { setResultText("Head Shake Detected!") }

    private val deviceSpinDispatcher: (DeviceSpinEvent) -> Unit =
        withHaptic { setResultText("Device Spin Detected!") }

    private val raiseToEarDispatcher: (RaiseToEarEvent) -> Unit =
        withHaptic { setResultText("Raised To Ear!") }

    private val longPressDragDispatcher: (TouchEvent) -> Unit =
        withHaptic {
            if (it is TouchEvent.LongPressDrag) {
                setResultText("LongPress Drag: ${it.direction}")
            }
        }

    private val twoFingerSwipeDispatcher: (TouchEvent) -> Unit =
        withHaptic {
            if (it is TouchEvent.Swipe) {
                setResultText("Two-Finger Swipe: ${it.direction}")
            }
        }

    private val cornerSwipeDispatcher: (TouchEvent) -> Unit =
        withHaptic {
            if (it is TouchEvent.Swipe) {
                val corner = (it.origin as TouchEvent.SwipeOrigin.Corner).type
                setResultText("Corner Swipe: $corner → ${it.direction}")
            }
        }

    private val touchDispatcher: (TouchEvent) -> Unit =
        withHaptic { event ->
            val text =
                when (event) {
                    is TouchEvent.Tap.NTap -> "${event.count}-Tap"
                    is TouchEvent.Tap.Double -> "Double Tap"
                    is TouchEvent.LongPress -> "Long press"
                    is TouchEvent.Tap.Single -> "Single Tap"
                    is TouchEvent.Swipe -> swipeDirText(event.direction)
                    is TouchEvent.Scroll -> scrollDirText(event.direction)
                    else -> null
                }
            if (text != null) setTouchResult(text)
        }

    private val pinchScaleDispatcher: (TouchEvent) -> Unit =
        withHaptic {
            if (it is TouchEvent.PinchScale) {
                setTouchResult(
                    if (it.isScalingOut) "Scaling Out" else "Scaling In",
                )
            }
        }

    // ── Direction helpers ────────────────────────────────────────────────

    private fun swipeDirText(dir: TouchEvent.Direction): String =
        when (dir) {
            TouchEvent.Direction.UP -> "Swipe Up"
            TouchEvent.Direction.DOWN -> "Swipe Down"
            TouchEvent.Direction.LEFT -> "Swipe Left"
            TouchEvent.Direction.RIGHT -> "Swipe Right"
            TouchEvent.Direction.UP_RIGHT -> "Swipe Up-Right"
            TouchEvent.Direction.UP_LEFT -> "Swipe Up-Left"
            TouchEvent.Direction.DOWN_RIGHT -> "Swipe Down-Right"
            TouchEvent.Direction.DOWN_LEFT -> "Swipe Down-Left"
        }

    private fun scrollDirText(dir: TouchEvent.Direction): String =
        when (dir) {
            TouchEvent.Direction.UP -> "Scrolling Up"
            TouchEvent.Direction.DOWN -> "Scrolling Down"
            TouchEvent.Direction.LEFT -> "Scrolling Left"
            TouchEvent.Direction.RIGHT -> "Scrolling Right"
            TouchEvent.Direction.UP_RIGHT -> "Scrolling Up-Right"
            TouchEvent.Direction.UP_LEFT -> "Scrolling Up-Left"
            TouchEvent.Direction.DOWN_RIGHT -> "Scrolling Down-Right"
            TouchEvent.Direction.DOWN_LEFT -> "Scrolling Down-Left"
        }

    // ── Result display ──────────────────────────────────────────────────

    private fun setResultText(text: String) {
        val sensor = selectedSensor ?: return
        resultsMap[sensor] = text
        onSensorResult(sensor, text)
        if (BuildConfig.DEBUG) Log.d(logTag, text)
        clearJobs[sensor]?.cancel()
        clearJobs[sensor] =
            scope.launch {
                delay(2000L)
                resultsMap.remove(sensor)
                onSensorResult(sensor, "")
                clearJobs.remove(sensor)
            }
    }

    private fun setTouchResult(text: String) {
        val active = selectedSensor
        if (active != TOUCH_DETECTION && active != PINCH_SCALE) return
        resultsMap[active] = text
        onSensorResult(active, text)
        clearJobs[active]?.cancel()
        clearJobs[active] =
            scope.launch {
                delay(3000L)
                resultsMap.remove(active)
                onSensorResult(active, "")
                clearJobs.remove(active)
            }
    }
}
