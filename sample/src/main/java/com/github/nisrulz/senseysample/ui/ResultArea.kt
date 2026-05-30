package com.github.nisrulz.senseysample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun ResultArea(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.inverseOnSurface, RoundedCornerShape(8.dp))
                .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 25.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

@PreviewLightDark
@Composable
private fun ResultAreaPreview() {
    PreviewTheme {
        ResultArea(text = "No gesture detected")
    }
}
