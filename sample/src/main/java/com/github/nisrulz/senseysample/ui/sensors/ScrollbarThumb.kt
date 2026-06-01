package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * An auto-hiding scrollbar thumb for any scrollable list.
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
 * @param thumbColor color of the scrollbar thumb (default: onSurfaceVariant at 30%)
 * @param thumbWidth width in dp (default: 6dp)
 * @param hideDelayMs milliseconds of inactivity before fading out (default: 900ms)
 */
@Composable
fun ScrollbarThumb(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    thumbColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
    thumbWidth: Dp = 6.dp,
    hideDelayMs: Long = 900L,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow { scrollState.value }
            .collect {
                visible = true
                delay(hideDelayMs)
                visible = false
            }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .width(thumbWidth)
                    .padding(vertical = 4.dp),
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                val contentHeight = scrollState.maxValue.toFloat() + size.height
                val thumbHeight = (size.height / contentHeight) * size.height
                val thumbOffset =
                    (scrollState.value.toFloat() / scrollState.maxValue.toFloat().coerceAtLeast(1f)) *
                        (size.height - thumbHeight)

                if (scrollState.maxValue > 0) {
                    drawRoundRect(
                        color = thumbColor,
                        topLeft = Offset(0f, thumbOffset),
                        size =
                            androidx.compose.ui.geometry
                                .Size(size.width, thumbHeight),
                        cornerRadius =
                            androidx.compose.ui.geometry
                                .CornerRadius(3.dp.toPx()),
                    )
                }
            }
        }
    }
}
