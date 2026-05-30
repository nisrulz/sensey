package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    selectedSensor: String?,
    sensors: List<SensorItem>,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
        ) {
            AppHeader()
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
            ) {
                SensorList(
                    sensors = sensors,
                    selectedSensor = selectedSensor,
                )
            }
        }
    }
}
