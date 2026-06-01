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
import com.github.nisrulz.senseysample.ui.core.Paddings
import com.github.nisrulz.senseysample.ui.core.PreviewTheme

@Composable
internal fun SenseyRadioButton(
    label: String,
    helperText: String,
    result: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(Paddings.md),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onSelect() }
                    .padding(Paddings.md),
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

        if (helperText.isNotBlank()) {
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
        }

        if (result.isNotBlank()) {
            Text(
                text = result,
                modifier = Modifier.fillMaxWidth().padding(Paddings.md),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 20.sp,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SenseyRadioButtonPreview() {
    PreviewTheme {
        SenseyRadioButton(
            label = "Shake Detector",
            result = "Result",
            helperText = "Helper text",
            selected = true,
            onSelect = {},
        )
    }
}
