
package com.github.nisrulz.sensey.contract

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface GestureTrigger<T> {
    fun evaluate(
        values: FloatArray,
        timestamp: Long,
    ): T?

    fun observe(): Flow<T> = emptyFlow()
}
