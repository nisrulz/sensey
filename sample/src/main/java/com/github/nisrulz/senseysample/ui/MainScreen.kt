package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
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
        color = PrimaryBlue,
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
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    sensors.forEach { sensor ->
                        val isTouch = sensor.label == "Touch Detection" || sensor.label == "Pinch Scale Detection" || sensor.label == "Edge Swipe"
                        if (isTouch) {
                            key(selectedSensor) {
                                val helper =
                                    when (sensor.label) {
                                        "Touch Detection" -> "Single Tap, Double Tap, Long Press, Swipe, Scroll, N-Tap"
                                        "Pinch Scale Detection" -> "Pinch In / Pinch Out"
                                        else -> "Swipe from screen edge"
                                    }
                                SenseyRadioButtonWithTouchArea(
                                    label = sensor.label,
                                    selected = sensor.isSelected,
                                    result = sensor.result,
                                    helperText = helper,
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
        }
    }
}
