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
import androidx.compose.ui.unit.dp

@Composable
fun TouchScreen(
    touchDetectionChecked: Boolean,
    pinchScaleChecked: Boolean,
    onTouchDetectionToggle: (Boolean) -> Unit,
    onPinchScaleToggle: (Boolean) -> Unit,
    resultText: String,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        color = PrimaryBlue,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                SenseyRadioButton(
                    label = "Touch Detection",
                    selected = touchDetectionChecked,
                    onSelect = { onTouchDetectionToggle(true) },
                )
                SenseyRadioButton(
                    label = "Pinch Scale Detection",
                    selected = pinchScaleChecked,
                    onSelect = { onPinchScaleToggle(true) },
                )
            }

            ResultArea(
                text = resultText,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            )
        }
    }
}
