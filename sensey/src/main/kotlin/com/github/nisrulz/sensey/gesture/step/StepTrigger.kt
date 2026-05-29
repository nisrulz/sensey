
package com.github.nisrulz.sensey.gesture.step

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.sqrt

internal class StepTrigger(
    private val gender: Int = StepDetectorUtil.MALE,
    private val threshold: Float = 3f,
) : GestureTrigger<StepEvent> {
    private var steps = 0
    private var previousMagnitude = 0f
    private var startTime = 0L
    private var baseStepCount = 0
    private var lastDispatchedSteps = -1

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): StepEvent? =
        when {
            values.size == 1 -> evaluateStepCounter(values[0], timestamp)
            values.size >= 3 -> evaluateAccelerometer(values, timestamp)
            else -> null
        }

    private fun evaluateStepCounter(
        sensorValue: Float,
        timestamp: Long,
    ): StepEvent? {
        if (baseStepCount < 1) {
            baseStepCount = sensorValue.toInt()
            return null
        }
        val currentSteps = sensorValue.toInt() - baseStepCount
        if (currentSteps == lastDispatchedSteps) return null
        lastDispatchedSteps = currentSteps
        steps = currentSteps
        return buildStepEvent(timestamp)
    }

    private fun evaluateAccelerometer(
        values: FloatArray,
        timestamp: Long,
    ): StepEvent? {
        val magnitude = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        val stepDetected = abs(magnitude - previousMagnitude) > threshold
        previousMagnitude = magnitude
        if (!stepDetected) return null
        steps++
        return buildStepEvent(timestamp)
    }

    private fun buildStepEvent(timestamp: Long): StepEvent {
        val distance = StepDetectorUtil.getDistanceCovered(steps, gender)
        val timeDelta = timestamp - startTime
        startTime = timestamp
        val activityType = StepDetectorUtil.getStepActivityType(distance, timeDelta)
        return StepEvent(steps, distance, activityType)
    }
}
