package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class SensorItem(
    val label: String,
    val isSelected: Boolean,
    val result: String,
    val onSelect: () -> Unit,
)

@Composable
internal fun SensorList(
    sensors: List<SensorItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        sensors.forEach { sensor ->
            SenseyRadioButton(
                label = sensor.label,
                result = sensor.result,
                selected = sensor.isSelected,
                onSelect = sensor.onSelect,
            )
        }
    }
}

@Preview
@Composable
private fun SensorListPreview() {
    SensorList(
        listOf(
            SensorItem("Shake Detector", true, "", {}),
            SensorItem("Flip Detector", false, "", {}),
        ),
    )
}
