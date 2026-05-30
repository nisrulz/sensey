
package com.github.nisrulz.sensey.internal

import kotlin.math.abs

/**
 * Tracks cumulative angular displacement from gyroscope readings.
 *
 * Converts angular velocity (rad/s) to degrees and accumulates over time.
 * Shared by both [TurnOverTrigger] and [DeviceSpinTrigger].
 * State: angles (cumulative rotation per axis in degrees), lastTimestamp
 * (previous reading time for delta computation).
 */
internal class GyroIntegrator {
    private var angles = FloatArray(3) // Cumulative rotation per axis (X, Y, Z) in degrees
    private var lastTimestamp = -1L // Timestamp of the last update (-1 = uninitialized)

    /**
     * Integrate angular velocity over time. Returns the current cumulative
     * rotation per axis in degrees.
     */
    fun update(
        angularVelocity: FloatArray,
        timestamp: Long,
    ): FloatArray {
        if (lastTimestamp != -1L) {
            val dt = (timestamp - lastTimestamp) / 1000f // Convert ms to seconds
            angles[0] += angularVelocity[0] * dt * RAD_TO_DEG
            angles[1] += angularVelocity[1] * dt * RAD_TO_DEG
            angles[2] += angularVelocity[2] * dt * RAD_TO_DEG
        }
        lastTimestamp = timestamp
        return angles
    }

    /** Reset cumulative angles and last-timestamp. */
    fun reset() {
        angles = FloatArray(3)
        lastTimestamp = -1L
    }

    /** Absolute cumulative rotation per axis (ignoring direction). */
    val absolute: FloatArray
        get() = floatArrayOf(abs(angles[0]), abs(angles[1]), abs(angles[2]))

    /** True when any axis exceeds the given threshold in absolute rotation. */
    fun isAnyAxisAbove(threshold: Float): Boolean =
        abs(angles[0]) > threshold || abs(angles[1]) > threshold || abs(angles[2]) > threshold

    companion object {
        private const val RAD_TO_DEG = 57.29578f // 180 / π
    }
}
