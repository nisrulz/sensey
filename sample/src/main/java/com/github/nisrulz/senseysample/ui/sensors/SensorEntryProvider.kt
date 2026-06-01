package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.ui.tabs.GestureGroupTab

@Composable
fun sensorEntryProvider(
    selectedSensor: String?,
    sensorResults: Map<String, String>,
    onSensorSelected: (String) -> Unit,
): (GestureGroup) -> NavEntry<GestureGroup> =
    { key ->
        NavEntry(key) {
            GestureGroupTab(
                group = key,
                selectedSensor = selectedSensor,
                sensorResults = sensorResults,
                onSensorSelect = onSensorSelected,
            )
        }
    }
