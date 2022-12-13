package com.raywenderlich.android.librarian.utils

fun Long?.toStringOrEmpty() : String {
    return this?.toString() ?: ""
}