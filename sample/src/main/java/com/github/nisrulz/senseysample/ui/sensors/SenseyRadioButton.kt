package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    RadioButtonRow(
        label = label,
        helperText = helperText,
        selected = selected,
        onSelect = onSelect,
        showHelperText = helperText.isNotBlank(),
    ) {
        if (result.isNotBlank()) {
            Text(
                text = result,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Paddings.sm, vertical = Paddings.md),
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
