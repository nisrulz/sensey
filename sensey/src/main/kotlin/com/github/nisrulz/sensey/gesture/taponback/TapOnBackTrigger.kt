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
package com.github.nisrulz.sensey.gesture.taponback

import com.github.nisrulz.sensey.contract.GestureTrigger
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

internal class TapOnBackTrigger(
    private val angleThreshold: Float = 1.5f,
    private val minAngleJerk: Float = 1.5f,
    private val tapDebounceMs: Long = 250L,
    private val tapSequenceTimeoutMs: Long = 500L,
) : GestureTrigger<TapOnBackEvent> {
    private var baselineX = 0f
    private var baselineY = 0f
    private var baselineZ = GRAVITY_EARTH
    private var hasBaseline = false
    private var previousAngleDeg = 0f
    private var lastTapTime = 0L
    private var tapCount = 0

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): TapOnBackEvent? {
        val (ax, ay, az) = values
        val accelMag = sqrt(ax * ax + ay * ay + az * az)
        updateBaseline(ax, ay, az)

        val angleDeg = computeAngleFromBaseline(ax, ay, az, accelMag)
        val angleJerk = abs(angleDeg - previousAngleDeg)
        previousAngleDeg = angleDeg

        if (isValidTap(angleDeg, angleJerk, timestamp)) {
            tapCount++
            lastTapTime = timestamp
            return null
        }

        return if (tapCount > 0 && timestamp - lastTapTime > tapSequenceTimeoutMs) {
            val event = if (tapCount >= 2) TapOnBackEvent else null
            tapCount = 0
            event
        } else {
            null
        }
    }

    private fun updateBaseline(
        ax: Float,
        ay: Float,
        az: Float,
    ) {
        if (hasBaseline) {
            baselineX = baselineX * SMOOTHING_ALPHA + ax * (1f - SMOOTHING_ALPHA)
            baselineY = baselineY * SMOOTHING_ALPHA + ay * (1f - SMOOTHING_ALPHA)
            baselineZ = baselineZ * SMOOTHING_ALPHA + az * (1f - SMOOTHING_ALPHA)
        } else {
            baselineX = ax
            baselineY = ay
            baselineZ = az
            hasBaseline = true
        }
    }

    private fun computeAngleFromBaseline(
        ax: Float,
        ay: Float,
        az: Float,
        accelMag: Float,
    ): Float {
        val baseMag = sqrt(baselineX * baselineX + baselineY * baselineY + baselineZ * baselineZ)
        val dotProduct = ax * baselineX + ay * baselineY + az * baselineZ
        val cosAngle = (dotProduct / (accelMag * baseMag)).coerceIn(-1f, 1f)
        return Math.toDegrees(acos(cosAngle.toDouble())).toFloat()
    }

    private fun isValidTap(
        angleDeg: Float,
        angleJerk: Float,
        timestamp: Long,
    ): Boolean =
        angleDeg > angleThreshold &&
            timestamp - lastTapTime > tapDebounceMs &&
            angleJerk > minAngleJerk

    companion object {
        private const val GRAVITY_EARTH = 9.81f
        private const val SMOOTHING_ALPHA = 0.95f
    }
}
