package com.raywenderlich.android.librarian.model.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookAndGenre(
  @Embedded
  val book: Book,
  @Relation(parentColumn = "bookGenreId", entityColumn = "id")
  val genre: Genre
) : Parcelable