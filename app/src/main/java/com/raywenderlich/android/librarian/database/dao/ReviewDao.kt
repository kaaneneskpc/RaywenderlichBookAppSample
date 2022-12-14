package com.raywenderlich.android.librarian.database.dao

import androidx.room.*
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookReview
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

  @Query("SELECT * FROM reviews")
  suspend fun getReviews(): List<BookReview>

  @Transaction
  @Query("SELECT * FROM reviews")
  fun getReviewsFlow(): Flow<List<BookReview>>

  @Query("SELECT * FROM reviews WHERE rating >= :rating")
  suspend fun getReviewsByRating(rating: Int): List<BookReview>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addReview(review: Review)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun updateReview(review: Review)

  @Delete
  suspend fun removeReview(review: Review)

  @Delete
  suspend fun removeReviews(review: List<Review>)

  @Query("SELECT * FROM reviews WHERE bookId = :bookId")
  suspend fun getReviewsForBook(bookId: String): List<Review>

  @Query("SELECT * FROM reviews WHERE id = :reviewId")
  suspend fun getReviewsById(reviewId: String): BookReview
}