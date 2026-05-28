
package com.github.nisrulz.sensey.gesture.proximity

import org.junit.Assert.assertEquals
import org.junit.Test

class ProximityTriggerTest {
    private val trigger = ProximityTrigger()

    @Test
    fun nearWhenDistanceLessThanMaxRange() {
        assertEquals(ProximityEvent.Near, trigger.evaluate(floatArrayOf(1f, 10f), 0L))
    }

    @Test
    fun farWhenDistanceEqualsMaxRange() {
        assertEquals(ProximityEvent.Far, trigger.evaluate(floatArrayOf(10f, 10f), 0L))
    }

    @Test
    fun farWhenDistanceGreaterThanMaxRange() {
        assertEquals(ProximityEvent.Far, trigger.evaluate(floatArrayOf(11f, 10f), 0L))
    }

    @Test
    fun nearWhenDistanceIsZero() {
        assertEquals(ProximityEvent.Near, trigger.evaluate(floatArrayOf(0f, 10f), 0L))
    }
}
