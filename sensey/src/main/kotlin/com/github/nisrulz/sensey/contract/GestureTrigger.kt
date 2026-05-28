
package com.github.nisrulz.sensey.contract

interface GestureTrigger<T> {
    fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): T?
}
