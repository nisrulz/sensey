@file:Suppress("DEPRECATION")

package com.github.nisrulz.senseysample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleEvent
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeEvent
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeTrigger
import com.github.nisrulz.senseysample.ui.TouchScreen

class TouchActivity : ComponentActivity() {

    private val LOGTAG = javaClass.canonicalName
    private val handler = Handler(Looper.getMainLooper())

    private var resultText by mutableStateOf("[ Hit Area ]")
    private var touchDetectionChecked by mutableStateOf(false)
    private var pinchScaleChecked by mutableStateOf(false)

    private val pinchDispatcher: (PinchScaleEvent) -> Unit = { event ->
        updateResultText(if (event.isScalingOut) "Scaling Out" else "Scaling In")
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

    private val touchDispatcher: (TouchTypeEvent) -> Unit = { event ->
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TouchScreen(
                touchDetectionChecked = touchDetectionChecked,
                pinchScaleChecked = pinchScaleChecked,
                onTouchDetectionToggle = { checked ->
                    touchDetectionChecked = checked
                    if (checked) startTouchTypeDetection() else Sensey.getInstance().stopTouchTypeDetection()
                },
                onPinchScaleToggle = { checked ->
                    pinchScaleChecked = checked
                    if (checked) startPinchDetection() else Sensey.getInstance().stopPinchScaleDetection()
                },
                resultText = resultText,
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
        Sensey.getInstance().apply {
            stopTouchTypeDetection()
            stopPinchScaleDetection()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Sensey.getInstance().setupDispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    private fun updateResultText(text: String) {
        resultText = text
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ resultText = "[ Hit Area ]" }, 3000)
        if (BuildConfig.DEBUG) Log.d(LOGTAG, text)
    }

    private fun startPinchDetection() {
        Sensey.getInstance().startPinchScaleDetection(this, pinchDispatcher)
    }

    private fun startTouchTypeDetection() {
        Sensey.getInstance().startTouchTypeDetection(this, touchDispatcher)
    }
}
