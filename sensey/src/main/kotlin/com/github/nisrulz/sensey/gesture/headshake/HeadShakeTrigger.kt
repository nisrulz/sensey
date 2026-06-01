package com.github.nisrulz.sensey.gesture.headshake

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GyroIntegrator
import kotlin.math.abs

/**
 * Detects a head shaking (no) gesture using the gyroscope.
 *
 * Algorithm: Uses [GyroIntegrator] to track cumulative rotation. A two-phase
 * oscillation detector tracks "out and back" motion on the horizontal plane
 * (combined Y + Z axes): the horizontal angle must first exceed [angleThreshold]
 * in one direction, then return past zero (complete oscillation), all within
 * [timeWindowMs] (default 800ms). Combining Y+Z handles the device being held
 * at any pitch angle (e.g. tilted with the top higher) — the world-vertical
 * rotation of a head shake projects onto both Y and Z axes, and summing them
 * recovers the full signal regardless of tilt.
 * Direction-agnostic — handles both positive-first and negative-first shakes
 * via a direction normalizer.
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
        // Combine Y (roll) and Z (yaw) to capture head-shake rotation
        // regardless of device pitch tilt. When the phone is held with
        // the top higher, world-vertical rotation projects onto both axes.
        val horizontalAngle = angles[1] + angles[2]

        when (phase) {
            Phase.IDLE -> {
                if (abs(horizontalAngle) > 5f) {
                    direction = if (horizontalAngle > 0) 1 else -1
                    phase = Phase.MOVING_OUT
                    startTime = timestamp
                }
            }
            Phase.MOVING_OUT -> {
                if (horizontalAngle * direction >= angleThreshold) {
                    phase = Phase.MOVING_BACK
                } else if (timestamp - startTime > timeWindowMs) {
                    reset()
                }
            }
            Phase.MOVING_BACK -> {
                if (horizontalAngle * direction <= 0f) {
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
