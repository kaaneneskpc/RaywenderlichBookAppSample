package com.raywenderlich.android.librarian.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre

class BooksByGenre(
  @Embedded
  val genre: Genre,
  @Relation(
    parentColumn = "id",
    entityColumn = "bookGenreId"
  )
  val books: List<Book>?
)