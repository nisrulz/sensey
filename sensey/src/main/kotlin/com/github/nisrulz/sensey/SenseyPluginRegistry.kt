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
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.pinchscale.PinchScaleEvent
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleEvent
import com.github.nisrulz.sensey.gesture.scoop.ScoopEvent
import com.github.nisrulz.sensey.gesture.shake.ShakeEvent
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelEvent
import com.github.nisrulz.sensey.gesture.step.StepEvent
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.sensey.gesture.touchtype.TouchTypeEvent
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent

class SenseyPluginRegistry {
    private val plugins = mutableListOf<GesturePlugin>()

    fun shakePlugin(
        threshold: Float = 3f,
        timeBeforeDeclaringShakeStopped: Long = 1000L,
        dispatcher: (ShakeEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .shakePlugin(threshold, timeBeforeDeclaringShakeStopped, dispatcher),
        )
    }

    fun flipPlugin(
        faceUpLowerBound: Float = 8f,
        faceUpUpperBound: Float = 10.5f,
        faceDownLowerBound: Float = -10.5f,
        faceDownUpperBound: Float = -8f,
        dispatcher: (FlipEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .flipPlugin(faceUpLowerBound, faceUpUpperBound, faceDownLowerBound, faceDownUpperBound, dispatcher),
        )
    }

    fun lightPlugin(
        darkThreshold: Float = 8f,
        dispatcher: (LightEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .lightPlugin(darkThreshold, dispatcher),
        )
    }

    fun proximityPlugin(
        debounceMillis: Long = 200L,
        dispatcher: (ProximityEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .proximityPlugin(debounceMillis, dispatcher),
        )
    }

    fun movementPlugin(
        threshold: Float = 0.3f,
        timeBeforeDeclaringStationary: Long = 5000L,
        dispatcher: (MovementEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .movementPlugin(threshold, timeBeforeDeclaringStationary, dispatcher),
        )
    }

    fun orientationPlugin(
        smoothness: Int = 1,
        dispatcher: (OrientationEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .orientationPlugin(smoothness, dispatcher),
        )
    }

    fun chopPlugin(
        threshold: Float = 25f,
        timeForChopGesture: Long = 700L,
        dispatcher: (ChopEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .chopPlugin(threshold, timeForChopGesture, dispatcher),
        )
    }

    fun wristTwistPlugin(
        threshold: Float = 12f,
        timeForWristTwistGesture: Long = 1000L,
        dispatcher: (WristTwistEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .wristTwistPlugin(threshold, timeForWristTwistGesture, dispatcher),
        )
    }

    fun wavePlugin(
        timeWindowMillis: Float = 1000f,
        debounceMillis: Long = 1000L,
        dispatcher: (WaveEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .wavePlugin(timeWindowMillis, debounceMillis, dispatcher),
        )
    }

    fun scoopPlugin(
        threshold: Float = 10f,
        dispatcher: (ScoopEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .scoopPlugin(threshold, dispatcher),
        )
    }

    fun pickupDevicePlugin(dispatcher: (PickupDeviceEvent) -> Unit) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .pickupDevicePlugin(dispatcher),
        )
    }

    fun tapOnBackPlugin(
        angleThreshold: Float = 1.5f,
        minAngleJerk: Float = 1.5f,
        tapDebounceMs: Long = 250L,
        tapSequenceTimeoutMs: Long = 500L,
        dispatcher: (TapOnBackEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture.tapOnBackPlugin(
                angleThreshold,
                minAngleJerk,
                tapDebounceMs,
                tapSequenceTimeoutMs,
                dispatcher,
            ),
        )
    }

    fun tiltDirectionPlugin(
        threshold: Float = 0.5f,
        dispatcher: (TiltDirectionEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .tiltDirectionPlugin(threshold, dispatcher),
        )
    }

    fun rotationAnglePlugin(
        minAngleChange: Float = 1f,
        dispatcher: (RotationAngleEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .rotationAnglePlugin(minAngleChange, dispatcher),
        )
    }

    fun stepPlugin(
        gender: Int = 0,
        threshold: Float = 3f,
        dispatcher: (StepEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .stepPlugin(gender, threshold, dispatcher),
        )
    }

    fun pinchScalePlugin(
        context: Context,
        dispatcher: (PinchScaleEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .pinchScalePlugin(context, dispatcher),
        )
    }

    fun touchTypePlugin(
        context: Context,
        dispatcher: (TouchTypeEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .touchTypePlugin(context, dispatcher),
        )
    }

    fun soundLevelPlugin(
        context: Context,
        dispatcher: (SoundLevelEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .soundLevelPlugin(context, dispatcher),
        )
    }

    internal fun collect(): List<GesturePlugin> = plugins.toList()
}
