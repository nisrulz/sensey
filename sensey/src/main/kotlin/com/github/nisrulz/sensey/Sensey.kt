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
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.compose.ComposeGestureProvider

class Sensey(
    context: Context,
    lifecycle: Lifecycle? = null,
    samplingPeriod: Int = SAMPLING_PERIOD_NORMAL,
    sensorDataLoggingEnabled: Boolean = false,
) {
    companion object {
        const val SAMPLING_PERIOD_FASTEST = SensorManager.SENSOR_DELAY_FASTEST
        const val SAMPLING_PERIOD_GAME = SensorManager.SENSOR_DELAY_GAME
        const val SAMPLING_PERIOD_NORMAL = SensorManager.SENSOR_DELAY_NORMAL
        const val SAMPLING_PERIOD_UI = SensorManager.SENSOR_DELAY_UI
        private const val LOGTAG = "===Sensey==="
    }

    private val plugins = mutableMapOf<String, GesturePlugin>()
    internal val composeGestureProviders = mutableListOf<ComposeGestureProvider>()
    private var sensorManager: SensorManager? = null
    private var samplingPeriodActual = samplingPeriod
    internal var sensorDataLoggingEnabled: Boolean = sensorDataLoggingEnabled
    private var lifecycleObserver: LifecycleEventObserver? = null
    private var registeredLifecycle: Lifecycle? = null

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lifecycle?.let(::registerLifecycleObserver)
    }

    fun register(plugin: GesturePlugin) {
        if (plugins.containsKey(plugin.key)) return
        plugins[plugin.key] = plugin
        plugin.onRegister(this)
    }

    fun register(builder: SenseyPluginRegistry.() -> Unit) {
        val registry = SenseyPluginRegistry()
        registry.builder()
        registry.collect().forEach(::register)
    }

    fun unregister(plugin: GesturePlugin) {
        plugins.remove(plugin.key)?.onUnregister(this)
    }

    fun unregisterAll() {
        composeGestureProviders.clear()
        plugins.values.toList().forEach { it.onUnregister(this) }
        plugins.clear()
    }

    fun stop() {
        unregisterAll()
        lifecycleObserver?.let { registeredLifecycle?.removeObserver(it) }
        lifecycleObserver = null
        registeredLifecycle = null
        sensorManager = null
    }

    fun checkHardware(
        context: Context,
        hardware: String,
    ): Boolean = context.packageManager.hasSystemFeature(hardware)

    fun checkPermission(
        context: Context,
        permission: String,
    ): Boolean = context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    internal fun registerSensorDetector(detector: SensorDetector) {
        detector.sensorDataLoggingEnabled = this.sensorDataLoggingEnabled
        val sensors = resolveSensors(*detector.sensorTypes)
        sensors.forEach { sensorManager?.registerListener(detector, it, samplingPeriodActual) }
    }

    internal fun unregisterSensorDetector(detector: SensorDetector) {
        sensorManager?.unregisterListener(detector)
    }

    internal fun registerComposeGestureProvider(provider: ComposeGestureProvider) {
        composeGestureProviders.add(provider)
    }

    internal fun unregisterComposeGestureProvider(provider: ComposeGestureProvider) {
        composeGestureProviders.remove(provider)
    }

    private fun registerLifecycleObserver(lifecycle: Lifecycle) {
        lifecycleObserver?.let { registeredLifecycle?.removeObserver(it) }
        lifecycleObserver =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) stop()
            }
        registeredLifecycle = lifecycle
        lifecycleObserver?.let { lifecycle.addObserver(it) }
    }

    private fun resolveSensors(vararg sensorTypes: Int): List<Sensor> {
        val manager = sensorManager ?: return emptyList()
        return sensorTypes.toList().mapNotNull { type ->
            manager.getDefaultSensor(type).also { sensor ->
                if (sensor == null) Log.w(LOGTAG, "Sensor type $type not available on this device")
            }
        }
    }
}
