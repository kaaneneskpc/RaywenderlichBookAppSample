package com.raywenderlich.android.librarian.utils

fun Boolean.toBinaryString(): String {
    return if(this) "1" else "0"
}

fun Boolean?.isNotNullAndTrue(): Boolean {
    return this != null && this == true
}

fun Boolean?.isNullOrFalse(): Boolean {
    return this == null || this == false
}

fun Boolean?.isNullOrTrue(): Boolean {
    return this == null || this == true
}

fun Boolean?.isNotNullAndFalse(): Boolean {
    return this != null && this == false
}

fun Boolean?.ifTrue(block: Boolean.() -> Unit): Boolean? {
    if(this == true){
        block()
    }
    return this
}

fun Boolean?.ifFalse(block: Boolean.() -> Unit): Boolean {
    if(this == false || this == null){
        this!!.block()
    }
    return this
}