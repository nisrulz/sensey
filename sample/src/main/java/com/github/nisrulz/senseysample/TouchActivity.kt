@file:Suppress("DEPRECATION")

package com.github.nisrulz.senseysample

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.senseysample.ui.TouchScreen

class TouchActivity : ComponentActivity() {

    private val logTag = javaClass.canonicalName
    private val sensorManager = TouchSensorManager(this, logTag)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TouchScreen(
                touchDetectionChecked = sensorManager.selectedSensor == TouchSensorManager.TOUCH,
                pinchScaleChecked = sensorManager.selectedSensor == TouchSensorManager.PINCH,
                onTouchDetectionToggle = { sensorManager.select(TouchSensorManager.TOUCH) },
                onPinchScaleToggle = { sensorManager.select(TouchSensorManager.PINCH) },
                resultText = sensorManager.resultText,
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.stopAll()
        Sensey.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.cancel()
    }

    override fun onResume() {
        super.onResume()
        Sensey.init(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Sensey.setupDispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }
}
