@file:Suppress("DEPRECATION")

package com.github.nisrulz.senseysample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.nisrulz.sensey.gesture.compose.SenseyGestureEffect
import com.github.nisrulz.sensey.gesture.pinchScalePlugin
import com.github.nisrulz.sensey.gesture.touchTypePlugin
import com.github.nisrulz.senseysample.ui.TouchScreen

class TouchActivity : ComponentActivity() {
    private val sensorManager = TouchSensorManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SenseyGestureEffect(lifecycle) {
                touchTypePlugin(this@TouchActivity) { event ->
                    sensorManager.onTouchEvent(event)
                }
                pinchScalePlugin(this@TouchActivity) { event ->
                    sensorManager.onPinchEvent(event)
                }
            }

            TouchScreen(
                touchDetectionChecked = sensorManager.touchDetectionEnabled,
                pinchScaleChecked = sensorManager.pinchScaleEnabled,
                onTouchDetectionToggle = { sensorManager.toggleTouch() },
                onPinchScaleToggle = { sensorManager.togglePinch() },
                resultText = sensorManager.resultText,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.cancel()
    }
}
