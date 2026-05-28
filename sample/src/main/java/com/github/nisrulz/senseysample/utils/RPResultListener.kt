

package com.github.nisrulz.senseysample.utils

internal interface RPResultListener {
    fun onPermissionDenied()

    fun onPermissionGranted()
}
