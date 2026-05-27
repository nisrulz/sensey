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
package com.github.nisrulz.sensey.gesture.shake

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ShakeTriggerTest {

    private val trigger = ShakeTrigger(
        threshold = 3f,
        timeBeforeDeclaringShakeStopped = 1000L,
    )

    @Test
    fun noEventWhenValuesAreStable() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 10L)
        assertNull(result)
    }

    @Test
    fun noEventWithZeroGravityForCustomThreshold() {
        val trigger = ShakeTrigger(threshold = 10f)
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result = trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 10L)
        assertNull(result)
    }

    @Test
    fun shakeDetectedWhenAccelerationExceedsThreshold() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result = trigger.evaluate(
            floatArrayOf(0f, 0f, 2 * 9.81f),
            timestamp = 10L,
        )
        assertEquals(ShakeEvent.Detected, result)
    }

    @Test
    fun shakeDetectedWithDoubleGravityForCustomThreshold() {
        val trigger = ShakeTrigger(threshold = 9f)
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result = trigger.evaluate(
            floatArrayOf(0f, 0f, 2 * 9.81f),
            timestamp = 10L,
        )
        assertEquals(ShakeEvent.Detected, result)
    }

    @Test
    fun shakeDetectedMultipleTimesWithStrongAcceleration() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        val result1 = trigger.evaluate(
            floatArrayOf(0f, 0f, 2 * 9.81f),
            timestamp = 10L,
        )
        assertEquals(ShakeEvent.Detected, result1)

        val result2 = trigger.evaluate(
            floatArrayOf(0f, 0f, 2 * -9.81f),
            timestamp = 50L,
        )
        assertEquals(ShakeEvent.Detected, result2)
    }

    @Test
    fun shakeStoppedAfterTimeoutWithoutAcceleration() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 2 * 9.81f), timestamp = 10L)

        val result = trigger.evaluate(
            floatArrayOf(0f, 0f, 0f),
            timestamp = 2000L,
        )
        assertEquals(ShakeEvent.Stopped, result)
    }

    @Test
    fun noStopEventBeforeTimeout() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 2 * 9.81f), timestamp = 10L)

        val result = trigger.evaluate(
            floatArrayOf(0f, 0f, 0f),
            timestamp = 500L,
        )
        assertNull(result)
    }

    @Test
    fun shakeDetectedThenStoppedThenDetectedAgain() {
        trigger.evaluate(floatArrayOf(0f, 0f, 9.81f), timestamp = 0L)
        trigger.evaluate(floatArrayOf(0f, 0f, 2 * 9.81f), timestamp = 10L)
        trigger.evaluate(floatArrayOf(0f, 0f, 0f), timestamp = 2000L)

        val result = trigger.evaluate(
            floatArrayOf(0f, 0f, 2 * -9.81f),
            timestamp = 3000L,
        )
        assertNotNull(result)
    }
}
