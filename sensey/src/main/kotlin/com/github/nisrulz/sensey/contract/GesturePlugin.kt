
package com.github.nisrulz.sensey.contract

import com.github.nisrulz.sensey.Sensey

interface GesturePlugin {
    val key: String

    fun onRegister(sensey: Sensey)

    fun onUnregister(sensey: Sensey)
}
