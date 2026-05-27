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
package com.github.nisrulz.sensey.gesture.tiltdirection

import com.github.nisrulz.sensey.contract.GestureTrigger

class TiltDirectionTrigger(
    private val threshold: Float = 0.5f,
) : GestureTrigger<TiltDirectionEvent> {

    override fun evaluate(values: FloatArray, timestamp: Long): TiltDirectionEvent? {
        val (x, y, z) = values
        val absX = kotlin.math.abs(x)
        val absY = kotlin.math.abs(y)
        val absZ = kotlin.math.abs(z)

        val maxAxis = maxOf(absX, absY, absZ)
        if (maxAxis < threshold) return null

        return when (maxAxis) {
            absX -> TiltDirectionEvent.AxisXTilt(
                if (x > 0) DIRECTION_ANTICLOCKWISE else DIRECTION_CLOCKWISE,
            )
            absY -> TiltDirectionEvent.AxisYTilt(
                if (y > 0) DIRECTION_ANTICLOCKWISE else DIRECTION_CLOCKWISE,
            )
            else -> TiltDirectionEvent.AxisZTilt(
                if (z > 0) DIRECTION_ANTICLOCKWISE else DIRECTION_CLOCKWISE,
            )
        }
    }

    companion object {
        const val DIRECTION_CLOCKWISE = 0
        const val DIRECTION_ANTICLOCKWISE = 1
    }
}
