package com.raywenderlich.android.librarian.model

data class BookItem(
  val bookId: String,
  val name: String,
  var isSelected: Boolean = false
)