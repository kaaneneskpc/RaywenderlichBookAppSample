package com.raywenderlich.android.librarian.utils

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, message, length).show()
}