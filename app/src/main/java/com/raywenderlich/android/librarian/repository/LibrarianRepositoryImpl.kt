package com.raywenderlich.android.librarian.repository

import com.raywenderlich.android.librarian.database.dao.BookDao
import com.raywenderlich.android.librarian.database.dao.GenreDao
import com.raywenderlich.android.librarian.database.dao.ReadingListDao
import com.raywenderlich.android.librarian.database.dao.ReviewDao
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LibrarianRepositoryImpl @Inject constructor(
  private val bookDao: BookDao,
  private val genreDao: GenreDao,
  private val reviewDao: ReviewDao,
  private val readingListDao: ReadingListDao
) : LibrarianRepository {

  override suspend fun addBook(book: Book) = bookDao.addBook(book)

  override suspend fun removeBook(book: Book) = bookDao.removeBook(book)

  override suspend fun getBooks(): List<BookAndGenre> = bookDao.getBooks()

  override fun getBooksFlow(): Flow<List<BookAndGenre>> = bookDao.getBooksFlow()

  override suspend fun getBookById(bookId: String): BookAndGenre = bookDao.getBookById(bookId)

  override suspend fun getBooksByGenre(genreId: String): List<BookAndGenre> =
    genreDao.getBooksByGenre(genreId).let { booksByGenre ->
      val books = booksByGenre.books ?: return emptyList()

      return books.map { BookAndGenre(it, booksByGenre.genre) }
    }

  override suspend fun getGenres(): List<Genre> = genreDao.getGenres()

  override suspend fun getGenreById(genreId: String): Genre = genreDao.getGenreById(genreId)

  override suspend fun addGenres(genres: List<Genre>) = genreDao.addGenres(genres)

  override suspend fun getReviews(): List<BookReview> = reviewDao.getReviews()

  override fun getReviewsFlow(): Flow<List<BookReview>> = reviewDao.getReviewsFlow()

  override suspend fun getBooksByRating(rating: Int): List<BookAndGenre> {
    val reviewsByRating = reviewDao.getReviewsByRating(rating)

    return reviewsByRating.map { BookAndGenre(it.book, genreDao.getGenreById(it.book.genreId)) }
  }

  override suspend fun getReviewById(reviewId: String): BookReview =
    reviewDao.getReviewsById(reviewId)

  override suspend fun addReview(review: Review) = reviewDao.addReview(review)

  override suspend fun removeReview(review: Review) = reviewDao.removeReview(review)

  override suspend fun removeReviews(reviews: List<Review>) = reviewDao.removeReviews(reviews)

  override suspend fun updateReview(review: Review) = reviewDao.updateReview(review)

  override suspend fun getReadingLists(): List<ReadingListsWithBooks> =
    readingListDao.getReadingLists().map {
      ReadingListsWithBooks(it.id, it.name, it.bookIds.map { bookId -> getBookById(bookId) })
    }

  override fun getReadingListsFlow(): Flow<List<ReadingListsWithBooks>> =
    readingListDao.getReadingListsFlow()
      .map { items ->
        items.map { readingList ->
          ReadingListsWithBooks(readingList.id, readingList.name,
            readingList.bookIds.map { bookId ->
              getBookById(bookId)
            })
        }
      }

  override suspend fun removeReadingList(readingList: ReadingList) =
    readingListDao.removeReadingList(readingList)

  override suspend fun updateReadingList(readingList: ReadingList) =
    readingListDao.updateReadingList(readingList)

  override suspend fun removeReadingLists(readingLists: List<ReadingList>) =
    readingListDao.removeReadingLists(readingLists)

  override suspend fun addReadingList(readingList: ReadingList) =
    readingListDao.addReadingList(readingList)

  override suspend fun getReadingListById(id: String): ReadingListsWithBooks {
    val list = readingListDao.getReadingListById(id)

    return ReadingListsWithBooks(
      list.id,
      list.name,
      list.bookIds.map { bookId -> getBookById(bookId) })
  }

  override fun getReadingListByIdFlow(id: String): Flow<ReadingListsWithBooks> {
    val readingList = readingListDao.getReadingListByIdFlow(id)

    return readingList.map {
      ReadingListsWithBooks(it.id, it.name, it.bookIds.map { bookId -> getBookById(bookId) })
    }
  }

  override suspend fun getReviewsForBook(
    bookId: String
  ): List<Review> = reviewDao.getReviewsForBook(bookId)
}