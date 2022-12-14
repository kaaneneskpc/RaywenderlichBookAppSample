package com.raywenderlich.android.librarian.database.dao

import androidx.room.*
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.relations.BooksByGenre

@Dao
interface GenreDao {

  @Query("SELECT * FROM genre")
  suspend fun getGenres(): List<Genre>

  @Transaction
  @Query("SELECT * FROM genre WHERE id = :genreId")
  suspend fun getBooksByGenre(genreId: String): BooksByGenre

  @Transaction
  @Query("SELECT * FROM genre")
  suspend fun getBooksByGenres(): List<BooksByGenre>

  @Query("SELECT * FROM genre WHERE id = :genreId")
  suspend fun getGenreById(genreId: String): Genre

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addGenres(genres: List<Genre>)
}