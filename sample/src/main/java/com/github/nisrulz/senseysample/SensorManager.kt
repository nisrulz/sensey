package com.github.nisrulz.senseysample

import android.app.Activity
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleEvent
import com.github.nisrulz.sensey.gesture.scoop.ScoopEvent
import com.github.nisrulz.sensey.gesture.shake.ShakeEvent
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelEvent
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackEvent
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent
import com.github.nisrulz.sensey.gesture.step.StepDetectorUtil
import com.github.nisrulz.sensey.gesture.step.StepEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.senseysample.utils.HapticUtil
import java.text.DecimalFormat

internal class SensorManager(
    private val activity: Activity,
    private val logTag: String,
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
    }

    var resultText by mutableStateOf("Results show here")
    var selectedSensor by mutableStateOf<String?>(null)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var resetJob: Job? = null

    private fun <T> withHaptic(dispatcher: (T) -> Unit): (T) -> Unit = { event ->
        HapticUtil.quickTap(activity)
        dispatcher(event)
    }

    private val soundLevelDispatcher: (SoundLevelEvent) -> Unit = withHaptic { event: SoundLevelEvent ->
        setResultText("${DecimalFormat("##.##").format(event.level.toDouble())} dB", true)
    }

    private val shakeDispatcher: (ShakeEvent) -> Unit = withHaptic { event: ShakeEvent ->
        when (event) {
            ShakeEvent.Detected -> setResultText("Shake Detected!", false)
            ShakeEvent.Stopped -> setResultText("Shake Stopped!", false)
        }
    }

    private val flipDispatcher: (FlipEvent) -> Unit = withHaptic { event: FlipEvent ->
        when (event) {
            FlipEvent.FaceUp -> setResultText("Face UP", false)
            FlipEvent.FaceDown -> setResultText("Face Down", false)
        }
    }

    private val lightDispatcher: (LightEvent) -> Unit = withHaptic { event: LightEvent ->
        when (event) {
            LightEvent.Dark -> setResultText("Dark", false)
            LightEvent.Light -> setResultText("Not Dark", false)
        }
    }

    private val orientationDispatcher: (OrientationEvent) -> Unit = withHaptic { event: OrientationEvent ->
        val text = when (event) {
            OrientationEvent.TopSideUp -> "Top Side UP"
            OrientationEvent.BottomSideUp -> "Bottom Side UP"
            OrientationEvent.LeftSideUp -> "Left Side UP"
            OrientationEvent.RightSideUp -> "Right Side UP"
        }
        setResultText(text, false)
    }

    private val proximityDispatcher: (ProximityEvent) -> Unit = withHaptic { event: ProximityEvent ->
        when (event) {
            ProximityEvent.Near -> setResultText("Near", false)
            ProximityEvent.Far -> setResultText("Far", false)
        }
    }

    private val waveDispatcher: (WaveEvent) -> Unit = withHaptic { setResultText("Wave Detected!", false) }

    private val movementDispatcher: (MovementEvent) -> Unit = withHaptic { event: MovementEvent ->
        when (event) {
            MovementEvent.Moved -> setResultText("Movement Detected!", false)
            MovementEvent.Stationary -> setResultText("Device Stationary!", false)
        }
    }

    private val chopDispatcher: (ChopEvent) -> Unit = withHaptic { setResultText("Chop Detected!", false) }

    private val wristTwistDispatcher: (WristTwistEvent) -> Unit = withHaptic { setResultText("Wrist Twist Detected!", false) }

    private val rotationAngleDispatcher: (RotationAngleEvent) -> Unit = withHaptic { event: RotationAngleEvent ->
        setResultText(
            "Rotation in Axis Detected(deg):\nX=${event.angleInAxisX},\nY=${event.angleInAxisY},\nZ=${event.angleInAxisZ}",
            true,
        )
    }

    private val tiltDirectionDispatcher: (TiltDirectionEvent) -> Unit = withHaptic { event: TiltDirectionEvent ->
        val (label, axis) = when (event) {
            is TiltDirectionEvent.AxisXTilt -> Pair(event.direction, "X")
            is TiltDirectionEvent.AxisYTilt -> Pair(event.direction, "Y")
            is TiltDirectionEvent.AxisZTilt -> Pair(event.direction, "Z")
        }
        val dir = if (label == TiltDirectionEvent.Direction.CLOCKWISE) "ClockWise" else "AntiClockWise"
        setResultText("Tilt in $axis Axis: $dir", false)
    }

    private val stepDispatcher: (StepEvent) -> Unit = withHaptic { event: StepEvent ->
        val typeOfActivity = when (event.activityType) {
            StepDetectorUtil.ACTIVITY_RUNNING -> "Running"
            StepDetectorUtil.ACTIVITY_WALKING -> "Walking"
            else -> "Still"
        }
        setResultText(
            "Steps: ${event.steps}\nDistance: ${event.distanceInMeters} m\nActivity Type: $typeOfActivity",
            true,
        )
    }

    private val pickupDeviceDispatcher: (PickupDeviceEvent) -> Unit = withHaptic { event: PickupDeviceEvent ->
        when (event) {
            PickupDeviceEvent.PickedUp -> setResultText("Device Picked up Detected!", false)
            PickupDeviceEvent.PutDown -> setResultText("Device Put down Detected!", false)
        }
    }

    private val scoopDispatcher: (ScoopEvent) -> Unit = withHaptic { setResultText("Scoop Gesture Detected!", false) }

    private val tapOnBackDispatcher: (TapOnBackEvent) -> Unit = withHaptic {
        setResultText("Tap On Back Detected!", false)
    }

    val sensors = listOf(
        SHAKE, FLIP, ORIENTATION, PROXIMITY, LIGHT, WAVE,
        SOUND_LEVEL, MOVEMENT, CHOP, WRIST_TWIST, ROTATION_ANGLE,
        TILT_DIRECTION, STEP, PICKUP_DEVICE, SCOOP, TAP_ON_BACK,
    )

    fun onSensorSelected(sensor: String, hasRecordAudioPermission: Boolean, onPermissionNeeded: () -> Unit) {
        val previous = selectedSensor
        if (previous == sensor) {
            handleStartDetector(sensor, start = false)
            selectedSensor = null
            return
        }
        if (previous != null) {
            handleStartDetector(previous, start = false)
        }
        if (sensor == SOUND_LEVEL && !hasRecordAudioPermission) {
            onPermissionNeeded()
            return
        }
        handleStartDetector(sensor, start = true)
        selectedSensor = sensor
    }

    fun startAfterPermissionGranted() {
        selectedSensor = SOUND_LEVEL
        handleStartDetector(SOUND_LEVEL, start = true)
    }

    fun stopSelectedDetector() {
        val current = selectedSensor ?: return
        handleStartDetector(current, start = false)
        selectedSensor = null
    }

    private fun handleStartDetector(sensor: String, start: Boolean) {
        if (!start) {
            when (sensor) {
                SHAKE -> Sensey.stopShakeDetection()
                FLIP -> Sensey.stopFlipDetection()
                ORIENTATION -> Sensey.stopOrientationDetection()
                PROXIMITY -> Sensey.stopProximityDetection()
                LIGHT -> Sensey.stopLightDetection()
                WAVE -> Sensey.stopWaveDetection()
                SOUND_LEVEL -> Sensey.stopSoundLevelDetection()
                MOVEMENT -> Sensey.stopMovementDetection()
                CHOP -> Sensey.stopChopDetection()
                WRIST_TWIST -> Sensey.stopWristTwistDetection()
                ROTATION_ANGLE -> Sensey.stopRotationAngleDetection()
                TILT_DIRECTION -> Sensey.stopTiltDirectionDetection()
                STEP -> Sensey.stopStepDetection()
                PICKUP_DEVICE -> Sensey.stopPickupDeviceDetection()
                SCOOP -> Sensey.stopScoopDetection()
                TAP_ON_BACK -> Sensey.stopTapOnBackDetection()
            }
            return
        }
        when (sensor) {
            SHAKE -> Sensey.startShakeDetection(10f, 2000, shakeDispatcher)
            FLIP -> Sensey.startFlipDetection(flipDispatcher)
            ORIENTATION -> Sensey.startOrientationDetection(orientationDispatcher)
            PROXIMITY -> Sensey.startProximityDetection(proximityDispatcher)
            LIGHT -> Sensey.startLightDetection(10f, lightDispatcher)
            WAVE -> Sensey.startWaveDetection(waveDispatcher)
            SOUND_LEVEL -> Sensey.startSoundLevelDetection(activity, soundLevelDispatcher)
            MOVEMENT -> Sensey.startMovementDetection(movementDispatcher)
            CHOP -> Sensey.startChopDetection(30f, 500, chopDispatcher)
            WRIST_TWIST -> Sensey.startWristTwistDetection(wristTwistDispatcher)
            ROTATION_ANGLE -> Sensey.startRotationAngleDetection(rotationAngleDispatcher)
            TILT_DIRECTION -> Sensey.startTiltDirectionDetection(tiltDirectionDispatcher)
            STEP -> Sensey.startStepDetection(activity, stepDispatcher, StepDetectorUtil.MALE)
            PICKUP_DEVICE -> Sensey.startPickupDeviceDetection(pickupDeviceDispatcher)
            SCOOP -> Sensey.startScoopDetection(scoopDispatcher)
            TAP_ON_BACK -> Sensey.startTapOnBackDetection(tapOnBackDispatcher)
        }
    }

    fun cancel() {
        scope.cancel()
    }

    private fun setResultText(text: String, realtime: Boolean) {
        resultText = text
        if (!realtime) {
            resetJob?.cancel()
            resetJob = scope.launch {
                delay(3000)
                resultText = "Results show here"
            }
        }
        if (BuildConfig.DEBUG) Log.d(logTag, text)
    }
}
