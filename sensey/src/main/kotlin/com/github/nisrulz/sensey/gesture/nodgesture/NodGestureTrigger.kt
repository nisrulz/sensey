package com.github.nisrulz.sensey.gesture.nodgesture

import com.github.nisrulz.sensey.contract.GestureTrigger
import com.github.nisrulz.sensey.internal.GyroIntegrator
import kotlin.math.abs

/**
 * Detects a nodding (yes) gesture using the gyroscope.
 *
 * Algorithm: Uses [GyroIntegrator] to track cumulative X-axis (pitch) rotation.
 * A two-phase oscillation detector tracks "out and back" motion: the X-angle
 * must first exceed [angleThreshold] in one direction, then return past zero
 * (complete oscillation), all within [timeWindowMs] (default 800ms). Direction-agnostic —
 * handles both positive-first and negative-first nods via a direction normalizer.
 * Expected sensor: Gyroscope (TYPE_GYROSCOPE).
 * State: GyroIntegrator, Phase (IDLE/MOVING_OUT/MOVING_BACK/COMPLETE), startTime, lastFireTime (cooldown).
 */
internal class NodGestureTrigger(
    private val angleThreshold: Float = 30f,
    private val timeWindowMs: Long = 800L,
    private val cooldownMs: Long = 1500L,
) : GestureTrigger<NodGestureEvent> {
    private val integrator = GyroIntegrator()
    private var phase = Phase.IDLE
    private var direction = 1
    private var startTime = -1L
    private var lastFireTime = -1L

    private enum class Phase { IDLE, MOVING_OUT, MOVING_BACK, COMPLETE }

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): NodGestureEvent? {
        if (lastFireTime != -1L && timestamp < lastFireTime + cooldownMs) return null

        val angles = integrator.update(values, timestamp)
        val x = angles[0]

        when (phase) {
            Phase.IDLE -> {
                if (abs(x) > 5f) {
                    direction = if (x > 0) 1 else -1
                    phase = Phase.MOVING_OUT
                    startTime = timestamp
                }
            }
            Phase.MOVING_OUT -> {
                if (x * direction >= angleThreshold) {
                    phase = Phase.MOVING_BACK
                } else if (timestamp - startTime > timeWindowMs) {
                    reset()
                }
            }
            Phase.MOVING_BACK -> {
                if (x * direction <= 0f) {
                    phase = Phase.COMPLETE
                } else if (timestamp - startTime > timeWindowMs) {
                    reset()
                }
            }
            Phase.COMPLETE -> {
                lastFireTime = timestamp
                reset()
                return NodGestureEvent
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
