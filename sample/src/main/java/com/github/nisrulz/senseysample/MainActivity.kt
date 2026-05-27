@file:Suppress("DEPRECATION")

package com.github.nisrulz.senseysample

import android.Manifest.permission
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.gesture.step.StepDetectorUtil
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
import com.github.nisrulz.sensey.gesture.step.StepEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionTrigger
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent
import com.github.nisrulz.senseysample.ui.MainScreen
import com.github.nisrulz.senseysample.ui.SensorItem
import com.github.nisrulz.senseysample.utils.RPResultListener
import com.github.nisrulz.senseysample.utils.RuntimePermissionUtil
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {

    private var hasRecordAudioPermission = false
    private val recordAudioPermission = permission.RECORD_AUDIO
    private val logTag = javaClass.canonicalName
    private val handler = Handler(Looper.getMainLooper())

    private var resultText by mutableStateOf("Results show here")
    private var isRealtimeResult by mutableStateOf(false)
    private var switchStates by mutableStateOf(sensors.associateWith { false })

    private val shakeDispatcher: (ShakeEvent) -> Unit = { event ->
        when (event) {
            ShakeEvent.Detected -> setResultText("Shake Detected!", false)
            ShakeEvent.Stopped -> setResultText("Shake Stopped!", false)
        }
    }
    private val flipDispatcher: (FlipEvent) -> Unit = { event ->
        when (event) {
            FlipEvent.FaceUp -> setResultText("Face UP", false)
            FlipEvent.FaceDown -> setResultText("Face Down", false)
        }
    }
    private val lightDispatcher: (LightEvent) -> Unit = { event ->
        when (event) {
            LightEvent.Dark -> setResultText("Dark", false)
            LightEvent.Light -> setResultText("Not Dark", false)
        }
    }
    private val orientationDispatcher: (OrientationEvent) -> Unit = { event ->
        val text = when (event) {
            OrientationEvent.TopSideUp -> "Top Side UP"
            OrientationEvent.BottomSideUp -> "Bottom Side UP"
            OrientationEvent.LeftSideUp -> "Left Side UP"
            OrientationEvent.RightSideUp -> "Right Side UP"
        }
        setResultText(text, false)
    }
    private val proximityDispatcher: (ProximityEvent) -> Unit = { event ->
        when (event) {
            ProximityEvent.Near -> setResultText("Near", false)
            ProximityEvent.Far -> setResultText("Far", false)
        }
    }
    private val waveDispatcher: (WaveEvent) -> Unit = { setResultText("Wave Detected!", false) }
    private val movementDispatcher: (MovementEvent) -> Unit = { event ->
        when (event) {
            MovementEvent.Moved -> setResultText("Movement Detected!", false)
            MovementEvent.Stationary -> setResultText("Device Stationary!", false)
        }
    }
    private val chopDispatcher: (ChopEvent) -> Unit = { setResultText("Chop Detected!", false) }
    private val wristTwistDispatcher: (WristTwistEvent) -> Unit =
        { setResultText("Wrist Twist Detected!", false) }
    private val rotationAngleDispatcher: (RotationAngleEvent) -> Unit = { event ->
        setResultText(
            "Rotation in Axis Detected(deg):\nX=${event.angleInAxisX},\nY=${event.angleInAxisY},\nZ=${event.angleInAxisZ}",
            true,
        )
    }
    private val tiltDirectionDispatcher: (TiltDirectionEvent) -> Unit = { event ->
        val (label, axis) = when (event) {
            is TiltDirectionEvent.AxisXTilt -> Pair(event.direction, "X")
            is TiltDirectionEvent.AxisYTilt -> Pair(event.direction, "Y")
            is TiltDirectionEvent.AxisZTilt -> Pair(event.direction, "Z")
        }
        val dir = if (label == TiltDirectionTrigger.DIRECTION_CLOCKWISE) "ClockWise" else "AntiClockWise"
        setResultText("Tilt in $axis Axis: $dir", false)
    }
    private val stepDispatcher: (StepEvent) -> Unit = { event ->
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
    private val pickupDeviceDispatcher: (PickupDeviceEvent) -> Unit = { event ->
        when (event) {
            PickupDeviceEvent.PickedUp -> setResultText("Device Picked up Detected!", false)
            PickupDeviceEvent.PutDown -> setResultText("Device Put down Detected!", false)
        }
    }
    private val scoopDispatcher: (ScoopEvent) -> Unit =
        { setResultText("Scoop Gesture Detected!", false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        hasRecordAudioPermission =
            RuntimePermissionUtil.checkPermissonGranted(this, recordAudioPermission)

        setContent {
            MainScreen(
                sensors = sensors.map { label ->
                    SensorItem(
                        label = label,
                        isChecked = switchStates[label] ?: false,
                        onToggle = { isChecked ->
                            switchStates = switchStates.toMutableMap().apply {
                                put(label, isChecked)
                            }
                            handleSensorToggle(label, isChecked)
                        },
                    )
                },
                resultText = resultText,
                onTouchDetectorClick = {
                    startActivity(Intent(this@MainActivity, TouchActivity::class.java))
                },
            )
        }
    }

    override fun onPause() {
        super.onPause()
        stopAllDetectors()
        Sensey.getInstance().stop()
    }

    override fun onResume() {
        super.onResume()
        Sensey.getInstance().init(this)
    }

    private fun stopAllDetectors() {
        Sensey.getInstance().let {
            it.stopShakeDetection(shakeDispatcher)
            it.stopFlipDetection(flipDispatcher)
            it.stopOrientationDetection(orientationDispatcher)
            it.stopProximityDetection(proximityDispatcher)
            it.stopLightDetection(lightDispatcher)
            it.stopWaveDetection(waveDispatcher)
            it.stopSoundLevelDetection()
            it.stopMovementDetection(movementDispatcher)
            it.stopChopDetection(chopDispatcher)
            it.stopWristTwistDetection(wristTwistDispatcher)
            it.stopRotationAngleDetection(rotationAngleDispatcher)
            it.stopTiltDirectionDetection(tiltDirectionDispatcher)
            it.stopStepDetection(stepDispatcher)
            it.stopPickupDeviceDetection(pickupDeviceDispatcher)
            it.stopScoopDetection(scoopDispatcher)
        }
    }

    private fun handleSensorToggle(sensor: String, isChecked: Boolean) {
        Sensey.getInstance().let {
            when (sensor) {
                "Shake Gesture" ->
                    if (isChecked) it.startShakeDetection(10f, 2000, shakeDispatcher)
                    else it.stopShakeDetection(shakeDispatcher)

                "Flip Gesture" ->
                    if (isChecked) it.startFlipDetection(flipDispatcher)
                    else it.stopFlipDetection(flipDispatcher)

                "Orientation Gesture" ->
                    if (isChecked) it.startOrientationDetection(orientationDispatcher)
                    else it.stopOrientationDetection(orientationDispatcher)

                "Proximity Gesture" ->
                    if (isChecked) it.startProximityDetection(proximityDispatcher)
                    else it.stopProximityDetection(proximityDispatcher)

                "Light Detection" ->
                    if (isChecked) it.startLightDetection(10f, lightDispatcher)
                    else it.stopLightDetection(lightDispatcher)

                "Wave Detection" ->
                    if (isChecked) it.startWaveDetection(waveDispatcher)
                    else it.stopWaveDetection(waveDispatcher)

                "Sound Level Detection" ->
                    if (isChecked) {
                        if (hasRecordAudioPermission) {
                            it.startSoundLevelDetection(this, soundLevelDispatcher)
                        } else {
                            RuntimePermissionUtil.requestPermission(
                                this,
                                recordAudioPermission,
                                100,
                            )
                        }
                    } else {
                        it.stopSoundLevelDetection()
                    }

                "Movement Detection" ->
                    if (isChecked) it.startMovementDetection(movementDispatcher)
                    else it.stopMovementDetection(movementDispatcher)

                "Chop Detector" ->
                    if (isChecked) it.startChopDetection(30f, 500, chopDispatcher)
                    else it.stopChopDetection(chopDispatcher)

                "Wrist Twist Detection" ->
                    if (isChecked) it.startWristTwistDetection(wristTwistDispatcher)
                    else it.stopWristTwistDetection(wristTwistDispatcher)

                "Rotation Angle Detection" ->
                    if (isChecked) it.startRotationAngleDetection(rotationAngleDispatcher)
                    else it.stopRotationAngleDetection(rotationAngleDispatcher)

                "Tilt Direction Detection" ->
                    if (isChecked) it.startTiltDirectionDetection(tiltDirectionDispatcher)
                    else it.stopTiltDirectionDetection(tiltDirectionDispatcher)

                "Step Detector" ->
                    if (isChecked) it.startStepDetection(this, stepDispatcher, StepDetectorUtil.MALE)
                    else it.stopStepDetection(stepDispatcher)

                "Pickup Device Detector" ->
                    if (isChecked) it.startPickupDeviceDetection(pickupDeviceDispatcher)
                    else it.stopPickupDeviceDetection(pickupDeviceDispatcher)

                "Scoop Detector" ->
                    if (isChecked) it.startScoopDetection(scoopDispatcher)
                    else it.stopScoopDetection(scoopDispatcher)
            }
        }
    }

    private val soundLevelDispatcher: (SoundLevelEvent) -> Unit = { event ->
        setResultText("${DecimalFormat("##.##").format(event.level.toDouble())} dB", true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(
                grantResults,
                object : RPResultListener {
                    override fun onPermissionDenied() {}
                    override fun onPermissionGranted() {
                        if (RuntimePermissionUtil.checkPermissonGranted(
                                this@MainActivity,
                                recordAudioPermission,
                            )
                        ) {
                            hasRecordAudioPermission = true
                            switchStates = switchStates.toMutableMap().apply {
                                put("Sound Level Detection", true)
                            }
                            handleSensorToggle("Sound Level Detection", true)
                        }
                    }
                },
            )
        }
    }

    private fun setResultText(text: String, realtime: Boolean) {
        isRealtimeResult = realtime
        resultText = text
        if (!realtime) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                resultText = "Results show here"
            }, 3000)
        }
        if (BuildConfig.DEBUG) Log.d(logTag, text)
    }

    companion object {
        private val sensors = listOf(
            "Shake Gesture",
            "Flip Gesture",
            "Orientation Gesture",
            "Proximity Gesture",
            "Light Detection",
            "Wave Detection",
            "Sound Level Detection",
            "Movement Detection",
            "Chop Detector",
            "Wrist Twist Detection",
            "Rotation Angle Detection",
            "Tilt Direction Detection",
            "Step Detector",
            "Pickup Device Detector",
            "Scoop Detector",
        )
    }
}
