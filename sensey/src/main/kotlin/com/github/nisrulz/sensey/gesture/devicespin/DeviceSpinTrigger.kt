
package com.github.nisrulz.sensey.gesture.devicespin

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GyroIntegrator

/**
 * Detects a spin gesture using the gyroscope.
 *
 * Algorithm: Uses [GyroIntegrator] to track cumulative rotation on all
 * axes within a [timeWindowMs] window. When any axis exceeds
 * [angleThreshold] (default 270°) the gesture fires. Compared to
 * [TurnOverTrigger], this monitors all three axes with a higher threshold
 * and a time window constraint to prevent false positives from slow drift.
 * Expected sensor: Gyroscope (TYPE_GYROSCOPE).
 * State: GyroIntegrator (shared), startTime (window start).
 */
internal class DeviceSpinTrigger(
    private val angleThreshold: Float = 270f,
    private val timeWindowMs: Long = 2000L,
    private val integrator: GyroIntegrator = GyroIntegrator(),
) : GestureTrigger<DeviceSpinEvent> {
    private var startTime = 0L // Start timestamp of the current time window

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): DeviceSpinEvent? {
        if (startTime == 0L) startTime = timestamp // Initialize the time window on first reading
        if (timestamp - startTime > timeWindowMs) {
            // Time window expired without reaching the threshold → reset
            integrator.reset()
            startTime = timestamp
            return null
        }
        integrator.update(values, timestamp) // Integrate angular velocity
        if (integrator.isAnyAxisAbove(angleThreshold)) {
            // Some axis completed a full spin
            integrator.reset()
            startTime = 0L
            return DeviceSpinEvent.Spun
        }
        return null
    }
}
