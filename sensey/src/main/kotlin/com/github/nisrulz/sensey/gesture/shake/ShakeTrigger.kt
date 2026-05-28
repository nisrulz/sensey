
package com.github.nisrulz.sensey.gesture.shake

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

internal class ShakeTrigger(
    private val threshold: Float = 3f,
    private val timeBeforeDeclaringShakeStopped: Long = 1000L,
) : GestureTrigger<ShakeEvent> {
    private var accelCurrent = 0f
    private var accelDelta = 0f
    private var isShaking = false
    private var lastShakeTime = 0L
    private var hasBaseline = false

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ShakeEvent? {
        val magnitude = computeMagnitude(values)
        if (!hasBaseline) return initializeBaseline(magnitude)

        val delta = magnitude - accelCurrent
        accelCurrent = magnitude
        accelDelta = accelDelta * SMOOTHING_FACTOR + delta

        if (isShakingStopped(timestamp)) {
            isShaking = false
            return ShakeEvent.Stopped
        }

        if (accelDelta > threshold) {
            lastShakeTime = timestamp
            isShaking = true
            return ShakeEvent.Detected
        }

        return null
    }

    private fun initializeBaseline(magnitude: Float): ShakeEvent? {
        accelCurrent = magnitude
        accelDelta = 0f
        hasBaseline = true
        return null
    }

    private fun computeMagnitude(values: FloatArray): Float =
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun isShakingStopped(timestamp: Long): Boolean {
        val timeSinceLastShake = timestamp - lastShakeTime
        return timeSinceLastShake > timeBeforeDeclaringShakeStopped && isShaking
    }

    companion object {
        private const val SMOOTHING_FACTOR = 0.9f
    }
}
