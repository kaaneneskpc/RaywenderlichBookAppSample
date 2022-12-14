package com.raywenderlich.android.librarian.model.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Review
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookReview(
  @Embedded
  val review: Review,
  @Relation(
    parentColumn = "bookId",
    entityColumn = "id"
  )
  val book: Book
) : Parcelable