package com.github.nisrulz.senseysample.ui.nav

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.ui.tabs.GestureGroupTab

private val COMPACT_MAX_WIDTH = 600.dp

@Composable
fun MainScreen(
    selectedGroup: GestureGroup,
    selectedSensor: String?,
    sensorResults: Map<String, String>,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onGroupSelected: (GestureGroup) -> Unit,
    onSensorSelected: (String) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        if (maxWidth > COMPACT_MAX_WIDTH) {
            GestureNavRailLayout(
                selectedGroup = selectedGroup,
                onGroupSelected = onGroupSelected,
                selectedSensor = selectedSensor,
                sensorResults = sensorResults,
                onSensorSelected = onSensorSelected,
                snackbarHostState = snackbarHostState,
            ) {
                key(selectedGroup) {
                    GestureGroupTab(
                        group = selectedGroup,
                        selectedSensor = selectedSensor,
                        sensorResults = sensorResults,
                        onSensorSelect = onSensorSelected,
                    )
                }
            }
        } else {
            GestureCompactLayout(
                selectedGroup = selectedGroup,
                onGroupSelected = onGroupSelected,
                selectedSensor = selectedSensor,
                sensorResults = sensorResults,
                onSensorSelected = onSensorSelected,
                snackbarHostState = snackbarHostState,
            ) {
                key(selectedGroup) {
                    GestureGroupTab(
                        group = selectedGroup,
                        selectedSensor = selectedSensor,
                        sensorResults = sensorResults,
                        onSensorSelect = onSensorSelected,
                    )
                }
            }
        }
    }
}
