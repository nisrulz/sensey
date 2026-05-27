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
package com.github.nisrulz.sensey.gesture.pickupdevice

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PickupDeviceTriggerTest {

    @Test
    fun pickedUpWhenVectorSumExceedsThreshold() {
        val trigger = PickupDeviceTrigger()
        assertEquals(PickupDeviceEvent.PickedUp, trigger.evaluate(floatArrayOf(12f, 0f, 0f), 0L))
    }

    @Test
    fun notPickedUpWhenVectorSumBelowThreshold() {
        val trigger = PickupDeviceTrigger()
        assertNull(trigger.evaluate(floatArrayOf(1f, 1f, 1f), 0L))
    }

    @Test
    fun notPickedUpWithZeroValues() {
        val trigger = PickupDeviceTrigger()
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun putDownAfterBeingPickedUp() {
        val trigger = PickupDeviceTrigger()
        trigger.evaluate(floatArrayOf(12f, 0f, 0f), 0L)
        assertEquals(PickupDeviceEvent.PutDown, trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 100L))
    }

    @Test
    fun noPutDownWithoutPriorPickup() {
        val trigger = PickupDeviceTrigger()
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), 0L))
    }
}
