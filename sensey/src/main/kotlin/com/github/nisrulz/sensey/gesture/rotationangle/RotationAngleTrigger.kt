
package com.github.nisrulz.sensey.gesture.rotationangle

import com.github.nisrulz.sensey.contract.GestureTrigger

/**
 * Emits rotation-angle events when the device orientation changes
 * significantly on any axis.
 *
 * Algorithm: Wraps raw Euler-angle readings (axis X, Y, Z) into a
 * RotationAngleEvent. Compares each axis value against the previously
 * emitted event; if any axis has changed by more than the minimum angle
 * threshold a new event is emitted. The first reading is always emitted.
 * Expected sensor: Rotation vector sensor (TYPE_ROTATION_VECTOR).
 * State: lastEvent (previously emitted event for delta comparison).
 */
internal class RotationAngleTrigger(
    private val minAngleChange: Float = 1f,
) : GestureTrigger<RotationAngleEvent> {
    private var lastEvent: RotationAngleEvent? = null // Previously emitted rotation event (for delta comparison)

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): RotationAngleEvent? {
        val event = RotationAngleEvent(values[0], values[1], values[2]) // Create event from sensor Euler angles
        val previous = lastEvent
        lastEvent = event

        return if (previous == null || hasSignificantChange(event, previous)) event else null
        // Emit if first event or if any axis changed beyond the threshold
    }

    private fun hasSignificantChange(
        current: RotationAngleEvent,
        previous: RotationAngleEvent,
    ): Boolean =
        // True if any axis changed by more than minAngleChange
        kotlin.math.abs(current.angleInAxisX - previous.angleInAxisX) > minAngleChange ||
            kotlin.math.abs(current.angleInAxisY - previous.angleInAxisY) > minAngleChange ||
            kotlin.math.abs(current.angleInAxisZ - previous.angleInAxisZ) > minAngleChange
}
