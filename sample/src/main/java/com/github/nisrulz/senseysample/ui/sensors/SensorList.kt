package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.nisrulz.senseysample.ui.core.PreviewTheme

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
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            sensors.forEach { sensor ->
                val isTouch =
                    sensor.label == "Touch Detection" ||
                        sensor.label == "Pinch Scale" ||
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
        ScrollbarThumb(
            scrollState = scrollState,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

private fun helperTextForLabel(label: String): String =
    when (label) {
        "Shake Gesture" -> "Shake the device"
        "Flip Gesture" -> "Flip device face up or face down"
        "Orientation Gesture" -> "Rotate device to different orientation"
        "Proximity Gesture" -> "Cover / uncover the proximity sensor"
        "Light Detection" -> "Change ambient light level"
        "Wave Detection" -> "Wave hand over the proximity sensor"
        "Sound Level Detection" -> "Speak or make a sound"
        "Movement Detection" -> "Move the device"
        "Chop Detector" -> "Perform a chopping motion"
        "Wrist Twist Detection" -> "Twist your wrist"
        "Rotation Angle Detection" -> "Rotate device on any axis"
        "Tilt Direction Detection" -> "Tilt the device in any direction"
        "Step Detector" -> "Walk or run while holding the device"
        "Pickup Device Detector" -> "Pick up or put down the device"
        "Scoop Detector" -> "Perform a scooping motion"
        "Tap On Back" -> "Tap on the back of the device"
        "Turn Over" -> "Turn the device over"
        "Device Spin" -> "Spin the device rapidly on any axis"
        "Raise To Ear" -> "Raise the device to your ear"
        "Clap Detection" -> "Clap your hands near the device"
        "Nod Gesture" -> "Nod your head (yes motion)"
        "Head Shake" -> "Shake your head (no motion)"
        "Touch Detection" -> "Single Tap, Double Tap, Long Press, Swipe, Scroll, N-Tap"
        "Pinch Scale" -> "Pinch In / Pinch Out"
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
