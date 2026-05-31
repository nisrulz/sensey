
package com.github.nisrulz.sensey.gesture.orientation

import com.github.nisrulz.sensey.contract.GestureTrigger

/**
 * Detects the device orientation (portrait, landscape, and their reverses).
 *
 * Algorithm: Smoothes pitch and roll via a configurable moving-average window.
 * Classifies the orientation using the averaged pitch and roll angles with
 * hysteresis from the previous orientation. Emits an event only when the
 * orientation actually changes from the last reported state.
 * Expected sensor: Accelerometer (TYPE_ACCELEROMETER).
 * State: eventOccurred (last emitted orientation, for deduplication),
 * currentOrientation, pitchBuffer/rollBuffer (circular), pitchSum/rollSum
 * (running sums), bufferIndex, isBufferInitialized.
 */
internal class OrientationTrigger(
    private val smoothness: Int = 1,
) : GestureTrigger<OrientationEvent> {
    private val windowSize = smoothness.coerceAtLeast(1) // Moving-average window size (at least 1)
    private var eventOccurred = 0 // Tracks the last emitted orientation to deduplicate
    private var currentOrientation = PORTRAIT // Currently classified orientation
    private val pitchBuffer = FloatArray(windowSize) // Circular buffer for pitch averaging
    private val rollBuffer = FloatArray(windowSize) // Circular buffer for roll averaging
    private var pitchSum = 0f // Running sum of the pitch buffer
    private var rollSum = 0f // Running sum of the roll buffer
    private var bufferIndex = 0 // Current write index in the circular buffers
    private var isBufferInitialized = false // Whether the moving-average buffers have been filled

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): OrientationEvent? {
        val pitch = values.getOrNull(0) ?: return null // Extract pitch angle; abort if unavailable
        val roll = values.getOrNull(1) ?: return null // Extract roll angle; abort if unavailable

        if (!isBufferInitialized) initializeBuffer(pitch, roll) // First call: fill buffer with initial values

        // Update the pitch circular buffer and recompute the average
        val oldPitch = pitchBuffer[bufferIndex]
        pitchBuffer[bufferIndex] = pitch
        pitchSum = pitchSum - oldPitch + pitch
        val averagePitch = pitchSum / windowSize

        // Update the roll circular buffer and recompute the average
        val oldRoll = rollBuffer[bufferIndex]
        rollBuffer[bufferIndex] = roll
        rollSum = rollSum - oldRoll + roll
        val averageRoll = rollSum / windowSize

        bufferIndex = (bufferIndex + 1) % windowSize // Advance the write index

        // Classify orientation based on averaged pitch and roll
        currentOrientation =
            classifyOrientation(averagePitch, averageRoll, currentOrientation)
        return toOrientationEvent(currentOrientation) // Emit if orientation changed
    }

    private fun initializeBuffer(
        pitch: Float,
        roll: Float,
    ) {
        // Fill the entire buffer with the initial sensor reading so the average is immediately valid
        pitchBuffer.fill(pitch)
        rollBuffer.fill(roll)
        pitchSum = pitch * windowSize
        rollSum = roll * windowSize
        isBufferInitialized = true
    }

    private fun toOrientationEvent(orientation: Int): OrientationEvent? =
        // Map the orientation constant to an event, emitting only on state changes
        when (orientation) {
            PORTRAIT -> {
                if (eventOccurred != 1) {
                    eventOccurred = 1
                    OrientationEvent.TopSideUp
                } else {
                    null
                }
            }
            LANDSCAPE -> {
                if (eventOccurred != 2) {
                    eventOccurred = 2
                    OrientationEvent.RightSideUp
                } else {
                    null
                }
            }
            PORTRAIT_REVERSE -> {
                if (eventOccurred != 3) {
                    eventOccurred = 3
                    OrientationEvent.BottomSideUp
                } else {
                    null
                }
            }
            LANDSCAPE_REVERSE -> {
                if (eventOccurred != 4) {
                    eventOccurred = 4
                    OrientationEvent.LeftSideUp
                } else {
                    null
                }
            }
            else -> null
        }

    private fun classifyOrientation(
        avgPitch: Float,
        avgRoll: Float,
        previous: Int,
    ): Int =
        // Determine the device orientation from averaged pitch and roll with hysteresis
        if ((previous == PORTRAIT || previous == PORTRAIT_REVERSE) && avgRoll in -30f..30f) {
            if (avgPitch > 0) PORTRAIT_REVERSE else PORTRAIT
        } else if (kotlin.math.abs(avgPitch) >= 30) {
            if (avgPitch > 0) PORTRAIT_REVERSE else PORTRAIT
        } else if (avgRoll > 0) {
            LANDSCAPE_REVERSE
        } else {
            LANDSCAPE
        }

    private companion object {
        const val PORTRAIT = 1
        const val LANDSCAPE = 2
        const val PORTRAIT_REVERSE = 3
        const val LANDSCAPE_REVERSE = 4
    }
}
