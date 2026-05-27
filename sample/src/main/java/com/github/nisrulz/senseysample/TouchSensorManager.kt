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
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleEvent
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeEvent

import com.github.nisrulz.senseysample.utils.HapticUtil

internal class TouchSensorManager(
    private val activity: Activity,
    private val logTag: String,
) {

    companion object {
        const val TOUCH = "touch"
        const val PINCH = "pinch"
    }

    var resultText by mutableStateOf("[ Hit Area ]")
    var selectedSensor by mutableStateOf<String?>(null)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var resetJob: Job? = null

    private fun <T> withHaptic(dispatcher: (T) -> Unit): (T) -> Unit = { event ->
        HapticUtil.quickTap(activity)
        dispatcher(event)
    }

    private val pinchDispatcher: (PinchScaleEvent) -> Unit = withHaptic { event: PinchScaleEvent ->
        updateResultText(if (event.isScalingOut) "Scaling Out" else "Scaling In")
    }

    private val touchDispatcher: (TouchTypeEvent) -> Unit = withHaptic { event: TouchTypeEvent ->
        when (event) {
            is TouchTypeEvent.NTap -> updateResultText("${event.count}-Tap")
            TouchTypeEvent.DoubleTap -> updateResultText("Double Tap")
            TouchTypeEvent.LongPress -> updateResultText("Long press")
            TouchTypeEvent.SingleTap -> updateResultText("Single Tap")
            is TouchTypeEvent.Swipe -> swipeDirText(event.direction).let { updateResultText(it) }
            is TouchTypeEvent.Scroll -> scrollDirText(event.direction).let { updateResultText(it) }
            TouchTypeEvent.ThreeFingerSingleTap -> updateResultText("Three Finger Tap")
            TouchTypeEvent.TwoFingerSingleTap -> updateResultText("Two Finger Tap")
        }
    }

    private fun swipeDirText(dir: TouchTypeEvent.Direction): String = when (dir) {
        TouchTypeEvent.Direction.UP -> "Swipe Up"
        TouchTypeEvent.Direction.DOWN -> "Swipe Down"
        TouchTypeEvent.Direction.LEFT -> "Swipe Left"
        TouchTypeEvent.Direction.RIGHT -> "Swipe Right"
        TouchTypeEvent.Direction.UP_RIGHT -> "Swipe Up-Right"
        TouchTypeEvent.Direction.UP_LEFT -> "Swipe Up-Left"
        TouchTypeEvent.Direction.DOWN_RIGHT -> "Swipe Down-Right"
        TouchTypeEvent.Direction.DOWN_LEFT -> "Swipe Down-Left"
    }

    private fun scrollDirText(dir: TouchTypeEvent.Direction): String = when (dir) {
        TouchTypeEvent.Direction.UP -> "Scrolling Up"
        TouchTypeEvent.Direction.DOWN -> "Scrolling Down"
        TouchTypeEvent.Direction.LEFT -> "Scrolling Left"
        TouchTypeEvent.Direction.RIGHT -> "Scrolling Right"
        TouchTypeEvent.Direction.UP_RIGHT -> "Scrolling Up-Right"
        TouchTypeEvent.Direction.UP_LEFT -> "Scrolling Up-Left"
        TouchTypeEvent.Direction.DOWN_RIGHT -> "Scrolling Down-Right"
        TouchTypeEvent.Direction.DOWN_LEFT -> "Scrolling Down-Left"
    }

    fun select(sensor: String) {
        val prev = selectedSensor
        if (prev == sensor) {
            stopDetector(sensor)
            selectedSensor = null
            return
        }
        if (prev != null) stopDetector(prev)
        startDetector(sensor)
        selectedSensor = sensor
    }

    fun stopAll() {
        selectedSensor?.let { stopDetector(it) }
        selectedSensor = null
    }

    private fun startDetector(sensor: String) {
        when (sensor) {
            TOUCH -> Sensey.startTouchTypeDetection(activity, touchDispatcher)
            PINCH -> Sensey.startPinchScaleDetection(activity, pinchDispatcher)
        }
    }

    private fun stopDetector(sensor: String) {
        when (sensor) {
            TOUCH -> Sensey.stopTouchTypeDetection()
            PINCH -> Sensey.stopPinchScaleDetection()
        }
    }

    fun cancel() {
        scope.cancel()
    }

    private fun updateResultText(text: String) {
        resultText = text
        resetJob?.cancel()
        resetJob = scope.launch {
            delay(3000)
            resultText = "[ Hit Area ]"
        }
        if (BuildConfig.DEBUG) Log.d(logTag, text)
    }
}
