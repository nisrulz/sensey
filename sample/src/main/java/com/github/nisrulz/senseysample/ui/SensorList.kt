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
                    sensor.label == "Diagonal Swipe" ||
                    sensor.label == "Long Press Drag" ||
                    sensor.label == "Two Finger Swipe" ||
                    sensor.label == "Corner Swipe"
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
                    helperText = helperTextForLabel(sensor.label),
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
        "Shake Gesture" -> "Shake / Shake Stopped"
        "Flip Gesture" -> "Face Up / Face Down"
        "Orientation Gesture" -> "Top, Bottom, Left, Right Side Up"
        "Proximity Gesture" -> "Near / Far"
        "Light Detection" -> "Dark / Light"
        "Wave Detection" -> "Wave hand over proximity sensor"
        "Sound Level Detection" -> "Microphone dB level"
        "Movement Detection" -> "Moved / Stationary"
        "Chop Detector" -> "Chopping motion"
        "Wrist Twist Detection" -> "Twist wrist"
        "Rotation Angle Detection" -> "X, Y, Z rotation in degrees"
        "Tilt Direction Detection" -> "Clockwise / Anti-clockwise per axis"
        "Step Detector" -> "Steps, distance, activity type"
        "Pickup Device Detector" -> "Picked Up / Put Down"
        "Scoop Detector" -> "Scooping gesture"
        "Tap On Back" -> "Tap on back of device"
        "Turn Over" -> "Turn device face down"
        "Device Spin" -> "Rapid spin on any axis"
        "Raise To Ear" -> "Raise device to ear"
        "Clap Detection" -> "Clap hands (requires microphone)"
        "Nod Gesture" -> "Nod head (yes)"
        "Head Shake" -> "Shake head (no)"
        "Touch Detection" -> "Single Tap, Double Tap, Long Press, Swipe, Scroll, N-Tap"
        "Pinch Scale Detection" -> "Pinch In / Pinch Out"
        "Edge Swipe" -> "Swipe from composable edge"
        "Diagonal Swipe" -> "Swipe diagonally"
        "Long Press Drag" -> "Long press then drag in any direction"
        "Two Finger Swipe" -> "Two-finger directional swipe"
        "Corner Swipe" -> "Swipe from screen corner inward"
        else -> ""
    }

@PreviewLightDark
@Composable
private fun SensorListPreview() {
    PreviewTheme {
        SensorList(
            listOf(
                SensorItem("Shake Gesture", true, "", {}),
                SensorItem("Flip Gesture", false, "", {}),
                SensorItem("Edge Swipe", false, "", {}),
                SensorItem("Long Press Drag", false, "", {}),
            ),
        )
    }
}
