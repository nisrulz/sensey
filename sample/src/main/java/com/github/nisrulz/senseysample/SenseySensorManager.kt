package com.github.nisrulz.senseysample

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.audio.clap.ClapEvent
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.chopPlugin
import com.github.nisrulz.sensey.gesture.clapPlugin
import com.github.nisrulz.sensey.gesture.deviceSpinPlugin
import com.github.nisrulz.sensey.gesture.devicespin.DeviceSpinEvent
import com.github.nisrulz.sensey.gesture.diagonalSwipePlugin
import com.github.nisrulz.sensey.gesture.diagonalswipe.DiagonalSwipeEvent
import com.github.nisrulz.sensey.gesture.edgeSwipePlugin
import com.github.nisrulz.sensey.gesture.edgeswipe.Edge
import com.github.nisrulz.sensey.gesture.edgeswipe.EdgeSwipeEvent
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.flipPlugin
import com.github.nisrulz.sensey.gesture.headShakePlugin
import com.github.nisrulz.sensey.gesture.headshake.HeadShakeEvent
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.lightPlugin
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.movementPlugin
import com.github.nisrulz.sensey.gesture.nodGesturePlugin
import com.github.nisrulz.sensey.gesture.nodgesture.NodGestureEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.orientationPlugin
import com.github.nisrulz.sensey.gesture.pickupDevicePlugin
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.pinchScalePlugin
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleEvent
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
import com.github.nisrulz.sensey.gesture.touchTypePlugin
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeEvent
import com.github.nisrulz.sensey.gesture.turnOverPlugin
import com.github.nisrulz.sensey.gesture.turnover.TurnOverEvent
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wavePlugin
import com.github.nisrulz.sensey.gesture.wristTwistPlugin
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent
import com.github.nisrulz.senseysample.utils.HapticUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

internal class SenseySensorManager(
    private val activity: Activity,
    private val logTag: String,
    val onSensorUnavailable: (String) -> Unit = {},
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
        const val PINCH_SCALE = "Pinch Scale Detection"
        const val EDGE_SWIPE = "Edge Swipe"
        const val DIAGONAL_SWIPE = "Diagonal Swipe"
    }

    var selectedSensor by mutableStateOf<String?>(null)
    private val resultsMap = mutableStateMapOf<String, String>()
    var sensey: Sensey? = null

    private var currentPlugin: GesturePlugin? = null
    private var pendingSensor: String? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun getResult(sensor: String): String = resultsMap[sensor] ?: ""

    private fun <T> withHaptic(dispatcher: (T) -> Unit): (T) -> Unit =
        { event ->
            HapticUtil.quickTap(activity)
            dispatcher(event)
        }

    private val soundLevelDispatcher: (SoundLevelEvent) -> Unit =
        { event: SoundLevelEvent ->
            setResultText("${DecimalFormat("##.##").format(event.level.toDouble())} dB", true)
        }

    private val clapDispatcher: (ClapEvent) -> Unit =
        withHaptic { setResultText("Clap Detected!", false) }

    private val shakeDispatcher: (ShakeEvent) -> Unit =
        withHaptic { event: ShakeEvent ->
            when (event) {
                ShakeEvent.Detected -> setResultText("Shake Detected!", false)
                ShakeEvent.Stopped -> setResultText("Shake Stopped!", false)
            }
        }

    private val flipDispatcher: (FlipEvent) -> Unit =
        withHaptic { event: FlipEvent ->
            when (event) {
                FlipEvent.FaceUp -> setResultText("Face UP", false)
                FlipEvent.FaceDown -> setResultText("Face Down", false)
            }
        }

    private val lightDispatcher: (LightEvent) -> Unit =
        withHaptic { event: LightEvent ->
            when (event) {
                LightEvent.Dark -> setResultText("Dark", false)
                LightEvent.Light -> setResultText("Not Dark", false)
            }
        }

    private val orientationDispatcher: (OrientationEvent) -> Unit =
        withHaptic { event: OrientationEvent ->
            val text =
                when (event) {
                    OrientationEvent.TopSideUp -> "Top Side UP"
                    OrientationEvent.BottomSideUp -> "Bottom Side UP"
                    OrientationEvent.LeftSideUp -> "Left Side UP"
                    OrientationEvent.RightSideUp -> "Right Side UP"
                }
            setResultText(text, false)
        }

    private val proximityDispatcher: (ProximityEvent) -> Unit =
        withHaptic { event: ProximityEvent ->
            when (event) {
                ProximityEvent.Near -> setResultText("Near", false)
                ProximityEvent.Far -> setResultText("Far", false)
            }
        }

    private val waveDispatcher: (WaveEvent) -> Unit = withHaptic { setResultText("Wave Detected!", false) }

    private val movementDispatcher: (MovementEvent) -> Unit =
        withHaptic { event: MovementEvent ->
            when (event) {
                is MovementEvent.Moved -> setResultText("Movement Detected!", false)
                is MovementEvent.Stationary -> setResultText("Device Stationary!", false)
            }
        }

    private val chopDispatcher: (ChopEvent) -> Unit = withHaptic { setResultText("Chop Detected!", false) }

    private val wristTwistDispatcher: (WristTwistEvent) -> Unit =
        withHaptic { setResultText("Wrist Twist Detected!", false) }

    private val rotationAngleDispatcher: (RotationAngleEvent) -> Unit =
        withHaptic { event: RotationAngleEvent ->
            setResultText(
                "Rotation in Axis Detected(deg):\nX=${event.angleInAxisX},\nY=${event.angleInAxisY},\nZ=${event.angleInAxisZ}",
                true,
            )
        }

    private val tiltDirectionDispatcher: (TiltDirectionEvent) -> Unit =
        withHaptic { event: TiltDirectionEvent ->
            val (label, axis) =
                when (event) {
                    is TiltDirectionEvent.AxisXTilt -> Pair(event.direction, "X")
                    is TiltDirectionEvent.AxisYTilt -> Pair(event.direction, "Y")
                    is TiltDirectionEvent.AxisZTilt -> Pair(event.direction, "Z")
                }
            val dir = if (label == TiltDirectionEvent.Direction.CLOCKWISE) "ClockWise" else "AntiClockWise"
            setResultText("Tilt in $axis Axis: $dir", false)
        }

    private val stepDispatcher: (StepEvent) -> Unit =
        withHaptic { event: StepEvent ->
            val typeOfActivity =
                when (event.activityType) {
                    StepDetectorUtil.ACTIVITY_RUNNING -> "Running"
                    StepDetectorUtil.ACTIVITY_WALKING -> "Walking"
                    else -> "Still"
                }
            setResultText(
                "Steps: ${event.steps}\nDistance: ${event.distanceInMeters} m\nActivity Type: $typeOfActivity",
                true,
            )
        }

    private val pickupDeviceDispatcher: (PickupDeviceEvent) -> Unit =
        withHaptic { event: PickupDeviceEvent ->
            when (event) {
                PickupDeviceEvent.PickedUp -> setResultText("Device Picked up Detected!", false)
                PickupDeviceEvent.PutDown -> setResultText("Device Put down Detected!", false)
            }
        }

    private val scoopDispatcher: (ScoopEvent) -> Unit = withHaptic { setResultText("Scoop Gesture Detected!", false) }

    private val tapOnBackDispatcher: (TapOnBackEvent) -> Unit =
        withHaptic { setResultText("Tap On Back Detected!", false) }

    private val edgeSwipeDispatcher: (EdgeSwipeEvent) -> Unit =
        withHaptic { event ->
            setResultText("Edge Swipe: ${event.edge}", false)
        }

    private val diagonalSwipeDispatcher: (DiagonalSwipeEvent) -> Unit =
        withHaptic { event ->
            setResultText("Diagonal Swipe: ${event.direction}", false)
        }

    private val turnOverDispatcher: (TurnOverEvent) -> Unit =
        withHaptic { setResultText("Turn Over Detected!", false) }

    private val nodGestureDispatcher: (NodGestureEvent) -> Unit =
        withHaptic { setResultText("Nod Detected!", false) }

    private val headShakeDispatcher: (HeadShakeEvent) -> Unit =
        withHaptic { setResultText("Head Shake Detected!", false) }

    private val deviceSpinDispatcher: (DeviceSpinEvent) -> Unit =
        withHaptic { setResultText("Device Spin Detected!", false) }

    private val raiseToEarDispatcher: (RaiseToEarEvent) -> Unit =
        withHaptic { setResultText("Raised To Ear!", false) }

    private val touchTypeDispatcher: (TouchTypeEvent) -> Unit =
        { event ->
            val text =
                when (event) {
                    is TouchTypeEvent.NTap -> "${event.count}-Tap"
                    TouchTypeEvent.DoubleTap -> "Double Tap"
                    TouchTypeEvent.LongPress -> "Long press"
                    TouchTypeEvent.SingleTap -> "Single Tap"
                    is TouchTypeEvent.Swipe -> swipeDirText(event.direction)
                    is TouchTypeEvent.Scroll -> scrollDirText(event.direction)
                    TouchTypeEvent.ThreeFingerSingleTap -> "Three Finger Tap"
                    TouchTypeEvent.TwoFingerSingleTap -> "Two Finger Tap"
                }
            setTouchResult(text)
        }

    private val pinchScaleDispatcher: (PinchScaleEvent) -> Unit =
        { event ->
            setTouchResult(if (event.isScalingOut) "Scaling Out" else "Scaling In")
        }

    private fun swipeDirText(dir: TouchTypeEvent.Direction): String =
        when (dir) {
            TouchTypeEvent.Direction.UP -> "Swipe Up"
            TouchTypeEvent.Direction.DOWN -> "Swipe Down"
            TouchTypeEvent.Direction.LEFT -> "Swipe Left"
            TouchTypeEvent.Direction.RIGHT -> "Swipe Right"
            TouchTypeEvent.Direction.UP_RIGHT -> "Swipe Up-Right"
            TouchTypeEvent.Direction.UP_LEFT -> "Swipe Up-Left"
            TouchTypeEvent.Direction.DOWN_RIGHT -> "Swipe Down-Right"
            TouchTypeEvent.Direction.DOWN_LEFT -> "Swipe Down-Left"
        }

    private fun scrollDirText(dir: TouchTypeEvent.Direction): String =
        when (dir) {
            TouchTypeEvent.Direction.UP -> "Scrolling Up"
            TouchTypeEvent.Direction.DOWN -> "Scrolling Down"
            TouchTypeEvent.Direction.LEFT -> "Scrolling Left"
            TouchTypeEvent.Direction.RIGHT -> "Scrolling Right"
            TouchTypeEvent.Direction.UP_RIGHT -> "Scrolling Up-Right"
            TouchTypeEvent.Direction.UP_LEFT -> "Scrolling Up-Left"
            TouchTypeEvent.Direction.DOWN_RIGHT -> "Scrolling Down-Right"
            TouchTypeEvent.Direction.DOWN_LEFT -> "Scrolling Down-Left"
        }

    private fun setTouchResult(text: String) {
        val active = selectedSensor
        if (active == TOUCH_DETECTION || active == PINCH_SCALE) {
            resultsMap[active] = text
            clearJobs[active]?.cancel()
            clearJobs[active] =
                scope.launch {
                    delay(3000)
                    resultsMap.remove(active)
                    clearJobs.remove(active)
                }
        }
    }

    val sensors =
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
        )

    fun onSensorSelected(
        sensor: String,
        hasRecordAudioPermission: Boolean,
        onPermissionNeeded: () -> Unit,
    ) {
        val previous = selectedSensor
        if (previous == sensor) {
            handleStartDetector(sensor, start = false)
            selectedSensor = null
            return
        }
        if (previous != null) {
            handleStartDetector(previous, start = false)
        }
        if ((sensor == SOUND_LEVEL || sensor == CLAP) && !hasRecordAudioPermission) {
            pendingSensor = sensor
            onPermissionNeeded()
            return
        }
        selectedSensor = sensor
        handleStartDetector(sensor, start = true)
    }

    fun startAfterPermissionGranted() {
        val sensor = pendingSensor ?: return
        if (sensey == null) return
        pendingSensor = null
        selectedSensor = sensor
        handleStartDetector(sensor, start = true)
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

    private fun handleStartDetector(
        sensor: String,
        start: Boolean,
    ) {
        if (!start) {
            currentPlugin?.let { sensey?.unregister(it) }
            currentPlugin = null
            resultsMap.remove(sensor)
            return
        }
        if (!isSensorAvailable(sensor)) {
            selectedSensor = null
            onSensorUnavailable(sensor)
            return
        }
        val plugin: GesturePlugin = createPlugin(sensor)
        sensey?.register(plugin)
        currentPlugin = plugin
    }

    private fun isSensorAvailable(sensor: String): Boolean {
        val sensorType = sensorTypeFor(sensor) ?: return true
        val manager = activity.getSystemService(SensorManager::class.java)
        return manager?.getDefaultSensor(sensorType) != null
    }

    private fun sensorTypeFor(sensor: String): Int? =
        when (sensor) {
            SHAKE, FLIP, MOVEMENT, SCOOP, PICKUP_DEVICE, TAP_ON_BACK -> Sensor.TYPE_ACCELEROMETER
            CHOP, WRIST_TWIST -> Sensor.TYPE_LINEAR_ACCELERATION
            LIGHT -> Sensor.TYPE_LIGHT
            PROXIMITY, WAVE -> Sensor.TYPE_PROXIMITY
            TURN_OVER, DEVICE_SPIN, TILT_DIRECTION, NOD_GESTURE, HEAD_SHAKE -> Sensor.TYPE_GYROSCOPE
            STEP -> Sensor.TYPE_STEP_COUNTER
            ROTATION_ANGLE -> Sensor.TYPE_ROTATION_VECTOR
            ORIENTATION -> Sensor.TYPE_ACCELEROMETER
            RAISE_TO_EAR -> Sensor.TYPE_PROXIMITY
            else -> null
        }

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
            TOUCH_DETECTION -> touchTypePlugin(activity, dispatcher = touchTypeDispatcher)
            PINCH_SCALE -> pinchScalePlugin(activity, dispatcher = pinchScaleDispatcher)
            DIAGONAL_SWIPE -> diagonalSwipePlugin(activity, dispatcher = diagonalSwipeDispatcher)
            EDGE_SWIPE ->
                edgeSwipePlugin(
                    activity,
                    edgeThresholdDp = 48.dp,
                    enabledEdges = setOf(Edge.LEFT, Edge.RIGHT, Edge.TOP, Edge.BOTTOM),
                    dispatcher = edgeSwipeDispatcher,
                )

            else -> error("Unknown sensor: $sensor")
        }

    fun cancel() {
        scope.cancel()
    }

    private val clearJobs = mutableMapOf<String, kotlinx.coroutines.Job>()

    private fun setResultText(
        text: String,
        realtime: Boolean,
    ) {
        val sensor = selectedSensor ?: return
        resultsMap[sensor] = text
        if (BuildConfig.DEBUG) Log.d(logTag, text)
        clearJobs[sensor]?.cancel()
        clearJobs[sensor] =
            scope.launch {
                delay(2000L)
                resultsMap.remove(sensor)
                clearJobs.remove(sensor)
            }
    }
}
