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

import com.github.nisrulz.sensey.contract.GestureTrigger

internal class ProximityTrigger(
    private val debounceMillis: Long = 200L,
) : GestureTrigger<ProximityEvent> {
    private var lastDispatchedState: ProximityEvent? = null
    private var lastStateChangeTime = 0L
    private var hasPendingState = false

    override fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): ProximityEvent? {
        val distance = values[0]
        val maxRange = values.getOrNull(1) ?: return null
        val currentState = if (distance < maxRange) ProximityEvent.Near else ProximityEvent.Far

        if (currentState == lastDispatchedState) {
            hasPendingState = false
            return null
        }

        if (!hasPendingState) {
            hasPendingState = true
            lastStateChangeTime = timestamp
            return null
        }

        if (timestamp - lastStateChangeTime >= debounceMillis) {
            lastDispatchedState = currentState
            hasPendingState = false
            return currentState
        }

        return null
    }
}
