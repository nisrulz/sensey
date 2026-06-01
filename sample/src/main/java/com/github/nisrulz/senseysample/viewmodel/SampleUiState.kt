package com.github.nisrulz.senseysample.viewmodel

import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.navigation.TouchGroup

data class SampleUiState(
    val selectedGroup: GestureGroup = TouchGroup,
    val selectedSensor: String? = null,
    val sensorResults: Map<String, String> = emptyMap(),
)
