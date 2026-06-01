package com.github.nisrulz.senseysample.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.nisrulz.senseysample.navigation.GestureGroup
import com.github.nisrulz.senseysample.navigation.gestureGroupInfo
import com.github.nisrulz.senseysample.navigation.groupSensors
import com.github.nisrulz.senseysample.ui.Paddings
import com.github.nisrulz.senseysample.ui.SensorItem
import com.github.nisrulz.senseysample.ui.SensorList

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
        modifier = modifier.fillMaxSize().padding(horizontal = Paddings.lg),
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
        "Pinch Scale Detection",
        "Edge Swipe",
        "Diagonal Swipe",
        "Long Press Drag",
        "Two Finger Swipe",
        "Corner Swipe",
    )
