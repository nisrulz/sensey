package com.github.nisrulz.senseysample.navigation

import androidx.navigation3.runtime.NavKey
import com.github.nisrulz.senseysample.SenseySensorManager
import kotlinx.serialization.Serializable

@Serializable
sealed interface GestureGroup : NavKey

@Serializable data object TouchGroup : GestureGroup

@Serializable data object MotionGroup : GestureGroup

@Serializable data object OrientationGroup : GestureGroup

@Serializable data object EnvironmentGroup : GestureGroup

data class GroupInfo(
    val label: String,
    val emoji: String,
)

private val TOUCH_SENSORS =
    listOf(
        SenseySensorManager.TOUCH_DETECTION,
        SenseySensorManager.PINCH_SCALE,
        SenseySensorManager.EDGE_SWIPE,
        SenseySensorManager.DIAGONAL_SWIPE,
        SenseySensorManager.LONG_PRESS_DRAG,
        SenseySensorManager.TWO_FINGER_SWIPE,
        SenseySensorManager.CORNER_SWIPE,
    )

private val MOTION_SENSORS =
    listOf(
        SenseySensorManager.SHAKE,
        SenseySensorManager.FLIP,
        SenseySensorManager.MOVEMENT,
        SenseySensorManager.CHOP,
        SenseySensorManager.WRIST_TWIST,
        SenseySensorManager.PICKUP_DEVICE,
        SenseySensorManager.SCOOP,
        SenseySensorManager.TAP_ON_BACK,
    )

private val ORIENTATION_SENSORS =
    listOf(
        SenseySensorManager.ORIENTATION,
        SenseySensorManager.ROTATION_ANGLE,
        SenseySensorManager.TILT_DIRECTION,
        SenseySensorManager.TURN_OVER,
        SenseySensorManager.DEVICE_SPIN,
        SenseySensorManager.NOD_GESTURE,
        SenseySensorManager.HEAD_SHAKE,
        SenseySensorManager.RAISE_TO_EAR,
    )

private val ENVIRONMENT_SENSORS =
    listOf(
        SenseySensorManager.PROXIMITY,
        SenseySensorManager.WAVE,
        SenseySensorManager.LIGHT,
        SenseySensorManager.SOUND_LEVEL,
        SenseySensorManager.CLAP,
        SenseySensorManager.STEP,
    )

val gestureGroupInfo: Map<GestureGroup, GroupInfo> =
    mapOf(
        TouchGroup to GroupInfo("Touch", "🖐️"),
        MotionGroup to GroupInfo("Motion", "📳"),
        OrientationGroup to GroupInfo("Orientation", "🧭"),
        EnvironmentGroup to GroupInfo("Environment", "🌡️"),
    )

val groupSensors: Map<GestureGroup, List<String>> =
    mapOf(
        TouchGroup to TOUCH_SENSORS,
        MotionGroup to MOTION_SENSORS,
        OrientationGroup to ORIENTATION_SENSORS,
        EnvironmentGroup to ENVIRONMENT_SENSORS,
    )

val allGroups: List<GestureGroup> = listOf(TouchGroup, MotionGroup, OrientationGroup, EnvironmentGroup)

val defaultGroup: GestureGroup = TouchGroup
