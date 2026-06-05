package com.github.nisrulz.senseysample.ui.sensors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Modifier.touchIndicator(
    show: Boolean,
    color: Color = Color(0x8C000000),
): Modifier {
    if (!show) return this

    val touchPoints = remember { mutableStateMapOf<Long, Offset>() }
    val scope = rememberCoroutineScope()
    val clearJobs = remember { mutableMapOf<Long, Job>() }

    return this
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    for (change in event.changes) {
                        val id = change.id.value
                        if (change.pressed) {
                            clearJobs[id]?.cancel()
                            touchPoints[id] = change.position
                        } else {
                            clearJobs[id]?.cancel()
                            clearJobs[id] =
                                scope.launch {
                                    delay(300)
                                    touchPoints.remove(id)
                                    clearJobs.remove(id)
                                }
                        }
                    }
                }
            }
        }.drawWithContent {
            drawContent()
            for (pos in touchPoints.values) {
                drawCircle(color = color, radius = 12.dp.toPx(), center = pos)
            }
        }
}
