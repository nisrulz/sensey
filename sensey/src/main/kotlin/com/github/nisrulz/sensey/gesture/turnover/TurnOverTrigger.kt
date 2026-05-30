
package com.github.nisrulz.sensey.gesture.turnover

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GyroIntegrator
import kotlin.math.sqrt

/**
 * Detects a full 180-degree flip of the device using the gyroscope.
 *
 * Algorithm: Uses [GyroIntegrator] to track cumulative rotation across
 * all axes. When the net rotation magnitude exceeds [angleThreshold]
 * (default 130°) the gesture fires and the integrator is reset. This is
 * more precise than the accelerometer-based [FlipTrigger] since it directly
 * measures angular motion rather than inferring orientation from gravity.
 * Expected sensor: Gyroscope (TYPE_GYROSCOPE).
 * State: GyroIntegrator (shared via constructor parameter).
 */
internal class TurnOverTrigger(
    private val angleThreshold: Float = 130f,
    private val integrator: GyroIntegrator = GyroIntegrator(),
) : GestureTrigger<TurnOverEvent> {
    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TurnOverEvent? {
        integrator.update(values, timestamp)
        val netRotation = netRotationMagnitude()
        if (netRotation > angleThreshold) {
            integrator.reset()
            return TurnOverEvent.Flipped
        }
        return null
    }

    private fun netRotationMagnitude(): Float {
        val abs = integrator.absolute
        return sqrt(abs[0] * abs[0] + abs[1] * abs[1] + abs[2] * abs[2])
    }
}
