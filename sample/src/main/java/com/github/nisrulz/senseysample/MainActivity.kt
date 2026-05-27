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
import com.github.nisrulz.sensey.ChopDetector.ChopListener
import com.github.nisrulz.sensey.FlipDetector.FlipListener
import com.github.nisrulz.sensey.LightDetector.LightListener
import com.github.nisrulz.sensey.MovementDetector.MovementListener
import com.github.nisrulz.sensey.OrientationDetector.OrientationListener
import com.github.nisrulz.sensey.PickupDeviceDetector.PickupDeviceListener
import com.github.nisrulz.sensey.ProximityDetector.ProximityListener
import com.github.nisrulz.sensey.RotationAngleDetector.RotationAngleListener
import com.github.nisrulz.sensey.ScoopDetector.ScoopListener
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener
import com.github.nisrulz.sensey.SoundLevelDetector.SoundLevelListener
import com.github.nisrulz.sensey.StepDetectorUtil
import com.github.nisrulz.sensey.StepListener
import com.github.nisrulz.sensey.TiltDirectionDetector
import com.github.nisrulz.sensey.TiltDirectionDetector.TiltDirectionListener
import com.github.nisrulz.sensey.WaveDetector.WaveListener
import com.github.nisrulz.sensey.WristTwistDetector.WristTwistListener
import com.github.nisrulz.senseysample.ui.MainScreen
import com.github.nisrulz.senseysample.ui.SensorItem
import com.github.nisrulz.senseysample.utils.RPResultListener
import com.github.nisrulz.senseysample.utils.RuntimePermissionUtil
import java.text.DecimalFormat

class MainActivity :
    ComponentActivity(),
    ShakeListener,
    FlipListener,
    LightListener,
    OrientationListener,
    ProximityListener,
    WaveListener,
    SoundLevelListener,
    MovementListener,
    ChopListener,
    WristTwistListener,
    RotationAngleListener,
    TiltDirectionListener,
    StepListener,
    ScoopListener,
    PickupDeviceListener {

    private var hasRecordAudioPermission = false
    private val recordAudioPermission = permission.RECORD_AUDIO
    private val logTag = javaClass.canonicalName
    private val handler = Handler(Looper.getMainLooper())

    private var resultText by mutableStateOf("Results show here")
    private var isRealtimeResult by mutableStateOf(false)
    private var switchStates by mutableStateOf(sensors.associateWith { false })

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
            it.stopShakeDetection(this)
            it.stopFlipDetection(this)
            it.stopOrientationDetection(this)
            it.stopProximityDetection(this)
            it.stopLightDetection(this)
            it.stopWaveDetection(this)
            it.stopSoundLevelDetection()
            it.stopMovementDetection(this)
            it.stopChopDetection(this)
            it.stopWristTwistDetection(this)
            it.stopRotationAngleDetection(this)
            it.stopTiltDirectionDetection(this)
            it.stopStepDetection(this)
            it.stopPickupDeviceDetection(this)
            it.stopScoopDetection(this)
        }
    }

    private fun handleSensorToggle(sensor: String, isChecked: Boolean) {
        Sensey.getInstance().let {
            when (sensor) {
                "Shake Gesture" ->
                    if (isChecked) it.startShakeDetection(10f, 2000, this)
                    else it.stopShakeDetection(this)

                "Flip Gesture" ->
                    if (isChecked) it.startFlipDetection(this)
                    else it.stopFlipDetection(this)

                "Orientation Gesture" ->
                    if (isChecked) it.startOrientationDetection(this)
                    else it.stopOrientationDetection(this)

                "Proximity Gesture" ->
                    if (isChecked) it.startProximityDetection(this)
                    else it.stopProximityDetection(this)

                "Light Detection" ->
                    if (isChecked) it.startLightDetection(10f, this)
                    else it.stopLightDetection(this)

                "Wave Detection" ->
                    if (isChecked) it.startWaveDetection(this)
                    else it.stopWaveDetection(this)

                "Sound Level Detection" ->
                    if (isChecked) {
                        if (hasRecordAudioPermission) {
                            it.startSoundLevelDetection(this, this)
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
                    if (isChecked) it.startMovementDetection(this)
                    else it.stopMovementDetection(this)

                "Chop Detector" ->
                    if (isChecked) it.startChopDetection(30f, 500, this)
                    else it.stopChopDetection(this)

                "Wrist Twist Detection" ->
                    if (isChecked) it.startWristTwistDetection(this)
                    else it.stopWristTwistDetection(this)

                "Rotation Angle Detection" ->
                    if (isChecked) it.startRotationAngleDetection(this)
                    else it.stopRotationAngleDetection(this)

                "Tilt Direction Detection" ->
                    if (isChecked) it.startTiltDirectionDetection(this)
                    else it.stopTiltDirectionDetection(this)

                "Step Detector" ->
                    if (isChecked) it.startStepDetection(this, this, StepDetectorUtil.MALE)
                    else it.stopStepDetection(this)

                "Pickup Device Detector" ->
                    if (isChecked) it.startPickupDeviceDetection(this)
                    else it.stopPickupDeviceDetection(this)

                "Scoop Detector" ->
                    if (isChecked) it.startScoopDetection(this)
                    else it.stopScoopDetection(this)
            }
        }
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

    override fun onShakeDetected() { setResultText("Shake Detected!", false) }
    override fun onShakeStopped() { setResultText("Shake Stopped!", false) }
    override fun onFaceUp() { setResultText("Face UP", false) }
    override fun onFaceDown() { setResultText("Face Down", false) }
    override fun onDark() { setResultText("Dark", false) }
    override fun onLight() { setResultText("Not Dark", false) }
    override fun onTopSideUp() { setResultText("Top Side UP", false) }
    override fun onBottomSideUp() { setResultText("Bottom Side UP", false) }
    override fun onLeftSideUp() { setResultText("Left Side UP", false) }
    override fun onRightSideUp() { setResultText("Right Side UP", false) }
    override fun onNear() { setResultText("Near", false) }
    override fun onFar() { setResultText("Far", false) }
    override fun onWave() { setResultText("Wave Detected!", false) }
    override fun onSoundDetected(level: Float) {
        setResultText("${DecimalFormat("##.##").format(level.toDouble())} dB", true)
    }
    override fun onMovement() { setResultText("Movement Detected!", false) }
    override fun onStationary() { setResultText("Device Stationary!", false) }
    override fun onChop() { setResultText("Chop Detected!", false) }
    override fun onWristTwist() { setResultText("Wrist Twist Detected!", false) }
    override fun onRotation(angleInAxisX: Float, angleInAxisY: Float, angleInAxisZ: Float) {
        setResultText(
            "Rotation in Axis Detected(deg):\nX=$angleInAxisX,\nY=$angleInAxisY,\nZ=$angleInAxisZ",
            true,
        )
    }
    override fun onTiltInAxisX(direction: Int) { displayTiltDirection(direction, "X") }
    override fun onTiltInAxisY(direction: Int) { displayTiltDirection(direction, "Y") }
    override fun onTiltInAxisZ(direction: Int) { displayTiltDirection(direction, "Z") }
    override fun stepInformation(noOfSteps: Int, distanceInMeter: Float, stepActivityType: Int) {
        val typeOfActivity = when (stepActivityType) {
            StepDetectorUtil.ACTIVITY_RUNNING -> "Running"
            StepDetectorUtil.ACTIVITY_WALKING -> "Walking"
            else -> "Still"
        }
        setResultText(
            "Steps: $noOfSteps\nDistance: $distanceInMeter m\nActivity Type: $typeOfActivity",
            true,
        )
    }
    override fun onDevicePickedUp() { setResultText("Device Picked up Detected!", false) }
    override fun onDevicePutDown() { setResultText("Device Put down Detected!", false) }
    override fun onScooped() { setResultText("Scoop Gesture Detected!", false) }

    private fun displayTiltDirection(direction: Int, axis: String) {
        val dir = if (direction == TiltDirectionDetector.DIRECTION_CLOCKWISE) "ClockWise" else "AntiClockWise"
        setResultText("Tilt in $axis Axis: $dir", false)
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
