
package com.github.nisrulz.sensey.gesture.rotationangle

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class RotationAngleTrigger(
    private val minAngleChange: Float = 1f,
) : GestureTrigger<RotationAngleEvent> {
    private var lastEvent: RotationAngleEvent? = null

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): RotationAngleEvent? {
        val event = RotationAngleEvent(values[0], values[1], values[2])
        val previous = lastEvent
        lastEvent = event

        return if (previous == null || hasSignificantChange(event, previous)) event else null
    }

    private fun hasSignificantChange(
        current: RotationAngleEvent,
        previous: RotationAngleEvent,
    ): Boolean =
        kotlin.math.abs(current.angleInAxisX - previous.angleInAxisX) > minAngleChange ||
            kotlin.math.abs(current.angleInAxisY - previous.angleInAxisY) > minAngleChange ||
            kotlin.math.abs(current.angleInAxisZ - previous.angleInAxisZ) > minAngleChange
}
