package com.github.nisrulz.sensey.gesture.headshake

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GyroIntegrator
import kotlin.math.abs

/**
 * Detects a head shaking (no) gesture using the gyroscope.
 *
 * Algorithm: Uses [GyroIntegrator] to track cumulative Z-axis (yaw) rotation.
 * A two-phase oscillation detector tracks "out and back" motion: the Z-angle
 * must first exceed [angleThreshold] in one direction, then return past zero
 * (complete oscillation), all within [timeWindowMs] (default 800ms). Direction-agnostic —
 * handles both positive-first and negative-first shakes via a direction normalizer.
 * Expected sensor: Gyroscope (TYPE_GYROSCOPE).
 * State: GyroIntegrator, Phase (IDLE/MOVING_OUT/MOVING_BACK/COMPLETE), startTime, lastFireTime (cooldown).
 */
internal class HeadShakeTrigger(
    private val angleThreshold: Float = 30f,
    private val timeWindowMs: Long = 800L,
    private val cooldownMs: Long = 1500L,
) : GestureTrigger<HeadShakeEvent> {
    private val integrator = GyroIntegrator()
    private var phase = Phase.IDLE
    private var direction = 1
    private var startTime = -1L
    private var lastFireTime = -1L

    private enum class Phase { IDLE, MOVING_OUT, MOVING_BACK, COMPLETE }

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): HeadShakeEvent? {
        if (lastFireTime != -1L && timestamp < lastFireTime + cooldownMs) return null

        val angles = integrator.update(values, timestamp)
        val z = angles[2]

        when (phase) {
            Phase.IDLE -> {
                if (abs(z) > 5f) {
                    direction = if (z > 0) 1 else -1
                    phase = Phase.MOVING_OUT
                    startTime = timestamp
                }
            }
            Phase.MOVING_OUT -> {
                if (z * direction >= angleThreshold) {
                    phase = Phase.MOVING_BACK
                } else if (timestamp - startTime > timeWindowMs) {
                    reset()
                }
            }
            Phase.MOVING_BACK -> {
                if (z * direction <= 0f) {
                    phase = Phase.COMPLETE
                } else if (timestamp - startTime > timeWindowMs) {
                    reset()
                }
            }
            Phase.COMPLETE -> {
                lastFireTime = timestamp
                reset()
                return HeadShakeEvent
            }
        }

        return null
    }

    private fun reset() {
        integrator.reset()
        phase = Phase.IDLE
        startTime = -1L
    }
}
