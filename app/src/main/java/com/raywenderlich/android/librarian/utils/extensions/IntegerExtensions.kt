package com.raywenderlich.android.librarian.utils

import android.content.res.Resources

fun Int.isEven(): Boolean {
    return (this % 2) == 0
}

fun Int.setEmojiFromInteger(): String {
    return String(Character.toChars(this))
}

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int?.toStringOrEmpty() : String {
    return this?.toString() ?: ""
}

fun Int.half(): Int = this.div(2)