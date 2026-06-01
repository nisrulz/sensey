package com.github.nisrulz.senseysample.ui.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.ui.core.AppHeader

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
internal fun GestureNavRailLayout(
    selectedGroup: GestureGroup,
    onGroupSelected: (GestureGroup) -> Unit,
    selectedSensor: String?,
    sensorResults: Map<String, String>,
    onSensorSelected: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    content: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Row(Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier.shadow(8.dp),
            header = { GestureNavRailHeader() },
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            GestureNavRailItems(
                selectedGroup = selectedGroup,
                onGroupSelected = onGroupSelected,
            )
        }
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = { AppHeader(scrollBehavior = scrollBehavior) },
        ) { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                content()
            }
        }
    }
}
