
package com.github.nisrulz.sensey.gesture.pinchscale

import com.github.nisrulz.sensey.contract.GestureTrigger

/**
 * Detects pinch-in and pinch-out (scale) gestures.
 *
 * Algorithm: Monitors the scale factor from a touch gesture. When the factor
 * exceeds 1.01 (pinch-in) or falls below 0.99 (pinch-out) consecutive
 * readings are counted. The event is only emitted after a confirmation count
 * is reached, providing debounce against jittery touch input. Tracks the
 * last emitted direction so opposite-direction events can be reported.
 * Expected sensor: Touch input (scale gesture detector).
 * State: eventOccurred (last emitted type), scaleInCount / scaleOutCount
 * (consecutive readings for confirmation).
 */
internal class PinchScaleTrigger : GestureTrigger<PinchScaleEvent> {
    private var eventOccurred = 0 // Tracks the last dispatched event type (1=scale in, 2=scale out)
    private var scaleInCount = 0 // Consecutive scale-in readings for confirmation
    private var scaleOutCount = 0 // Consecutive scale-out readings for confirmation

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): PinchScaleEvent? {
        val scaleFactor = values.getOrNull(0) ?: return null // Extract scale factor; abort if unavailable

        return when {
            isScalingIn(scaleFactor) -> {
                // Scale factor > 1.01 → pinching in
                scaleInCount++
                if (eventOccurred != SCALE_IN && scaleInCount > CONFIRMATION_THRESHOLD) {
                    eventOccurred = SCALE_IN
                    scaleOutCount = 0
                    PinchScaleEvent(scaleFactor, isScalingOut = false) // Emit scale-in event
                } else {
                    null // Not yet confirmed
                }
            }
            isScalingOut(scaleFactor) -> {
                // Scale factor < 0.99 → pinching out
                scaleOutCount++
                if (eventOccurred != SCALE_OUT && scaleOutCount > CONFIRMATION_THRESHOLD) {
                    eventOccurred = SCALE_OUT
                    scaleInCount = 0
                    PinchScaleEvent(scaleFactor, isScalingOut = true) // Emit scale-out event
                } else {
                    null // Not yet confirmed
                }
            }
            else -> null // No significant scale change
        }
    }

    fun reset() {
        // Reset all state for a new gesture sequence
        eventOccurred = 0
        scaleInCount = 0
        scaleOutCount = 0
    }

    private fun isScalingIn(scaleFactor: Float): Boolean = scaleFactor > SCALE_IN_THRESHOLD

    private fun isScalingOut(scaleFactor: Float): Boolean = scaleFactor < SCALE_OUT_THRESHOLD

    companion object {
        private const val SCALE_IN_THRESHOLD = 1.01f // Scale factor above this → pinch in
        private const val SCALE_OUT_THRESHOLD = 0.99f // Scale factor below this → pinch out
        private const val CONFIRMATION_THRESHOLD = 2 // Consecutive readings needed to confirm
        private const val SCALE_IN = 1
        private const val SCALE_OUT = 2
    }
}
