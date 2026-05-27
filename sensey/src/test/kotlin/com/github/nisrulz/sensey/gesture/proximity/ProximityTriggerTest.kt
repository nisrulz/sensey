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
