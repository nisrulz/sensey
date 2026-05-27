package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val PrimaryBlue = Color(0xFF35518C)
internal val AccentOrange = Color(0xFFFF9800)
internal val White = Color(0xFFFFFFFF)
internal val DividerGray = Color(0xFFB6B6B6)

@Composable
internal fun SenseyRadioButton(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            color = White,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 20.dp),
        )
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = AccentOrange,
                unselectedColor = White,
            ),
        )
    }
}

@Composable
internal fun ResultArea(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(0.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = DividerGray,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
        )
    }
}
