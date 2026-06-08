package com.github.nisrulz.senseysample

import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.ui.core.SenseyTheme
import com.github.nisrulz.senseysample.ui.nav.MainScreen
import com.github.nisrulz.senseysample.utils.SensorLogger
import com.github.nisrulz.senseysample.utils.isAudioPermissionGranted
import com.github.nisrulz.senseysample.utils.registerAudioPermission
import com.github.nisrulz.senseysample.utils.requestAudioIfNeeded
import com.github.nisrulz.senseysample.viewmodel.SampleViewModel

class MainActivity : ComponentActivity() {
    private val snackbarHostState = SnackbarHostState()
    private lateinit var viewModel: SampleViewModel

    private val sensorManager by lazy {
        SenseySensorManager(
            activity = this,
            logTag = javaClass.name,
            onSensorUnavailable = { label ->
                viewModel.showSnackbar("$label requires a sensor not available on this device")
            },
            onSensorResult = { sensor, result ->
                viewModel.updateSensorResult(sensor, result)
            },
        )
    }

    private val sensorLogger by lazy {
        SensorLogger(getSystemService(SensorManager::class.java)!!)
    }

    private val audioPermissionLauncher by lazy {
        registerAudioPermission(
            onGranted = { sensorManager.startAfterPermissionGranted() },
            onDenied = { sensorManager.clearPendingPermission() },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorLogger.start()

        sensorManager.sensey =
            senseyRegister(
                samplingPeriod = Sensey.SAMPLING_PERIOD_GAME,
                sensorDataLoggingEnabled = true,
            ) { }

        setContent {
            viewModel = viewModel()

            LaunchedEffect(Unit) {
                viewModel.snackbarEvent.collect { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }

            SenseyTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                MainScreen(
                    selectedGroup = state.selectedGroup,
                    selectedSensor = state.selectedSensor,
                    sensorResults = state.sensorResults,
                    snackbarHostState = snackbarHostState,
                    onGroupSelected = { onGroupSelected(it) },
                    onSensorSelected = { onSensorSelected(it) },
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.stopSelectedDetector()
        sensorLogger.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.cancel()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.startAfterPermissionGranted()
        sensorLogger.start()
    }

    private fun onGroupSelected(group: GestureGroup) {
        viewModel.selectGroup(group)
    }

    private fun onSensorSelected(sensor: String) {
        val isSameSensor = sensorManager.selectedSensor == sensor
        sensorManager.onSensorSelected(
            sensor = sensor,
            hasRecordAudioPermission = isAudioPermissionGranted(),
            onPermissionNeeded = { audioPermissionLauncher.requestAudioIfNeeded(this) },
        )
        viewModel.selectSensor(if (isSameSensor) null else sensor)
    }
}
