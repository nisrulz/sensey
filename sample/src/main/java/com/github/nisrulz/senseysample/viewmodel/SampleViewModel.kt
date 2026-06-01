package com.github.nisrulz.senseysample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nisrulz.senseysample.navigation.GestureGroup
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SampleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SampleUiState())
    val uiState: StateFlow<SampleUiState> = _uiState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    fun selectGroup(group: GestureGroup) {
        _uiState.update { it.copy(selectedGroup = group) }
    }

    fun selectSensor(sensor: String?) {
        _uiState.update { it.copy(selectedSensor = sensor) }
    }

    fun updateSensorResult(
        sensor: String,
        result: String,
    ) {
        _uiState.update {
            it.copy(sensorResults = it.sensorResults + (sensor to result))
        }
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarEvent.emit(message)
        }
    }
}
