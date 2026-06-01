package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * A scrollbar thumb that tracks a scrollable list's scroll position.
 * Auto-hides after scrolling stops.
 *
 * Usage:
 * ```
 * val scrollState = rememberScrollState()
 *
 * Box {
 *     Column(Modifier.verticalScroll(scrollState)) { /* items */ }
 *     ScrollbarThumb(
 *         scrollState = scrollState,
 *         modifier = Modifier.align(Alignment.CenterEnd),
 *     )
 * }
 * ```
 *
 * @param scrollState the ScrollState from [rememberScrollState]
 * @param modifier optional [Modifier] applied to the scrollbar container
 * @param thumbColor color of the scrollbar thumb (default: onSurfaceVariant at 30%)
 * @param thumbWidth width in dp (default: 6dp)
 * @param hideDelayMs delay in ms before hiding after scroll stops (default: 1500)
 */
@Composable
fun ScrollbarThumb(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    thumbColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
    thumbWidth: Dp = 6.dp,
    hideDelayMs: Long = 1500L,
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            visible = true
        } else {
            delay(hideDelayMs)
            visible = false
        }
    }

    val targetAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "scrollbarAlpha",
    )

    Box(
        modifier =
            modifier
                .fillMaxHeight()
                .width(thumbWidth)
                .padding(vertical = 4.dp)
                .alpha(targetAlpha),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (scrollState.maxValue > 0) {
                val contentHeight = scrollState.maxValue.toFloat() + size.height
                val thumbHeight = (size.height / contentHeight) * size.height
                val thumbOffset =
                    (scrollState.value.toFloat() / scrollState.maxValue.toFloat().coerceAtLeast(1f)) *
                        (size.height - thumbHeight)

                drawRoundRect(
                    color = thumbColor,
                    topLeft = Offset(0f, thumbOffset),
                    size = Size(size.width, thumbHeight),
                    cornerRadius = CornerRadius(3.dp.toPx()),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ScrollbarThumbPreview() {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.height(200.dp)) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            repeat(20) { index ->
                Text("Item $index", modifier = Modifier.padding(16.dp))
            }
        }
        ScrollbarThumb(
            scrollState = scrollState,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}
