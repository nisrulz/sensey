
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
