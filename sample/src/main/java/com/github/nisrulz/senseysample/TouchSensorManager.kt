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
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeTrigger
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
            TouchTypeEvent.DoubleTap -> updateResultText("Double Tap")
            TouchTypeEvent.LongPress -> updateResultText("Long press")
            TouchTypeEvent.SingleTap -> updateResultText("Single Tap")
            is TouchTypeEvent.Swipe -> swipeDirText(event.direction)?.let { updateResultText(it) }
            is TouchTypeEvent.Scroll -> scrollDirText(event.direction)?.let { updateResultText(it) }
            TouchTypeEvent.ThreeFingerSingleTap -> updateResultText("Three Finger Tap")
            TouchTypeEvent.TwoFingerSingleTap -> updateResultText("Two Finger Tap")
        }
    }

    private fun swipeDirText(dir: Int): String? = when (dir) {
        TouchTypeTrigger.SWIPE_DIR_UP -> "Swipe Up"
        TouchTypeTrigger.SWIPE_DIR_DOWN -> "Swipe Down"
        TouchTypeTrigger.SWIPE_DIR_LEFT -> "Swipe Left"
        TouchTypeTrigger.SWIPE_DIR_RIGHT -> "Swipe Right"
        else -> null
    }

    private fun scrollDirText(dir: Int): String? = when (dir) {
        TouchTypeTrigger.SCROLL_DIR_UP -> "Scrolling Up"
        TouchTypeTrigger.SCROLL_DIR_DOWN -> "Scrolling Down"
        TouchTypeTrigger.SCROLL_DIR_LEFT -> "Scrolling Left"
        TouchTypeTrigger.SCROLL_DIR_RIGHT -> "Scrolling Right"
        else -> null
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
