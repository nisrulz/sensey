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

class TapOnBackTrigger(
    private val angleThreshold: Float = 1.5f,
    private val minAngleJerk: Float = 1.5f,
    private val tapDebounceMs: Long = 250L,
    private val tapSequenceTimeoutMs: Long = 500L,
) : GestureTrigger<TapOnBackEvent> {

    private var axBaseline = 0f
    private var ayBaseline = 0f
    private var azBaseline = 9.8f
    private var hasBaseline = false
    private var prevAngleDeg = 0f
    private var lastTapTime = 0L
    private var tapCount = 0

    override fun evaluate(values: FloatArray, timestamp: Long): TapOnBackEvent? {
        val (ax, ay, az) = values

        val accelMag = sqrt(ax * ax + ay * ay + az * az)

        if (hasBaseline) {
            axBaseline = axBaseline * 0.95f + ax * 0.05f
            ayBaseline = ayBaseline * 0.95f + ay * 0.05f
            azBaseline = azBaseline * 0.95f + az * 0.05f
        } else {
            axBaseline = ax
            ayBaseline = ay
            azBaseline = az
            hasBaseline = true
        }

        val baseMag = sqrt(axBaseline * axBaseline + ayBaseline * ayBaseline + azBaseline * azBaseline)
        val dot = ax * axBaseline + ay * ayBaseline + az * azBaseline
        val cosAngle = (dot / (accelMag * baseMag)).coerceIn(-1f, 1f)
        val angleDeg = Math.toDegrees(acos(cosAngle.toDouble())).toFloat()

        val angleJerk = abs(angleDeg - prevAngleDeg)
        prevAngleDeg = angleDeg

        if (angleDeg > angleThreshold && timestamp - lastTapTime > tapDebounceMs && angleJerk > minAngleJerk) {
            tapCount++
            lastTapTime = timestamp
            return null
        }

        if (tapCount > 0 && timestamp - lastTapTime > tapSequenceTimeoutMs) {
            val event = if (tapCount >= 2) TapOnBackEvent else null
            tapCount = 0
            return event
        }

        return null
    }
}
