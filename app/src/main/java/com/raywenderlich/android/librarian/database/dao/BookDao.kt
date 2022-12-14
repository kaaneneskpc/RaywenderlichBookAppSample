package com.raywenderlich.android.librarian.database.dao

import androidx.room.*
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

  @Query("SELECT * FROM books")
  suspend fun getBooks(): List<BookAndGenre>

  @Query("SELECT * FROM books")
  fun getBooksFlow(): Flow<List<BookAndGenre>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addBook(book: Book)

  @Delete
  suspend fun removeBook(book: Book)

  @Query("SELECT * FROM books WHERE id = :bookId")
  suspend fun getBookById(bookId: String): BookAndGenre
}