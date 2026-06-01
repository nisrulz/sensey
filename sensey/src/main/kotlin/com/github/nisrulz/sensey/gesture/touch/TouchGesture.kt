
package com.github.nisrulz.sensey.gesture.touch

sealed interface TouchGesture {
    interface SwipeScroll : TouchGesture {
        fun evaluate(
            deltaX: Float,
            deltaY: Float,
            velocityX: Float,
            velocityY: Float,
        ): TouchEvent?
    }

    interface Drag : TouchGesture {
        fun evaluate(
            deltaX: Float,
            deltaY: Float,
        ): TouchEvent?
    }

    interface Scale : TouchGesture {
        fun evaluate(scaleFactor: Float): TouchEvent?
    }

    interface TwoFinger : TouchGesture {
        fun evaluate(
            panX: Float,
            panY: Float,
        ): TouchEvent?
    }

    interface ZoneSwipe : TouchGesture {
        fun evaluate(
            startX: Float,
            startY: Float,
            endX: Float,
            endY: Float,
        ): TouchEvent?
    }
}
