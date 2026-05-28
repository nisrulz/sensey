
package com.github.nisrulz.sensey.gesture.step

object StepDetectorUtil {
    const val ACTIVITY_STILL = 0
    const val ACTIVITY_WALKING = 1
    const val ACTIVITY_RUNNING = 2

    const val MALE = 0
    const val FEMALE = 1

    private const val MALE_STEP_DISTANCE_CM = 78
    private const val FEMALE_STEP_DISTANCE_CM = 70

    fun getDistanceCovered(
        steps: Int,
        gender: Int,
    ): Float {
        val averageStepDistance = if (gender == MALE) MALE_STEP_DISTANCE_CM else FEMALE_STEP_DISTANCE_CM
        return (steps * averageStepDistance) / 100f
    }

    fun getStepActivityType(
        distance: Float,
        timeDelta: Long,
    ): Int {
        if (timeDelta <= 0) return ACTIVITY_STILL
        val speedMs = distance * 1000f / timeDelta
        return when {
            speedMs > ACTIVITY_RUNNING_THRESHOLD -> ACTIVITY_RUNNING
            speedMs > ACTIVITY_WALKING_THRESHOLD -> ACTIVITY_WALKING
            else -> ACTIVITY_STILL
        }
    }

    private const val ACTIVITY_WALKING_THRESHOLD = 0.2f
    private const val ACTIVITY_RUNNING_THRESHOLD = 2.0f
}
