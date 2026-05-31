package com.github.nisrulz.senseysample.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SenseyRadioButton(
    label: String,
    result: String = "Hello",
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(8.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onSelect() }
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
            )
            RadioButton(
                selected = selected,
                onClick = null,
                colors =
                    RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = Color.LightGray,
                    ),
            )
        }

        if (result.isNotBlank()) {
            Text(
                text = result,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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
        SenseyRadioButton(label = "Shake Detector", selected = true, onSelect = {})
    }
}
