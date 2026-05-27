package com.github.nisrulz.senseysample

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleEvent
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeEvent
import com.github.nisrulz.senseysample.utils.HapticUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class TouchSensorManager(
    private val context: Context,
) {
    var resultText by mutableStateOf("[ Hit Area ]")
    var touchDetectionEnabled by mutableStateOf(false)
    var pinchScaleEnabled by mutableStateOf(false)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var resetJob: Job? = null

    fun toggleTouch() {
        touchDetectionEnabled = !touchDetectionEnabled
    }

    fun togglePinch() {
        pinchScaleEnabled = !pinchScaleEnabled
    }

    fun onTouchEvent(event: TouchTypeEvent) {
        HapticUtil.quickTap(context)
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
        updateResultText(text)
    }

    fun onPinchEvent(event: PinchScaleEvent) {
        HapticUtil.quickTap(context)
        updateResultText(if (event.isScalingOut) "Scaling Out" else "Scaling In")
    }

    fun cancel() {
        scope.cancel()
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

    private fun updateResultText(text: String) {
        resultText = text
        resetJob?.cancel()
        resetJob =
            scope.launch {
                delay(3000)
                resultText = "[ Hit Area ]"
            }
        if (BuildConfig.DEBUG) Log.d(javaClass.canonicalName, text)
    }
}
