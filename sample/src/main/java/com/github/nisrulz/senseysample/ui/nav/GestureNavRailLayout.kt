package com.github.nisrulz.senseysample.ui.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.ui.core.AppHeader
import com.github.nisrulz.senseysample.ui.sensors.sensorEntryProvider

@Composable
internal fun GestureNavRailLayout(
    selectedGroup: GestureGroup,
    onGroupSelected: (GestureGroup) -> Unit,
    backStack: List<GestureGroup>,
    selectedSensor: String?,
    sensorResults: Map<String, String>,
    onSensorSelected: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Row(Modifier.fillMaxSize()) {
        NavigationRail(header = { GestureNavRailHeader() }) {
            GestureNavRailItems(
                selectedGroup = selectedGroup,
                onGroupSelected = onGroupSelected,
            )
        }
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = { AppHeader() },
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
}
