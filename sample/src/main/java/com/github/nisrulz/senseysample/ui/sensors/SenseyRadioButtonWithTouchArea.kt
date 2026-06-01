package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
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
    Column(modifier = Modifier.padding(Paddings.md)) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onSelect() }.padding(Paddings.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f).padding(vertical = Paddings.md),
            )
            RadioButton(
                selected = selected,
                onClick = null,
                colors =
                    RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    ),
            )
        }

        Text(
            text = helperText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier =
                Modifier.padding(
                    start = Paddings.md,
                    top = Paddings.none,
                    end = Paddings.md,
                    bottom = Paddings.sm,
                ),
        )

        if (showHitArea || result.isNotBlank()) {
            ResultArea(
                text = result.ifBlank { "[ Hit Area ]" },
                modifier = Modifier.senseyGestures().padding(Paddings.md),
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
