
package com.github.nisrulz.sensey.gesture.touch

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.CornerType
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.EdgeType

/**
 * Configuration for [TouchPlugin].
 *
 * Each gesture feature is independently controlled by its own config
 * sub-object with an `enabled` flag. By default only [TapsConfig] and
 * [SwipeConfig] are enabled; all other features are opt-in.
 *
 * Example: enable edge swipes with a custom threshold:
 * ```
 * TouchConfig(edgeSwipe = EdgeSwipeConfig(enabled = true, edgeThresholdDp = 32.dp))
 * ```
 *
 * Convenience wrapper functions ([edgeSwipePlugin], [cornerSwipePlugin], etc.)
 * internally create a [TouchPlugin] with a pre-configured [TouchConfig].
 */
data class TouchConfig(
    val taps: TapsConfig = TapsConfig(),
    val swipe: SwipeConfig = SwipeConfig(),
    val edgeSwipe: EdgeSwipeConfig = EdgeSwipeConfig(),
    val cornerSwipe: CornerSwipeConfig = CornerSwipeConfig(),
    val twoFingerSwipe: TwoFingerSwipeConfig = TwoFingerSwipeConfig(),
    val longPressDrag: LongPressDragConfig = LongPressDragConfig(),
    val pinchScale: PinchScaleConfig = PinchScaleConfig(),
)

data class TapsConfig(
    val enabled: Boolean = true,
    val nTapCount: Int = 3,
    val nTapWindowMs: Long = 400L,
)

data class SwipeConfig(
    val enabled: Boolean = true,
    val minDistance: Float = 120f,
    val velocityThreshold: Float = 200f,
    val diagonalOnly: Boolean = false,
)

data class EdgeSwipeConfig(
    val enabled: Boolean = false,
    val edgeThresholdDp: Dp = 48.dp,
    val enabledEdges: Set<EdgeType> = EdgeType.entries.toSet(),
    val minDistance: Float = 120f,
)

data class CornerSwipeConfig(
    val enabled: Boolean = false,
    val cornerRadiusDp: Dp = 48.dp,
    val enabledCorners: Set<CornerType> = CornerType.entries.toSet(),
    val minDistance: Float = 120f,
)

data class TwoFingerSwipeConfig(
    val enabled: Boolean = false,
    val minDistance: Float = 80f,
)

data class LongPressDragConfig(
    val enabled: Boolean = false,
    val minDistance: Float = 20f,
)

data class PinchScaleConfig(
    val enabled: Boolean = false,
)
