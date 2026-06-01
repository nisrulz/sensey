package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import com.github.nisrulz.senseysample.navigation.GestureGroup

@Composable
internal fun GestureCompactLayout(
    selectedGroup: GestureGroup,
    onGroupSelected: (GestureGroup) -> Unit,
    backStack: List<GestureGroup>,
    selectedSensor: String?,
    sensorResults: Map<String, String>,
    onSensorSelected: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { AppHeader() },
        bottomBar = {
            GestureNavBar(
                selectedGroup = selectedGroup,
                onGroupSelected = onGroupSelected,
            )
        },
    ) { paddingValues ->
        Box(Modifier.fillMaxSize().padding(paddingValues)) {
            NavDisplay(
                backStack = backStack,
                onBack = {},
                entryProvider =
                    sensorEntryProvider(
                        selectedSensor = selectedSensor,
                        sensorResults = sensorResults,
                        onSensorSelected = onSensorSelected,
                    ),
            )
        }
    }
}
