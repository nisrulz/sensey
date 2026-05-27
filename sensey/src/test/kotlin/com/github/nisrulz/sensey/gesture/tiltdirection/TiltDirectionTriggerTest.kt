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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TiltDirectionTriggerTest {
    private val trigger = TiltDirectionTrigger()

    @Test
    fun xAxisClockwise() {
        assertEquals(
            TiltDirectionEvent.AxisXTilt(TiltDirectionEvent.Direction.CLOCKWISE),
            trigger.evaluate(floatArrayOf(-1f, 0f, 0f), 0L),
        )
    }

    @Test
    fun xAxisAnticlockwise() {
        assertEquals(
            TiltDirectionEvent.AxisXTilt(TiltDirectionEvent.Direction.ANTICLOCKWISE),
            trigger.evaluate(floatArrayOf(1f, 0f, 0f), 0L),
        )
    }

    @Test
    fun yAxisAnticlockwise() {
        assertEquals(
            TiltDirectionEvent.AxisYTilt(TiltDirectionEvent.Direction.ANTICLOCKWISE),
            trigger.evaluate(floatArrayOf(0f, 1f, 0f), 0L),
        )
    }

    @Test
    fun zAxisClockwise() {
        assertEquals(
            TiltDirectionEvent.AxisZTilt(TiltDirectionEvent.Direction.CLOCKWISE),
            trigger.evaluate(floatArrayOf(0f, 0f, -1f), 0L),
        )
    }

    @Test
    fun noEventBelowThreshold() {
        assertNull(trigger.evaluate(floatArrayOf(0.1f, 0.1f, 0.1f), 0L))
    }

    @Test
    fun dominantAxisReturnedWhenMultipleExceedThreshold() {
        val result = trigger.evaluate(floatArrayOf(0.6f, 2f, 0f), 0L)
        assertEquals(TiltDirectionEvent.AxisYTilt(TiltDirectionEvent.Direction.ANTICLOCKWISE), result)
    }

    @Test
    fun axisWithHighestMagnitudeChosen() {
        val result = trigger.evaluate(floatArrayOf(2f, 1f, 0f), 0L)
        assertEquals(TiltDirectionEvent.AxisXTilt(TiltDirectionEvent.Direction.ANTICLOCKWISE), result)
    }
}
