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
package com.github.nisrulz.sensey.gesture.light

import com.github.nisrulz.sensey.contract.GestureTrigger

class LightTrigger(
    private val threshold: Float = 3f,
) : GestureTrigger<LightEvent> {

    override fun evaluate(values: FloatArray, timestamp: Long): LightEvent? {
        val lux = values[0]
        return if (lux < threshold) LightEvent.Dark else LightEvent.Light
    }
}
