package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun DetectionIndicator(
    detected: Boolean,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    val color = if (detected) AccentOrange else White
    Box(
        modifier =
            modifier
                .size(48.dp)
                .clip(shape)
                .background(color)
                .border(width = 1.dp, color = Color.Black, shape = shape),
        contentAlignment = Alignment.Center,
    ) {
        if (detected) Text(text = "⚡")
    }
}

@Preview
@Composable
private fun DetectionIndicatorPreview() {
    DetectionIndicator(detected = false)
}

@Preview
@Composable
private fun DetectionIndicatorDetectedPreview() {
    DetectionIndicator(detected = true)
}
