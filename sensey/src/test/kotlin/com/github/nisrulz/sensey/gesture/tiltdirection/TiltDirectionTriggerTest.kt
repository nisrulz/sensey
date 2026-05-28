
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
