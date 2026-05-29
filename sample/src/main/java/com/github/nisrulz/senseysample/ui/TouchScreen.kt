package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

@Preview
@Composable
private fun TouchScreenPreview() {
    TouchScreen(
        touchDetectionChecked = true,
        pinchScaleChecked = false,
        onTouchDetectionToggle = {},
        onPinchScaleToggle = {},
        resultText = "Touch gesture detected",
    )
}

@Composable
fun TouchScreen(
    touchDetectionChecked: Boolean,
    pinchScaleChecked: Boolean,
    onTouchDetectionToggle: () -> Unit,
    onPinchScaleToggle: () -> Unit,
    resultText: String,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        color = PrimaryBlue,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                SenseyRadioButton(
                    label = "Touch Detection",
                    selected = touchDetectionChecked,
                    onSelect = { onTouchDetectionToggle() },
                )
                SenseyRadioButton(
                    label = "Pinch Scale Detection",
                    selected = pinchScaleChecked,
                    onSelect = { onPinchScaleToggle() },
                )
            }

            ResultArea(
                text = resultText,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .senseyGestures(),
            )
        }
    }
}
