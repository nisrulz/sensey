
package com.github.nisrulz.sensey.gesture.touch

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider

class TouchPlugin(
    private val config: TouchConfig = TouchConfig(),
    private val dispatcher: (TouchEvent) -> Unit,
) : GesturePlugin {
    override val key = "TouchPlugin"

    private val providers = mutableListOf<ComposeGestureProvider>()
    private var tapCount = 0
    private var lastTapTime = 0L

    override fun onRegister(sensey: Sensey) {
        buildProviders()
        providers.forEach { sensey.registerComposeGestureProvider(it) }
    }

    override fun onUnregister(sensey: Sensey) {
        providers.forEach { sensey.unregisterComposeGestureProvider(it) }
        providers.clear()
        tapCount = 0
        lastTapTime = 0L
    }

    private fun buildProviders() {
        if (config.taps.enabled) {
            providers.add(ComposeGestureProvider { installTaps() })
        }
        if (config.swipe.enabled) {
            providers.add(ComposeGestureProvider { installSwipe() })
        }
        if (config.edgeSwipe.enabled) {
            providers.add(ComposeGestureProvider { installEdgeSwipe() })
        }
        if (config.cornerSwipe.enabled) {
            providers.add(ComposeGestureProvider { installCornerSwipe() })
        }
        if (config.twoFingerSwipe.enabled) {
            providers.add(ComposeGestureProvider { installTwoFingerSwipe() })
        }
        if (config.longPressDrag.enabled) {
            providers.add(ComposeGestureProvider { installLongPressDrag() })
        }
        if (config.pinchScale.enabled) {
            providers.add(ComposeGestureProvider { installPinchScale() })
        }
    }

    private suspend fun PointerInputScope.installTaps() {
        detectTapGestures(
            onTap = {
                val now = System.currentTimeMillis()
                tapCount = if (now - lastTapTime <= config.taps.nTapWindowMs) tapCount + 1 else 1
                lastTapTime = now
                if (tapCount >= config.taps.nTapCount) {
                    tapCount = 0
                    dispatcher(TouchEvent.Tap.NTap(config.taps.nTapCount))
                } else {
                    dispatcher(TouchEvent.Tap.Single)
                }
            },
            onDoubleTap = { dispatcher(TouchEvent.Tap.Double) },
            onLongPress = { dispatcher(TouchEvent.LongPress) },
        )
    }

    private suspend fun PointerInputScope.installSwipe() {
        val trigger =
            SwipeScrollTrigger(
                minDistance = config.swipe.minDistance,
                velocityThreshold = config.swipe.velocityThreshold,
                diagonalOnly = config.swipe.diagonalOnly,
            )
        var dragStart = Offset.Zero
        detectDragGestures(
            onDragStart = { dragStart = it },
            onDrag = { change, dragAmount ->
                change.consume()
                val event =
                    trigger.evaluate(
                        deltaX = (change.position - dragStart).x,
                        deltaY = (change.position - dragStart).y,
                        velocityX = dragAmount.x,
                        velocityY = dragAmount.y,
                    )
                when (event) {
                    is TouchEvent.Swipe -> dispatcher(event)
                    is TouchEvent.Scroll -> dispatcher(event)
                    else -> {}
                }
            },
            onDragEnd = { dragStart = Offset.Zero },
            onDragCancel = { dragStart = Offset.Zero },
        )
    }

    private suspend fun PointerInputScope.installEdgeSwipe() {
        val edgeThresholdPx = with(density) { config.edgeSwipe.edgeThresholdDp.toPx() }
        val trigger =
            EdgeSwipeTrigger(
                edgeThresholdPx = edgeThresholdPx,
                enabledEdges = config.edgeSwipe.enabledEdges,
                screenW = size.width.toFloat(),
                screenH = size.height.toFloat(),
                minDistance = config.edgeSwipe.minDistance,
            )
        var dragStart = Offset.Zero
        var dragEnd = Offset.Zero
        detectDragGestures(
            onDragStart = {
                dragStart = it
                dragEnd = it
            },
            onDrag = { change, _ ->
                change.consume()
                dragEnd = change.position
            },
            onDragEnd = {
                trigger
                    .evaluate(
                        startX = dragStart.x,
                        startY = dragStart.y,
                        endX = dragEnd.x,
                        endY = dragEnd.y,
                    )?.let(dispatcher)
                dragStart = Offset.Zero
                dragEnd = Offset.Zero
            },
            onDragCancel = {
                dragStart = Offset.Zero
                dragEnd = Offset.Zero
            },
        )
    }

    private suspend fun PointerInputScope.installCornerSwipe() {
        val cornerRadiusPx = with(density) { config.cornerSwipe.cornerRadiusDp.toPx() }
        val trigger =
            CornerSwipeTrigger(
                cornerRadiusPx = cornerRadiusPx,
                enabledCorners = config.cornerSwipe.enabledCorners,
                screenW = size.width.toFloat(),
                screenH = size.height.toFloat(),
                minDistance = config.cornerSwipe.minDistance,
            )
        var dragStart = Offset.Zero
        var dragEnd = Offset.Zero
        detectDragGestures(
            onDragStart = {
                dragStart = it
                dragEnd = it
            },
            onDrag = { change, _ ->
                change.consume()
                dragEnd = change.position
            },
            onDragEnd = {
                trigger
                    .evaluate(
                        startX = dragStart.x,
                        startY = dragStart.y,
                        endX = dragEnd.x,
                        endY = dragEnd.y,
                    )?.let(dispatcher)
                dragStart = Offset.Zero
                dragEnd = Offset.Zero
            },
            onDragCancel = {
                dragStart = Offset.Zero
                dragEnd = Offset.Zero
            },
        )
    }

    private suspend fun PointerInputScope.installTwoFingerSwipe() {
        val trigger =
            TwoFingerSwipeTrigger(
                minDistance = config.twoFingerSwipe.minDistance,
            )
        awaitEachGesture {
            var dragStart = Offset.Zero
            var tracking = false
            var event = awaitPointerEvent()
            do {
                val active = event.changes.filter { it.pressed }
                if (active.size >= 2 && !tracking) {
                    dragStart =
                        Offset(
                            active.map { it.position.x }.average().toFloat(),
                            active.map { it.position.y }.average().toFloat(),
                        )
                    tracking = true
                }
                if (tracking && active.size >= 2) {
                    val centroid =
                        Offset(
                            active.map { it.position.x }.average().toFloat(),
                            active.map { it.position.y }.average().toFloat(),
                        )
                    trigger
                        .evaluate(
                            panX = centroid.x - dragStart.x,
                            panY = centroid.y - dragStart.y,
                        )?.let(dispatcher)
                    active.forEach { it.consume() }
                }
                if (active.size < 2) tracking = false
                event = awaitPointerEvent()
            } while (event.changes.any { it.pressed })
        }
    }

    private suspend fun PointerInputScope.installLongPressDrag() {
        val trigger =
            LongPressDragTrigger(
                minDistance = config.longPressDrag.minDistance,
            )
        var dragStart = Offset.Zero
        detectDragGesturesAfterLongPress(
            onDragStart = { dragStart = it },
            onDrag = { change, _ ->
                change.consume()
                val delta = change.position - dragStart
                trigger.evaluate(deltaX = delta.x, deltaY = delta.y)?.let(dispatcher)
            },
            onDragEnd = { dragStart = Offset.Zero },
            onDragCancel = { dragStart = Offset.Zero },
        )
    }

    private suspend fun PointerInputScope.installPinchScale() {
        val trigger = PinchScaleTrigger()
        detectTransformGestures { _, _, zoom, _ ->
            trigger.evaluate(scaleFactor = zoom)?.let(dispatcher)
        }
    }
}
