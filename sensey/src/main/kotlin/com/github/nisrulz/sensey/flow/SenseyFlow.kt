
package com.github.nisrulz.sensey.flow

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.contract.GesturePlugin
import com.github.nisrulz.sensey.gesture.audio.clap.ClapEvent
import com.github.nisrulz.sensey.gesture.chop.ChopEvent
import com.github.nisrulz.sensey.gesture.devicespin.DeviceSpinEvent
import com.github.nisrulz.sensey.gesture.flip.FlipEvent
import com.github.nisrulz.sensey.gesture.headshake.HeadShakeEvent
import com.github.nisrulz.sensey.gesture.light.LightEvent
import com.github.nisrulz.sensey.gesture.movement.MovementEvent
import com.github.nisrulz.sensey.gesture.nodgesture.NodGestureEvent
import com.github.nisrulz.sensey.gesture.orientation.OrientationEvent
import com.github.nisrulz.sensey.gesture.pickupdevice.PickupDeviceEvent
import com.github.nisrulz.sensey.gesture.proximity.ProximityEvent
import com.github.nisrulz.sensey.gesture.raisetoear.RaiseToEarEvent
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
import com.github.nisrulz.sensey.gesture.turnover.TurnOverEvent
import com.github.nisrulz.sensey.gesture.wave.WaveEvent
import com.github.nisrulz.sensey.gesture.wristtwist.WristTwistEvent
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class SenseyFlowScope internal constructor(
    private val context: Context,
    private val lifecycle: Lifecycle,
    samplingPeriod: Int = Sensey.SAMPLING_PERIOD_NORMAL,
    sensorDataLoggingEnabled: Boolean = false,
) {
    private val sensey = Sensey(context, lifecycle, samplingPeriod, sensorDataLoggingEnabled)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + CoroutineName("SenseyFlow"))
    private val flowEntries = mutableListOf<FlowEntry<*>>()
    private var collectJob: Job? = null

    private class FlowEntry<T>(
        val flow: Flow<T>,
        val dispatcher: suspend (T) -> Unit,
    )

    private val lifecycleObserver =
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> startCollection()
                Lifecycle.Event.ON_STOP -> stopCollection()
                Lifecycle.Event.ON_DESTROY -> destroy()
                else -> {}
            }
        }

    init {
        lifecycle.addObserver(lifecycleObserver)
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            startCollection()
        }
    }

    private fun startCollection() {
        if (collectJob?.isActive == true) return
        collectJob =
            scope.launch {
                coroutineScope {
                    flowEntries.map { entry ->
                        launch {
                            entry.flow.collect { value ->
                                @Suppress("UNCHECKED_CAST")
                                (entry.dispatcher as suspend (Any?) -> Unit)(value)
                            }
                        }
                    }
                }
            }
    }

    private fun stopCollection() {
        collectJob?.cancel()
        collectJob = null
    }

    fun stop() {
        destroy()
    }

    private fun destroy() {
        stopCollection()
        lifecycle.removeObserver(lifecycleObserver)
        scope.cancel()
        sensey.stop()
    }

    private fun <T> registerFlow(
        createPlugin: ((T) -> Unit) -> GesturePlugin,
        dispatcher: (T) -> Unit,
    ) {
        val flow =
            callbackFlow<T> {
                val plugin = createPlugin { trySend(it) }
                sensey.register(plugin)
                awaitClose { sensey.unregister(plugin) }
            }
        flowEntries.add(FlowEntry(flow) { dispatcher(it) })
    }

    fun shakePlugin(
        threshold: Float = 3f,
        timeBeforeDeclaringShakeStopped: Long = 1000L,
        dispatcher: (ShakeEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .shakePlugin(threshold, timeBeforeDeclaringShakeStopped, send)
        }, dispatcher)
    }

    fun flipPlugin(
        faceUpLowerBound: Float = 8f,
        faceUpUpperBound: Float = 10.5f,
        faceDownLowerBound: Float = -10.5f,
        faceDownUpperBound: Float = -8f,
        dispatcher: (FlipEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture.flipPlugin(
                faceUpLowerBound,
                faceUpUpperBound,
                faceDownLowerBound,
                faceDownUpperBound,
                send,
            )
        }, dispatcher)
    }

    fun lightPlugin(
        darkThreshold: Float = 8f,
        dispatcher: (LightEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .lightPlugin(darkThreshold, send)
        }, dispatcher)
    }

    fun proximityPlugin(
        debounceMillis: Long = 200L,
        dispatcher: (ProximityEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .proximityPlugin(debounceMillis, send)
        }, dispatcher)
    }

    fun movementPlugin(
        threshold: Float = 0.3f,
        timeBeforeDeclaringStationary: Long = 1500L,
        dispatcher: (MovementEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .movementPlugin(threshold, timeBeforeDeclaringStationary, send)
        }, dispatcher)
    }

    fun orientationPlugin(
        smoothness: Int = 1,
        dispatcher: (OrientationEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .orientationPlugin(smoothness, send)
        }, dispatcher)
    }

    fun chopPlugin(
        threshold: Float = 25f,
        timeForChopGesture: Long = 700L,
        dispatcher: (ChopEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .chopPlugin(threshold, timeForChopGesture, send)
        }, dispatcher)
    }

    fun nodGesturePlugin(
        angleThreshold: Float = 30f,
        timeWindowMs: Long = 500L,
        cooldownMs: Long = 1500L,
        dispatcher: (NodGestureEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .nodGesturePlugin(angleThreshold, timeWindowMs, cooldownMs, send)
        }, dispatcher)
    }

    fun headShakePlugin(
        angleThreshold: Float = 30f,
        timeWindowMs: Long = 500L,
        cooldownMs: Long = 1500L,
        dispatcher: (HeadShakeEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .headShakePlugin(angleThreshold, timeWindowMs, cooldownMs, send)
        }, dispatcher)
    }

    fun wristTwistPlugin(
        threshold: Float = 12f,
        timeForWristTwistGesture: Long = 1000L,
        dispatcher: (WristTwistEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .wristTwistPlugin(threshold, timeForWristTwistGesture, send)
        }, dispatcher)
    }

    fun turnOverPlugin(
        angleThreshold: Float = 150f,
        dispatcher: (TurnOverEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .turnOverPlugin(angleThreshold, send)
        }, dispatcher)
    }

    fun deviceSpinPlugin(
        angleThreshold: Float = 270f,
        timeWindowMs: Long = 2000L,
        dispatcher: (DeviceSpinEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .deviceSpinPlugin(angleThreshold, timeWindowMs, send)
        }, dispatcher)
    }

    fun raiseToEarPlugin(
        maxProximityCm: Float = 5f,
        minGzRatio: Float = 0.3f,
        debounceMs: Long = 500L,
        dispatcher: (RaiseToEarEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .raiseToEarPlugin(maxProximityCm, minGzRatio, debounceMs, send)
        }, dispatcher)
    }

    fun clapPlugin(
        thresholdDb: Float = -35f,
        requiredClaps: Int = 2,
        clapTimeframeMs: Long = 800L,
        dispatchEvents: (ClapEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .clapPlugin(
                    context,
                    thresholdDb = thresholdDb,
                    requiredClaps = requiredClaps,
                    clapTimeframeMs = clapTimeframeMs,
                    dispatchEvents = send,
                )
        }, dispatchEvents)
    }

    fun wavePlugin(
        timeWindowMillis: Long = 1000L,
        debounceMillis: Long = 1000L,
        dispatcher: (WaveEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .wavePlugin(timeWindowMillis, debounceMillis, send)
        }, dispatcher)
    }

    fun scoopPlugin(
        threshold: Float = 10f,
        dispatcher: (ScoopEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .scoopPlugin(threshold, send)
        }, dispatcher)
    }

    fun pickupDevicePlugin(
        settleTimeMs: Long = 1000L,
        dispatcher: (PickupDeviceEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .pickupDevicePlugin(settleTimeMs, send)
        }, dispatcher)
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
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture.tapOnBackPlugin(
                accelThreshold = accelThreshold,
                minJerk = minJerk,
                preSettleMs = preSettleMs,
                settleWindowMs = settleWindowMs,
                reboundGuardMs = reboundGuardMs,
                tapIntervalMs = tapIntervalMs,
                cooldownMs = cooldownMs,
                dispatcher = send,
            )
        }, dispatcher)
    }

    fun tiltDirectionPlugin(
        threshold: Float = 0.5f,
        dispatcher: (TiltDirectionEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .tiltDirectionPlugin(threshold, send)
        }, dispatcher)
    }

    fun rotationAnglePlugin(
        minAngleChange: Float = 1f,
        dispatcher: (RotationAngleEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .rotationAnglePlugin(minAngleChange, send)
        }, dispatcher)
    }

    fun stepPlugin(
        gender: Int = 0,
        threshold: Float = 3f,
        dispatcher: (StepEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .stepPlugin(gender, threshold, send)
        }, dispatcher)
    }

    fun touchPlugin(
        config: TouchConfig = TouchConfig(),
        dispatcher: (TouchEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .touchPlugin(context, config, send)
        }, dispatcher)
    }

    fun edgeSwipePlugin(
        edgeThresholdDp: Dp = 48.dp,
        enabledEdges: Set<EdgeType> = EdgeType.entries.toSet(),
        dispatcher: (TouchEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .edgeSwipePlugin(context, edgeThresholdDp, enabledEdges, send)
        }, dispatcher)
    }

    fun diagonalSwipePlugin(
        minDragDistance: Float = 80f,
        dispatcher: (TouchEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .diagonalSwipePlugin(context, minDragDistance, send)
        }, dispatcher)
    }

    fun longPressDragPlugin(
        minDragDistance: Float = 20f,
        dispatcher: (TouchEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .longPressDragPlugin(context, minDragDistance, send)
        }, dispatcher)
    }

    fun twoFingerSwipePlugin(
        minDragDistance: Float = 80f,
        dispatcher: (TouchEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .twoFingerSwipePlugin(context, minDragDistance, send)
        }, dispatcher)
    }

    fun cornerSwipePlugin(
        cornerRadiusDp: Dp = 48.dp,
        enabledCorners: Set<CornerType> = CornerType.entries.toSet(),
        dispatcher: (TouchEvent) -> Unit,
    ) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .cornerSwipePlugin(context, cornerRadiusDp, enabledCorners, send)
        }, dispatcher)
    }

    fun pinchScalePlugin(dispatcher: (TouchEvent) -> Unit) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .pinchScalePlugin(context, send)
        }, dispatcher)
    }

    fun soundLevelPlugin(dispatcher: (SoundLevelEvent) -> Unit) {
        registerFlow({ send ->
            com.github.nisrulz.sensey.gesture
                .soundLevelPlugin(context, send)
        }, dispatcher)
    }
}

fun Context.senseyFlow(
    lifecycle: Lifecycle,
    samplingPeriod: Int = Sensey.SAMPLING_PERIOD_NORMAL,
    sensorDataLoggingEnabled: Boolean = false,
    block: SenseyFlowScope.() -> Unit,
): SenseyFlowScope {
    val scope = SenseyFlowScope(this, lifecycle, samplingPeriod, sensorDataLoggingEnabled)
    scope.block()
    return scope
}
