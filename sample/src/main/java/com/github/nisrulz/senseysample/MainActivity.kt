package com.github.nisrulz.senseysample

import android.Manifest.permission
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.senseyStop
import com.github.nisrulz.senseysample.ui.MainScreen
import com.github.nisrulz.senseysample.ui.SensorItem
import com.github.nisrulz.senseysample.utils.RuntimePermissionUtil

class MainActivity : ComponentActivity() {
    private var hasRecordAudioPermission = false
    private val recordAudioPermission = permission.RECORD_AUDIO
    private val logTag = javaClass.name

    private val sensorManager = SenseySensorManager(this, logTag)

    private val audioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                hasRecordAudioPermission = true
                sensorManager.startAfterPermissionGranted()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        hasRecordAudioPermission =
            RuntimePermissionUtil.checkPermissonGranted(this, recordAudioPermission)

        setContent {
            MainScreen(
                sensors =
                    sensorManager.sensors.map { label ->
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
        senseyStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.cancel()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.sensey =
            senseyRegister(sensorDataLoggingEnabled = true) {
                // plugin registration happens in SenseySensorManager
            }
    }

    private fun onSensorSelected(sensor: String) {
        sensorManager.onSensorSelected(sensor, hasRecordAudioPermission) {
            audioPermissionLauncher.launch(recordAudioPermission)
        }
    }
}
