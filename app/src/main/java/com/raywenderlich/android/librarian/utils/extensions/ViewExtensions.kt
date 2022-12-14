package com.raywenderlich.android.librarian.utils

import android.graphics.RectF
import android.view.View
import kotlin.math.min

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.convertDpToPixel(dp: Float): Int {
    return (dp * this.resources.displayMetrics.density).toInt()
}

fun View.getWidthPixels(): Int {
    return this.resources.displayMetrics.widthPixels
}

fun View.getBoundRectF(strokeWidth: Float): RectF {

    val availableWidth = width - paddingLeft - paddingRight
    val availableHeight = height - paddingTop - paddingBottom

    val sideLength = min(availableWidth, availableHeight)

    val left = (paddingLeft + (availableWidth - sideLength))
    val top = (paddingTop + (availableHeight - sideLength))

    return RectF(
        left + strokeWidth,
        top + strokeWidth,
        left + sideLength - strokeWidth,
        top + sideLength - strokeWidth
    )
}

fun View.fadeOutAnimation(
    duration: Long = 300L,
    visibility: Int = View.INVISIBLE,
    completionCallback: (() -> Unit)? = null
) {
    animate().alpha(0F)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completionCallback?.invoke()
        }
}

fun View.fadeInAnimation(
    duration: Long = 300L,
    completionCallback: (() -> Unit)? = null
) {
    alpha = 0F
    visibility = View.VISIBLE
    animate().alpha(1F)
        .setDuration(duration)
        .withEndAction { completionCallback?.invoke() }
}