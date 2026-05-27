@file:Suppress("DEPRECATION")

package com.github.nisrulz.senseysample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.nisrulz.sensey.PinchScaleDetector
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.TouchTypeDetector
import com.github.nisrulz.senseysample.ui.TouchScreen

class TouchActivity : ComponentActivity() {

    private val LOGTAG = javaClass.canonicalName
    private val handler = Handler(Looper.getMainLooper())

    private var resultText by mutableStateOf("[ Hit Area ]")
    private var touchDetectionChecked by mutableStateOf(false)
    private var pinchScaleChecked by mutableStateOf(false)

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
        Sensey.getInstance()
            .startPinchScaleDetection(this, object : PinchScaleDetector.PinchScaleListener {
                override fun onScale(gestureDetector: ScaleGestureDetector, isScalingOut: Boolean) {
                    updateResultText(if (isScalingOut) "Scaling Out" else "Scaling In")
                }
                override fun onScaleEnd(gestureDetector: ScaleGestureDetector) {
                    updateResultText("Scaling : Stopped")
                }
                override fun onScaleStart(gestureDetector: ScaleGestureDetector) {
                    updateResultText("Scaling : Started")
                }
            })
    }

    private fun startTouchTypeDetection() {
        Sensey.getInstance()
            .startTouchTypeDetection(this, object : TouchTypeDetector.TouchTypListener {
                override fun onDoubleTap() { updateResultText("Double Tap") }
                override fun onLongPress() { updateResultText("Long press") }
                override fun onScroll(scrollDirection: Int) {
                    updateResultText(
                        when (scrollDirection) {
                            TouchTypeDetector.SCROLL_DIR_UP -> "Scrolling Up"
                            TouchTypeDetector.SCROLL_DIR_DOWN -> "Scrolling Down"
                            TouchTypeDetector.SCROLL_DIR_LEFT -> "Scrolling Left"
                            TouchTypeDetector.SCROLL_DIR_RIGHT -> "Scrolling Right"
                            else -> return
                        }
                    )
                }
                override fun onSingleTap() { updateResultText("Single Tap") }
                override fun onSwipe(swipeDirection: Int) {
                    updateResultText(
                        when (swipeDirection) {
                            TouchTypeDetector.SWIPE_DIR_UP -> "Swipe Up"
                            TouchTypeDetector.SWIPE_DIR_DOWN -> "Swipe Down"
                            TouchTypeDetector.SWIPE_DIR_LEFT -> "Swipe Left"
                            TouchTypeDetector.SWIPE_DIR_RIGHT -> "Swipe Right"
                            else -> return
                        }
                    )
                }
                override fun onThreeFingerSingleTap() { updateResultText("Three Finger Tap") }
                override fun onTwoFingerSingleTap() { updateResultText("Two Finger Tap") }
            })
    }
}
