
package com.github.nisrulz.sensey.gesture.pickupdevice

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PickupDeviceTriggerTest {
    @Test
    fun noEventOnStableFlatSurface() {
        val trigger = PickupDeviceTrigger(windowSize = 4, settleTimeMs = 3000L)
        repeat(10) {
            assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), it * 100L))
        }
    }

    @Test
    fun pickedUpWhenValuesBecomeUnstable() {
        val trigger = PickupDeviceTrigger(stableRange = 0.5f, movingRange = 1.5f, windowSize = 4)
        repeat(4) { trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), it * 100L) }
        val result = trigger.evaluate(floatArrayOf(5f, 0f, 5f), 1000L)
        assertEquals(PickupDeviceEvent.PickedUp, result)
    }

    @Test
    fun notPickedUpWithSmallFluctuation() {
        val trigger = PickupDeviceTrigger(movingRange = 3f, windowSize = 4)
        repeat(4) { trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), it * 100L) }
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 10.5f), 1000L))
    }

    @Test
    fun putDownAfterBeingHeldAndSettled() {
        val trigger =
            PickupDeviceTrigger(
                stableRange = 0.5f,
                movingRange = 1.5f,
                windowSize = 4,
                settleTimeMs = 500L,
            )

        var ts = 0L
        repeat(4) { trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), ts); ts += 100L }
        trigger.evaluate(floatArrayOf(5f, 0f, 5f), ts) // PickedUp
        ts += 100L
        repeat(4) { trigger.evaluate(floatArrayOf(5f, 0f, 5.1f), ts); ts += 100L } // held

        // Return to table: fill buffer with gravity values
        val putDownFired =
            (1..10).any {
                trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), ts).also { ts += 100L } == PickupDeviceEvent.PutDown
            }
        assert(putDownFired)
    }

    @Test
    fun noPutDownWithoutPriorPickup() {
        val trigger = PickupDeviceTrigger(stableRange = 0.5f, windowSize = 4, settleTimeMs = 500L)
        var ts = 0L
        repeat(20) {
            trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), ts)
            ts += 100L
        }
    }

    @Test
    fun putDownRequiresStabilization() {
        val trigger =
            PickupDeviceTrigger(
                stableRange = 0.5f,
                movingRange = 1.5f,
                windowSize = 4,
                settleTimeMs = 500L,
            )

        var ts = 0L
        repeat(4) { trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), ts); ts += 100L }
        trigger.evaluate(floatArrayOf(5f, 0f, 5f), ts) // PickedUp
        ts += 100L
        repeat(4) { trigger.evaluate(floatArrayOf(5f, 0f, 5.1f), ts); ts += 100L }

        // One stable reading early isn't enough
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), ts)
        ts += 100L
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), ts)
        ts += 100L
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), ts))
        ts += 100L

        // Wait for settleTimeMs to elapse
        val putDownFired =
            (1..10).any {
                trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), ts).also { ts += 100L } == PickupDeviceEvent.PutDown
            }
        assert(putDownFired)
    }
}
