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

import android.hardware.Sensor
import android.hardware.SensorEvent
import com.github.nisrulz.sensey.TypedSensorDetector
import com.github.nisrulz.sensey.contract.GestureTrigger

class ProximityDetector(
    trigger: GestureTrigger<ProximityEvent>,
    dispatcher: (ProximityEvent) -> Unit,
) : TypedSensorDetector<ProximityEvent>(trigger, dispatcher, Sensor.TYPE_PROXIMITY) {

    override fun getValues(sensorEvent: SensorEvent): FloatArray =
        floatArrayOf(sensorEvent.values[0], sensorEvent.sensor.maximumRange)
}
