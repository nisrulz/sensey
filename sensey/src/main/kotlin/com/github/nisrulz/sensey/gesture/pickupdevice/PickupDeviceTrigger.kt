
package com.github.nisrulz.sensey.gesture.pickupdevice

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.sqrt

internal class PickupDeviceTrigger(
    private val stableRange: Float = 0.5f,
    private val movingRange: Float = 1.5f,
    private val gravityLower: Float = 9.0f,
    private val gravityUpper: Float = 10.5f,
    private val windowSize: Int = 8,
    private val settleReadings: Int = 6,
) : GestureTrigger<PickupDeviceEvent> {
    private val buffer = FloatArray(windowSize)
    private var bufferIndex = 0
    private var bufferCount = 0
    private var bufferSum = 0f
    private var isHeld = false
    private var settleCount = 0

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): PickupDeviceEvent? {
        val magnitude = computeMagnitude(values)
        val oldValue = buffer[bufferIndex]
        buffer[bufferIndex] = magnitude
        bufferIndex = (bufferIndex + 1) % windowSize
        if (bufferCount < windowSize) bufferCount++
        bufferSum = bufferSum - oldValue + magnitude
        if (bufferCount < 3) return null

        val range = computeRange()
        val mean = bufferSum / bufferCount

        return when {
            isPickedUp(range) -> {
                isHeld = true
                settleCount = 0
                PickupDeviceEvent.PickedUp
            }
            isPutDown(mean, range) -> {
                settleCount++
                if (settleCount >= settleReadings) {
                    isHeld = false
                    PickupDeviceEvent.PutDown
                } else {
                    null
                }
            }
            else -> {
                if (isHeld) settleCount = 0
                null
            }
        }
    }

    private fun computeMagnitude(values: FloatArray): Float =
        sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])

    private fun computeRange(): Float {
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

    private fun isPutDown(
        mean: Float,
        range: Float,
    ): Boolean = isHeld && mean in gravityLower..gravityUpper && range <= stableRange
}
