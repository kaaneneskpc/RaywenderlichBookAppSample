package com.raywenderlich.android.librarian.utils

inline fun <reified T> List<*>?.isAll(): Boolean? {
    return this?.all { it is T }
}