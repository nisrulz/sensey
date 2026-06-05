package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.nisrulz.sensey.gesture.compose.senseyGestures
import com.github.nisrulz.senseysample.ui.core.Paddings
import com.github.nisrulz.senseysample.ui.core.PreviewTheme

@Composable
internal fun SenseyRadioButtonWithTouchArea(
    label: String,
    result: String = "",
    helperText: String,
    selected: Boolean,
    showHitArea: Boolean = false,
    onSelect: () -> Unit,
) {
    RadioButtonRow(
        label = label,
        helperText = helperText,
        selected = selected,
        onSelect = onSelect,
    ) {
        if (showHitArea || result.isNotBlank()) {
            ResultArea(
                text = result.ifBlank { "[ Hit Area ]" },
                modifier = Modifier.senseyGestures().padding(horizontal = Paddings.sm, vertical = Paddings.md),
                showTouchIndicator = showHitArea,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SenseyRadioButtonPreview() {
    PreviewTheme {
        SenseyRadioButtonWithTouchArea(
            label = "Shake Detector",
            result = "",
            helperText = "Some text",
            selected = true,
            onSelect = {},
        )
    }
}
