
package com.github.nisrulz.sensey

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.headshake.HeadShakeEvent
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.nodgesture.NodGestureEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.rotationangle.RotationAngleEvent
import com.github.nisrulz.sensey.gesture.scoop.ScoopEvent
import com.github.nisrulz.sensey.gesture.shake.ShakeEvent
import com.github.nisrulz.sensey.gesture.soundlevel.SoundLevelEvent
import com.github.nisrulz.sensey.gesture.step.StepEvent
import com.github.nisrulz.sensey.gesture.taponback.TapOnBackEvent
import com.github.nisrulz.sensey.gesture.tiltdirection.TiltDirectionEvent
import com.github.nisrulz.sensey.gesture.touch.TouchConfig
import com.github.nisrulz.sensey.gesture.touch.TouchEvent
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.CornerType
import com.github.nisrulz.sensey.gesture.touch.TouchEvent.EdgeType
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
        timeBeforeDeclaringStationary: Long = 1500L,
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

    fun nodGesturePlugin(
        angleThreshold: Float = 30f,
        timeWindowMs: Long = 500L,
        cooldownMs: Long = 1500L,
        dispatcher: (NodGestureEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .nodGesturePlugin(angleThreshold, timeWindowMs, cooldownMs, dispatcher),
        )
    }

    fun headShakePlugin(
        angleThreshold: Float = 30f,
        timeWindowMs: Long = 500L,
        cooldownMs: Long = 1500L,
        dispatcher: (HeadShakeEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .headShakePlugin(angleThreshold, timeWindowMs, cooldownMs, dispatcher),
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
        timeWindowMillis: Long = 1000L,
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

    fun pickupDevicePlugin(
        settleTimeMs: Long = 1000L,
        dispatcher: (PickupDeviceEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .pickupDevicePlugin(settleTimeMs, dispatcher),
        )
    }

    fun tapOnBackPlugin(
        accelThreshold: Float = 1.5f,
        minJerk: Float = 2.0f,
        preSettleMs: Long = 200L,
        settleWindowMs: Long = 100L,
        reboundGuardMs: Long = 180L,
        tapIntervalMs: Long = 400L,
        cooldownMs: Long = 1000L,
        dispatcher: (TapOnBackEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture.tapOnBackPlugin(
                accelThreshold = accelThreshold,
                minJerk = minJerk,
                preSettleMs = preSettleMs,
                settleWindowMs = settleWindowMs,
                reboundGuardMs = reboundGuardMs,
                tapIntervalMs = tapIntervalMs,
                cooldownMs = cooldownMs,
                dispatcher = dispatcher,
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

    fun touchPlugin(
        context: Context,
        config: TouchConfig = TouchConfig(),
        dispatcher: (TouchEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .touchPlugin(context, config, dispatcher),
        )
    }

    fun edgeSwipePlugin(
        context: Context,
        edgeThresholdDp: Dp = 48.dp,
        enabledEdges: Set<EdgeType> = EdgeType.entries.toSet(),
        dispatcher: (TouchEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .edgeSwipePlugin(context, edgeThresholdDp, enabledEdges, dispatcher),
        )
    }

    fun diagonalSwipePlugin(
        context: Context,
        minDragDistance: Float = 80f,
        dispatcher: (TouchEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .diagonalSwipePlugin(context, minDragDistance, dispatcher),
        )
    }

    fun longPressDragPlugin(
        context: Context,
        minDragDistance: Float = 20f,
        dispatcher: (TouchEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .longPressDragPlugin(context, minDragDistance, dispatcher),
        )
    }

    fun twoFingerSwipePlugin(
        context: Context,
        minDragDistance: Float = 80f,
        dispatcher: (TouchEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .twoFingerSwipePlugin(context, minDragDistance, dispatcher),
        )
    }

    fun cornerSwipePlugin(
        context: Context,
        cornerRadiusDp: Dp = 48.dp,
        enabledCorners: Set<CornerType> = CornerType.entries.toSet(),
        dispatcher: (TouchEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .cornerSwipePlugin(context, cornerRadiusDp, enabledCorners, dispatcher),
        )
    }

    fun pinchScalePlugin(
        context: Context,
        dispatcher: (TouchEvent) -> Unit,
    ) {
        plugins.add(
            com.github.nisrulz.sensey.gesture
                .pinchScalePlugin(context, dispatcher),
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
