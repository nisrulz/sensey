
package com.github.nisrulz.sensey.gesture.tiltdirection

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class TiltDirectionTrigger(
    private val threshold: Float = 0.5f,
) : GestureTrigger<TiltDirectionEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TiltDirectionEvent? {
        val (x, y, z) = values
        val absX = kotlin.math.abs(x)
        val absY = kotlin.math.abs(y)
        val absZ = kotlin.math.abs(z)

        val dominantAxis = maxOf(absX, absY, absZ)
        if (dominantAxis < threshold) return null

        return when (dominantAxis) {
            absX -> TiltDirectionEvent.AxisXTilt(directionFor(x))
            absY -> TiltDirectionEvent.AxisYTilt(directionFor(y))
            else -> TiltDirectionEvent.AxisZTilt(directionFor(z))
        }
    }

    private fun directionFor(value: Float): TiltDirectionEvent.Direction =
        if (value > 0) {
            TiltDirectionEvent.Direction.ANTICLOCKWISE
        } else {
            TiltDirectionEvent.Direction.CLOCKWISE
        }
}
