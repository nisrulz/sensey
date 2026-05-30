
package com.github.nisrulz.senseysample.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

internal object PermissionRequestUtil {
    fun register(
        activity: ComponentActivity,
        onResult: (Boolean) -> Unit,
    ): ActivityResultLauncher<String> =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            onResult(granted)
        }

    fun isGranted(
        context: Context,
        permission: String,
    ): Boolean = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun requestIfNeeded(
        launcher: ActivityResultLauncher<String>,
        permission: String,
        context: Context,
    ) {
        if (!isGranted(context, permission)) {
            launcher.launch(permission)
        }
    }
}

internal fun ComponentActivity.registerAudioPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit = {},
): ActivityResultLauncher<String> =
    PermissionRequestUtil.register(this) { granted ->
        if (granted) onGranted() else onDenied()
    }

internal fun Context.isAudioPermissionGranted(): Boolean =
    PermissionRequestUtil.isGranted(this, Manifest.permission.RECORD_AUDIO)

internal fun ActivityResultLauncher<String>.requestAudioIfNeeded(context: Context) {
    PermissionRequestUtil.requestIfNeeded(this, Manifest.permission.RECORD_AUDIO, context)
}
