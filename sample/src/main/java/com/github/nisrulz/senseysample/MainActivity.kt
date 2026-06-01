package com.github.nisrulz.senseysample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.senseysample.ui.MainScreen
import com.github.nisrulz.senseysample.ui.SenseyTheme
import com.github.nisrulz.senseysample.ui.SensorItem
import com.github.nisrulz.senseysample.utils.isAudioPermissionGranted
import com.github.nisrulz.senseysample.utils.registerAudioPermission
import com.github.nisrulz.senseysample.utils.requestAudioIfNeeded
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val snackbarHostState = SnackbarHostState()
    private val sensorManager =
        SenseySensorManager(this, javaClass.name) { label ->
            CoroutineScope(Dispatchers.Main).launch {
                snackbarHostState.showSnackbar("$label requires a sensor not available on this device")
            }
        }

    private val audioPermissionLauncher =
        registerAudioPermission(
            onGranted = { sensorManager.startAfterPermissionGranted() },
            onDenied = { sensorManager.clearPendingPermission() },
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager.sensey =
            senseyRegister(samplingPeriod = Sensey.SAMPLING_PERIOD_GAME, sensorDataLoggingEnabled = true) { }

        setContent {
            SenseyTheme {
                MainScreen(
                    selectedSensor = sensorManager.selectedSensor,
                    snackbarHostState = snackbarHostState,
                    sensors =
                        sensorManager.sensors.map { label ->
                            SensorItem(
                                label = label,
                                isSelected = label == sensorManager.selectedSensor,
                                result = sensorManager.getResult(label),
                                onSelect = { onSensorSelected(label) },
                            )
                        },
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.stopSelectedDetector()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.cancel()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.startAfterPermissionGranted()
    }

    private fun onSensorSelected(sensor: String) {
        val hasPermission = isAudioPermissionGranted()
        sensorManager.onSensorSelected(sensor, hasPermission) {
            audioPermissionLauncher.requestAudioIfNeeded(this)
        }
    }
}
