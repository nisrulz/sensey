
package com.github.nisrulz.sensey.gesture.proximity

import android.hardware.Sensor
import android.hardware.SensorEvent
import com.github.nisrulz.sensey.TypedSensorDetector
import com.github.nisrulz.sensey.contract.GestureTrigger

internal class ProximityDetector(
    trigger: GestureTrigger<ProximityEvent>,
    dispatcher: (ProximityEvent) -> Unit,
) : TypedSensorDetector<ProximityEvent>(trigger, dispatcher, Sensor.TYPE_PROXIMITY) {
    override fun getValues(sensorEvent: SensorEvent): FloatArray =
        floatArrayOf(sensorEvent.values[0], sensorEvent.sensor.maximumRange)
}
