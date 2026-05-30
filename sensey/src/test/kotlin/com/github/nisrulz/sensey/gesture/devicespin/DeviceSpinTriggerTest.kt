
package com.github.nisrulz.sensey.gesture.devicespin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DeviceSpinTriggerTest {
    @Test
    fun noEventBelowThreshold() {
        val trigger = DeviceSpinTrigger(angleThreshold = 270f, timeWindowMs = 5000L)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L) // establish baseline
        // 5 rad/s * 0.2s = ~57 degrees per axis — well below 270°
        assertNull(trigger.evaluate(floatArrayOf(5f, 0f, 0f), 200L))
    }

    @Test
    fun spunWhenAnyAxisExceedsThreshold() {
        val trigger = DeviceSpinTrigger(angleThreshold = 200f, timeWindowMs = 5000L)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L) // establish baseline
        // 20 rad/s * 0.2s = ~229 degrees on X axis — exceeds 200°
        assertEquals(DeviceSpinEvent.Spun, trigger.evaluate(floatArrayOf(20f, 0f, 0f), 200L))
    }

    @Test
    fun noEventWhenTimeWindowExpires() {
        val trigger = DeviceSpinTrigger(angleThreshold = 500f, timeWindowMs = 100L)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L) // establish baseline
        trigger.evaluate(floatArrayOf(1f, 0f, 0f), 50L)
        assertNull(trigger.evaluate(floatArrayOf(1f, 0f, 0f), 200L)) // window reset
    }

    @Test
    fun spunOnYAxis() {
        val trigger = DeviceSpinTrigger(angleThreshold = 200f, timeWindowMs = 5000L)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L) // establish baseline
        // 20 rad/s * 0.2s = ~229 degrees on Y axis — exceeds 200°
        assertEquals(DeviceSpinEvent.Spun, trigger.evaluate(floatArrayOf(0f, 20f, 0f), 200L))
    }
}
