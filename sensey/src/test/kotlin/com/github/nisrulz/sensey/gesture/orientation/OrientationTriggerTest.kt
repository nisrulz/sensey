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
