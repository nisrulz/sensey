package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.layout.Arrangement
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

@Composable
internal fun SenseyRadioButton(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
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
            onClick = onSelect,
            colors =
                RadioButtonDefaults.colors(
                    selectedColor = AccentOrange,
                    unselectedColor = White,
                ),
        )
    }
}

@Preview
@Composable
private fun SenseyRadioButtonPreview() {
    SenseyRadioButton(label = "Shake Detector", selected = true, onSelect = {})
}
