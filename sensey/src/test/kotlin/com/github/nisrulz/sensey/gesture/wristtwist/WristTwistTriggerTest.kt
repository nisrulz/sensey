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
package com.github.nisrulz.sensey.gesture.wristtwist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WristTwistTriggerTest {
    private val trigger = WristTwistTrigger(threshold = 15f, timeForWristTwistGesture = 1000L)

    @Test
    fun gestureStartedButNotCompletedWithThresholdValues() {
        assertNull(trigger.evaluate(floatArrayOf(-30f, 0f, 0f), 0L))
    }

    @Test
    fun twistCompletedAfterTimeout() {
        trigger.evaluate(floatArrayOf(-30f, 0f, 0f), 0L)
        assertEquals(WristTwistEvent.Twisted, trigger.evaluate(floatArrayOf(0f, 0f, 0f), 2000L))
    }

    @Test
    fun noTwistBeforeTimeout() {
        trigger.evaluate(floatArrayOf(-30f, 0f, 0f), 0L)
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 500L))
    }

    @Test
    fun noEventWithStableValues() {
        assertNull(trigger.evaluate(floatArrayOf(0f, 0f, 0f), 0L))
    }

    @Test
    fun noEventWhenMagnitudeBelowThreshold() {
        assertNull(trigger.evaluate(floatArrayOf(-10f, -2f, -20f), 0L))
    }
}
