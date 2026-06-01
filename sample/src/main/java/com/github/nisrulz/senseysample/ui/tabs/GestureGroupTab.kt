package com.github.nisrulz.senseysample.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.navigation.TouchGroup
import com.github.nisrulz.senseysample.navigation.gestureGroupInfo
import com.github.nisrulz.senseysample.navigation.groupSensors
import com.github.nisrulz.senseysample.ui.core.Paddings
import com.github.nisrulz.senseysample.ui.core.PreviewTheme
import com.github.nisrulz.senseysample.ui.sensors.SensorItem
import com.github.nisrulz.senseysample.ui.sensors.SensorList

@Composable
fun GestureGroupTab(
    group: GestureGroup,
    selectedSensor: String?,
    sensorResults: Map<String, String>,
    onSensorSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val info = gestureGroupInfo[group] ?: return
    val sensorLabels = groupSensors[group] ?: return

    Box(
        modifier = modifier.fillMaxSize().padding(horizontal = Paddings.sm),
    ) {
        SensorList(
            sensors =
                sensorLabels.map { label ->
                    val isTouch = label in TOUCH_LIKE_SENSORS
                    SensorItem(
                        label = label,
                        isSelected = label == selectedSensor,
                        result = sensorResults[label].orEmpty(),
                        onSelect = { onSensorSelect(label) },
                    )
                },
            selectedSensor = selectedSensor,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private val TOUCH_LIKE_SENSORS =
    setOf(
        "Touch Detection",
        "Pinch Scale",
        "Edge Swipe",
        "Diagonal Swipe",
        "Long Press Drag",
        "Two Finger Swipe",
        "Corner Swipe",
    )

@PreviewLightDark
@Composable
private fun GestureGroupTabPreview() {
    PreviewTheme {
        GestureGroupTab(
            group = TouchGroup,
            selectedSensor = null,
            sensorResults = emptyMap(),
            onSensorSelect = {},
        )
    }
}
