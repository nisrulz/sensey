
package com.github.nisrulz.sensey.gesture.tiltdirection

import com.github.nisrulz.sensey.contract.GestureTrigger

/**
 * Detects the dominant tilt axis and its direction.
 *
 * Algorithm: Compares the absolute values of the three acceleration/gravity
 * components (X, Y, Z). The axis with the largest magnitude above the
 * threshold is considered the dominant tilt axis. A positive value on that
 * axis maps to ANTICLOCKWISE tilt; negative maps to CLOCKWISE.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
 * State: None (stateless computation).
 */
internal class TiltDirectionTrigger(
    private val threshold: Float = 0.5f,
) : GestureTrigger<TiltDirectionEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TiltDirectionEvent? {
        val (x, y, z) = values // Destructure gravity/acceleration components
        val absX = kotlin.math.abs(x)
        val absY = kotlin.math.abs(y)
        val absZ = kotlin.math.abs(z)

        val dominantAxis = maxOf(absX, absY, absZ) // Find the axis with the largest gravity component
        if (dominantAxis < threshold) return null // Below threshold: ignore minor tilts

        return when (dominantAxis) {
            absX -> TiltDirectionEvent.AxisXTilt(directionFor(x)) // Emit X-axis tilt event
            absY -> TiltDirectionEvent.AxisYTilt(directionFor(y)) // Emit Y-axis tilt event
            else -> TiltDirectionEvent.AxisZTilt(directionFor(z)) // Emit Z-axis tilt event
        }
    }

    private fun directionFor(value: Float): TiltDirectionEvent.Direction =
        // Positive value → ANTICLOCKWISE, negative → CLOCKWISE
        if (value > 0) {
            TiltDirectionEvent.Direction.ANTICLOCKWISE
        } else {
            TiltDirectionEvent.Direction.CLOCKWISE
        }
}
