@file:Suppress("DEPRECATION")

package com.github.nisrulz.senseysample

import android.Manifest.permission
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.senseysample.ui.MainScreen
import com.github.nisrulz.senseysample.ui.SensorItem
import com.github.nisrulz.senseysample.utils.RPResultListener
import com.github.nisrulz.senseysample.utils.RuntimePermissionUtil

class MainActivity : ComponentActivity() {

    private var hasRecordAudioPermission = false
    private val recordAudioPermission = permission.RECORD_AUDIO
    private val logTag = javaClass.canonicalName

    private val sensorManager = SensorManager(this, logTag)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        hasRecordAudioPermission =
            RuntimePermissionUtil.checkPermissonGranted(this, recordAudioPermission)

        setContent {
            MainScreen(
                sensors = sensorManager.sensors.map { label ->
                    SensorItem(
                        label = label,
                        isSelected = label == sensorManager.selectedSensor,
                        onSelect = { onSensorSelected(label) },
                    )
                },
                resultText = sensorManager.resultText,
                onTouchDetectorClick = {
                    startActivity(Intent(this@MainActivity, TouchActivity::class.java))
                },
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.stopSelectedDetector()
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

    private fun onSensorSelected(sensor: String) {
        sensorManager.onSensorSelected(sensor, hasRecordAudioPermission) {
            RuntimePermissionUtil.requestPermission(this, recordAudioPermission, 100)
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
                            sensorManager.startAfterPermissionGranted()
                        }
                    }
                },
            )
        }
    }
}
