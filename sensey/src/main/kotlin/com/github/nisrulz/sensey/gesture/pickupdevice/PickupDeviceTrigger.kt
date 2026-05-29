
package com.github.nisrulz.sensey.gesture.pickupdevice

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

/**
 * Detects when the device is picked up or put down.
 *
 * Algorithm: Maintains a circular buffer of recent acceleration magnitudes.
 * The range (max - min) of the buffer indicates movement. A high range
 * above the moving threshold signals a pickup. When the mean acceleration
 * returns to the gravity range and the range is stable for a configurable
 * duration, the device is declared put down.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
 * State: buffer (circular), bufferIndex, bufferCount, bufferSum (running
 * mean), isHeld (held flag), settleStartTime (timestamp of first stable reading).
 */
internal class PickupDeviceTrigger(
    private val stableRange: Float = 0.5f,
    private val movingRange: Float = 1.5f,
    private val gravityLower: Float = 9.0f,
    private val gravityUpper: Float = 10.5f,
    private val windowSize: Int = 8,
    private val settleTimeMs: Long = 1000L,
) : GestureTrigger<PickupDeviceEvent> {
    private val buffer = FloatArray(windowSize) // Circular buffer of recent acceleration magnitudes
    private var bufferIndex = 0 // Current write position in the circular buffer
    private var bufferCount = 0 // Number of valid entries in the buffer (grows until window is full)
    private var bufferSum = 0f // Running sum of buffer contents for mean computation
    private var isHeld = false // Whether the device is currently held (for put-down detection)
    private var settleStartTime = 0L // Timestamp when stable conditions were first met

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): PickupDeviceEvent? {
        val magnitude = computeMagnitude(values) // Compute the Euclidean norm of the acceleration vector
        val oldValue = buffer[bufferIndex] // Value being overwritten in the circular buffer
        buffer[bufferIndex] = magnitude
        bufferIndex = (bufferIndex + 1) % windowSize // Advance the write position
        if (bufferCount < windowSize) bufferCount++ // Grow the count until the buffer is full
        bufferSum = bufferSum - oldValue + magnitude // Update the running sum
        if (bufferCount < 3) return null // Need a minimum number of samples before any decision

        val range = computeRange() // Compute range (max - min) of the buffer
        val mean = bufferSum / bufferCount // Compute the mean acceleration

        return when {
            isPickedUp(range) -> {
                // Range exceeds the moving threshold: device was picked up
                isHeld = true
                settleStartTime = 0L
                PickupDeviceEvent.PickedUp
            }
            isPutDown(mean, range) -> {
                // Mean in gravity range and range stable: possible put-down
                if (settleStartTime == 0L) settleStartTime = timestamp
                if (timestamp - settleStartTime >= settleTimeMs) {
                    // Device stable for the full settle duration → put down
                    isHeld = false
                    settleStartTime = 0L
                    PickupDeviceEvent.PutDown
                } else {
                    null // Not yet stable long enough
                }
            }
            else -> {
                // Idle: reset settle timer if held
                if (isHeld) settleStartTime = 0L
                null
            }
        }
    }

    private fun computeMagnitude(values: FloatArray): Float =
        // Euclidean norm of the acceleration vector
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun computeRange(): Float {
        // Compute the max - min range over the valid buffer entries
        var min = Float.MAX_VALUE
        var max = Float.MIN_VALUE
        for (i in 0 until bufferCount) {
            val v = buffer[i]
            if (v < min) min = v
            if (v > max) max = v
        }
        return max - min
    }

    private fun isPickedUp(range: Float): Boolean = !isHeld && range > movingRange
    // Device is not held and buffer range exceeds the moving threshold → picked up

    private fun isPutDown(
        mean: Float,
        range: Float,
    ): Boolean = isHeld && mean in gravityLower..gravityUpper && range <= stableRange
    // Device is held, mean is near gravity, and range is stable → put down
}
