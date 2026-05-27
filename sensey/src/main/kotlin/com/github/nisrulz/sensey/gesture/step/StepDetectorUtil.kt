/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nisrulz.sensey.gesture.step

object StepDetectorUtil {

    const val ACTIVITY_STILL = 0
    const val ACTIVITY_WALKING = 1
    const val ACTIVITY_RUNNING = 2

    const val MALE = 0
    const val FEMALE = 1

    private const val MALE_STEP_DISTANCE_CM = 78
    private const val FEMALE_STEP_DISTANCE_CM = 70

    fun getDistanceCovered(steps: Int, gender: Int): Float {
        val averageStepDistance = if (gender == MALE) MALE_STEP_DISTANCE_CM else FEMALE_STEP_DISTANCE_CM
        return (steps * averageStepDistance) / 100f
    }

    fun getStepActivityType(distance: Float, timeDelta: Long): Int {
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
