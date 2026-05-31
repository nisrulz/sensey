
package com.github.nisrulz.sensey.gesture.movement

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MovementTriggerTest {
    private val trigger = MovementTrigger(threshold = 0.3f, timeBeforeDeclaringStationary = 5000L)

    @Test
    fun noEventWithStableGravityValues() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L))
    }

    @Test
    fun movementDetectedWhenDeltaExceedsThreshold() {
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertTrue(result is MovementEvent.Moved)
    }

    @Test
    fun stationaryAfterTimeout() {
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertEquals(MovementEvent.Stationary, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 6000L))
    }

    @Test
    fun noStationaryBeforeTimeout() {
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 3000L))
    }

    @Test
    fun movingAgainAfterStationary() {
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 6000L)
        assertEquals(
            MovementEvent.Moved(MovementEvent.Direction.Z_POS),
            trigger.evaluate(floatArrayOf(0f, 0f, 10f), 7000L),
        )
    }
}
