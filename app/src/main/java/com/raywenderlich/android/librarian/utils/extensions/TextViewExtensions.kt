package com.raywenderlich.android.librarian.utils

import android.widget.TextView

fun TextView.setTextWithAnimation(
    text: CharSequence,
    duration: Long = 300L,
    completionCallback: (() -> Unit)? = null
) {
    fadeOutAnimation(duration) {
        this.text = text
        fadeInAnimation(duration, completionCallback)
    }
}