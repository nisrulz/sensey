
package com.github.nisrulz.sensey.gesture.orientation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OrientationTriggerTest {
    @Test
    fun topSideUpWithDefaultOrientation() {
        val trigger = OrientationTrigger()
        assertEquals(OrientationEvent.TopSideUp, trigger.evaluate(floatArrayOf(-10f, 0f), 0L))
    }

    @Test
    fun rightSideUp() {
        val trigger = OrientationTrigger()
        trigger.evaluate(floatArrayOf(-10f, 0f), 0L)
        assertEquals(OrientationEvent.RightSideUp, trigger.evaluate(floatArrayOf(0f, -40f), 100L))
    }

    @Test
    fun bottomSideUp() {
        val trigger = OrientationTrigger()
        trigger.evaluate(floatArrayOf(-10f, 0f), 0L)
        assertEquals(OrientationEvent.BottomSideUp, trigger.evaluate(floatArrayOf(40f, 0f), 100L))
    }

    @Test
    fun leftSideUp() {
        val trigger = OrientationTrigger()
        trigger.evaluate(floatArrayOf(-10f, 0f), 0L)
        assertEquals(OrientationEvent.LeftSideUp, trigger.evaluate(floatArrayOf(0f, 40f), 100L))
    }

    @Test
    fun noRepeatedEventForSameOrientation() {
        val trigger = OrientationTrigger()
        assertEquals(OrientationEvent.TopSideUp, trigger.evaluate(floatArrayOf(-10f, 0f), 0L))
        assertNull(trigger.evaluate(floatArrayOf(-10f, 0f), 100L))
    }
}
