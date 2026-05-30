package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.nisrulz.sensey.gesture.compose.senseyGestures

@Composable
internal fun SenseyRadioButtonWithTouchArea(
    label: String,
    result: String = "Hello",
    helperText: String = "",
    selected: Boolean,
    showHitArea: Boolean = false,
    onSelect: () -> Unit,
) {
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onSelect() }
                    .padding(vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                color = White,
                fontSize = 16.sp,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(vertical = 20.dp),
            )
            RadioButton(
                selected = selected,
                onClick = null,
                colors =
                    RadioButtonDefaults.colors(
                        selectedColor = AccentOrange,
                        unselectedColor = White,
                    ),
            )
        }

        Text(text = helperText, color = DividerGray, modifier = Modifier.padding(bottom = 8.dp))

        if (showHitArea || result.isNotBlank()) {
            ResultArea(
                text = result.ifBlank { "[ Hit Area ]" },
                modifier =
                    Modifier
                        .senseyGestures()
                        .padding(bottom = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun SenseyRadioButtonPreview() {
    SenseyRadioButtonWithTouchArea(
        label = "Shake Detector",
        selected = true,
        onSelect = {},
        helperText = "Some Helper Text",
    )
}
