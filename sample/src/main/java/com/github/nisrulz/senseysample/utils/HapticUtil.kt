package com.github.nisrulz.senseysample.utils

import android.app.Activity
import android.content.Context
import android.view.HapticFeedbackConstants

object HapticUtil {
    fun quickTap(context: Context) {
        val view = (context as? Activity)?.window?.decorView ?: return
        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    }
}
