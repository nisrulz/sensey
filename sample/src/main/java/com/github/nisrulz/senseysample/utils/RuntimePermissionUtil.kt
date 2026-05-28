

package com.github.nisrulz.senseysample.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

internal object RuntimePermissionUtil {
    fun checkPermissonGranted(
        context: Context,
        permission: String,
    ): Boolean = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun onRequestPermissionsResult(
        grantResults: IntArray,
        rpResultListener: RPResultListener,
    ) {
        if (grantResults.isNotEmpty()) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                rpResultListener.onPermissionGranted()
            } else {
                rpResultListener.onPermissionDenied()
            }
        }
    }

    fun requestPermission(
        activity: Activity,
        permission: String,
        REQUEST_CODE: Int,
    ) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CODE)
    }

    fun requestPermission(
        activity: Activity,
        permissions: Array<String>,
        REQUEST_CODE: Int,
    ) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
    }
}
