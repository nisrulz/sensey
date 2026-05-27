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
package com.github.nisrulz.sensey.gesture.chop

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChopTriggerTest {
    private val trigger = ChopTrigger(threshold = 35f, timeForChopGesture = 700L)

    @Test
    fun gestureStartedButNotCompletedWithThresholdValues() {
        assertNull(trigger.evaluate(floatArrayOf(40f, -40f, 40f), 0L))
    }

    @Test
    fun choppingCompletedAfterTimeout() {
        trigger.evaluate(floatArrayOf(40f, -40f, 40f), 0L)
        assertEquals(ChopEvent.Chopped, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 1000L))
    }

    @Test
    fun noChopBeforeTimeout() {
        trigger.evaluate(floatArrayOf(40f, -40f, 40f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 500L))
    }

    @Test
    fun noEventWithStableValues() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun noEventWithPartialCondition() {
        assertNull(trigger.evaluate(floatArrayOf(40f, -5f, 40f), 0L))
    }
}
