package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

data class SensorItem(
    val label: String,
    val isSelected: Boolean,
    val result: String,
    val onSelect: () -> Unit,
)

@Composable
internal fun SensorList(
    sensors: List<SensorItem>,
    selectedSensor: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        sensors.forEach { sensor ->
            val isTouch =
                sensor.label == "Touch Detection" ||
                    sensor.label == "Pinch Scale Detection" ||
                    sensor.label == "Edge Swipe" ||
                    sensor.label == "Diagonal Swipe"
            if (isTouch) {
                key(selectedSensor) {
                    SenseyRadioButtonWithTouchArea(
                        label = sensor.label,
                        selected = sensor.isSelected,
                        result = sensor.result,
                        helperText = helperTextForLabel(sensor.label),
                        showHitArea = sensor.isSelected,
                        onSelect = sensor.onSelect,
                    )
                }
            } else {
                SenseyRadioButton(
                    label = sensor.label,
                    result = sensor.result,
                    selected = sensor.isSelected,
                    onSelect = sensor.onSelect,
                )
            }
        }
    }
}

private fun helperTextForLabel(label: String): String =
    when (label) {
        "Touch Detection" -> "Single Tap, Double Tap, Long Press, Swipe, Scroll, N-Tap"
        "Pinch Scale Detection" -> "Pinch In / Pinch Out"
        "Edge Swipe" -> "Swipe from composable edge"
        else -> "Swipe diagonally"
    }

@PreviewLightDark
@Composable
private fun SensorListPreview() {
    PreviewTheme {
        SensorList(
            listOf(
                SensorItem("Shake Detector", true, "", {}),
                SensorItem("Flip Detector", false, "", {}),
            ),
        )
    }
}
