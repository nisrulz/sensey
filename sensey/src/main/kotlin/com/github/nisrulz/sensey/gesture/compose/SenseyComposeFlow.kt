
package com.github.nisrulz.sensey.gesture.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.flow.SenseyFlowScope
import com.github.nisrulz.sensey.flow.senseyFlow

@Composable
fun SenseyFlowEffect(
    lifecycle: Lifecycle,
    context: Context = LocalContext.current,
    samplingPeriod: Int = Sensey.SAMPLING_PERIOD_NORMAL,
    sensorDataLoggingEnabled: Boolean = false,
    block: SenseyFlowScope.() -> Unit,
) {
    DisposableEffect(lifecycle, context) {
        val scope =
            context.senseyFlow(
                lifecycle = lifecycle,
                samplingPeriod = samplingPeriod,
                sensorDataLoggingEnabled = sensorDataLoggingEnabled,
                block = block,
            )
        onDispose { scope.stop() }
    }
}
