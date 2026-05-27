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
package com.github.nisrulz.sensey

import android.content.Context
import androidx.lifecycle.Lifecycle

private var defaultInstance: Sensey? = null

internal var defaultSensey: Sensey?
    get() = defaultInstance
    set(value) {
        defaultInstance = value
    }

fun Context.senseyRegister(
    lifecycle: Lifecycle? = null,
    samplingPeriod: Int = Sensey.SAMPLING_PERIOD_NORMAL,
    sensorDataLoggingEnabled: Boolean = false,
    block: SenseyPluginRegistry.() -> Unit,
): Sensey {
    defaultInstance?.stop()
    val sensey = Sensey(this, lifecycle, samplingPeriod, sensorDataLoggingEnabled)
    defaultInstance = sensey
    sensey.register(block)
    return sensey
}

fun senseyStop() {
    defaultInstance?.stop()
    defaultInstance = null
}
