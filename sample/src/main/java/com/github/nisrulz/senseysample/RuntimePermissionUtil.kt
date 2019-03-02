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

package com.github.nisrulz.senseysample

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

internal object RuntimePermissionUtil {

    fun checkPermissonGranted(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun onRequestPermissionsResult(grantResults: IntArray,
                                   rpResultListener: RPResultListener) {
        if (grantResults.isNotEmpty()) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    rpResultListener.onPermissionGranted()
                } else {
                    rpResultListener.onPermissionDenied()
                }
            }
        }
    }

    fun requestPermission(activity: Activity, permission: String,
                          REQUEST_CODE: Int) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CODE)
    }

    fun requestPermission(activity: Activity, permissions: Array<String>,
                          REQUEST_CODE: Int) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
    }
}
