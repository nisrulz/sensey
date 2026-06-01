package com.github.nisrulz.senseysample.ui.nav

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.nisrulz.senseysample.navigation.GestureGroup

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
    val backStack =
        remember(selectedGroup) {
            mutableStateListOf(selectedGroup)
        }

    LaunchedEffect(selectedGroup) {
        if (backStack.isNotEmpty()) backStack[0] = selectedGroup
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        if (maxWidth > COMPACT_MAX_WIDTH) {
            GestureNavRailLayout(
                selectedGroup = selectedGroup,
                onGroupSelected = onGroupSelected,
                backStack = backStack,
                selectedSensor = selectedSensor,
                sensorResults = sensorResults,
                onSensorSelected = onSensorSelected,
                snackbarHostState = snackbarHostState,
            )
        } else {
            GestureCompactLayout(
                selectedGroup = selectedGroup,
                onGroupSelected = onGroupSelected,
                backStack = backStack,
                selectedSensor = selectedSensor,
                sensorResults = sensorResults,
                onSensorSelected = onSensorSelected,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}
