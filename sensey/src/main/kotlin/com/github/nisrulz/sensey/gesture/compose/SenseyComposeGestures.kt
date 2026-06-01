
package com.github.nisrulz.sensey.gesture.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.github.nisrulz.sensey.SenseyPluginRegistry
import com.github.nisrulz.sensey.defaultSensey
import com.github.nisrulz.sensey.senseyRegister
import com.github.nisrulz.sensey.senseyStop

fun interface ComposeGestureProvider {
    suspend fun PointerInputScope.installGestures()
}

fun Modifier.senseyGestures(): Modifier {
    val sensey = defaultSensey ?: return this
    var combined = this
    for (provider in sensey.composeGestureProviders) {
        combined =
            combined.then(
                Modifier.pointerInput(Unit) {
                    with(provider) { installGestures() }
                },
            )
    }
    return combined
}

@Composable
fun SenseyGestureEffect(
    lifecycle: Lifecycle,
    context: Context = LocalContext.current,
    sensorDataLoggingEnabled: Boolean = false,
    block: SenseyPluginRegistry.() -> Unit,
) {
    DisposableEffect(lifecycle) {
        context.senseyRegister(
            lifecycle = lifecycle,
            sensorDataLoggingEnabled = sensorDataLoggingEnabled,
            block = block,
        )
        onDispose { senseyStop() }
    }
}
