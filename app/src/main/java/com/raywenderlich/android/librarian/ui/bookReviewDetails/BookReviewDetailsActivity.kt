package com.raywenderlich.android.librarian.ui.bookReviewDetails

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.ReadingEntry
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_AND_GENRE
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_REVIEW
import com.raywenderlich.android.librarian.utils.EMPTY_GENRE
import com.raywenderlich.android.librarian.utils.formatDateToText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BookReviewDetailsActivity : AppCompatActivity() {

  @Inject
  lateinit var repository: LibrarianRepository
  private val _bookReviewDetailsState = mutableStateOf(EMPTY_BOOK_REVIEW)
  private val _genreState = mutableStateOf(EMPTY_GENRE)

  companion object {
    private const val KEY_BOOK_REVIEW = "book_review"

    fun getIntent(context: Context, review: BookReview): Intent {
      val intent = Intent(context, BookReviewDetailsActivity::class.java)

      intent.putExtra(KEY_BOOK_REVIEW, review)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val data = if (Build.VERSION.SDK_INT >= 33) {
      intent?.getParcelableExtra(KEY_BOOK_REVIEW, BookReview::class.java)
    } else {
      intent?.getParcelableExtra(KEY_BOOK_REVIEW)
    }

    if (data == null) {
      finish()
      return
    }

    setReview(data)
    setContent { BookReviewDetailsContent() }
  }

  @Composable
  fun BookReviewDetailsContent() {
    Scaffold(topBar = { BookReviewDetailsTopBar() },
      floatingActionButton = { AddReadingEntry() }) {
      BookReviewDetailsInformation()
    }
  }

  @Composable
  fun BookReviewDetailsTopBar() {
    val reviewState = _bookReviewDetailsState.value
    val bookName =
      reviewState.book.name

    TopBar(title = bookName, onBackPressed = { onBackPressedDispatcher.onBackPressed() })
  }

  @Composable
  fun AddReadingEntry() {
    FloatingActionButton(onClick = { }) {
      Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reading Entry")
    }
  }

  @Composable
  fun BookReviewDetailsInformation() {
    val bookReview = _bookReviewDetailsState.value
    val genre = _genreState.value

    Column(modifier = Modifier
      .fillMaxSize()
      .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
      horizontalAlignment = Alignment.CenterHorizontally) {

      Spacer(modifier = Modifier.height(16.dp))

      Card(modifier = Modifier.size(200.dp, 300.dp), shape = RoundedCornerShape(16.dp), elevation = 16.dp) {
        AsyncImage(model = bookReview.review.imageUrl, contentScale = ContentScale.FillWidth, contentDescription = null)
      }

      Spacer(modifier = Modifier.height(16.dp))
      Text(text = bookReview.book.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
      
      Spacer(modifier = Modifier.height(6.dp))
      Text(text = genre.name, fontSize = 12.sp)

      Spacer(modifier = Modifier.height(6.dp))
      RatingBar(modifier = Modifier.align(CenterHorizontally),
        range =1..5,
        isSelectable = false,
        isLargeRating = false,
        currentRating = bookReview.review.rating,
       onRatingChanged = {})

      Spacer(modifier = Modifier.height(6.dp))
      Text(text = stringResource(R.string.last_updated_date, formatDateToText(bookReview.review.lastUpdatedDate)), fontSize = 12.sp)

      Spacer(modifier = Modifier.height(8.dp))
      Spacer(modifier = Modifier.fillMaxWidth(0.9f).height(1.dp).background(brush = SolidColor(Color.LightGray), shape = RectangleShape))

      Text(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp),
        text = bookReview.review.notes,
        fontSize = 12.sp,
        fontStyle = FontStyle.Italic)

      Spacer(modifier = Modifier.fillMaxWidth(0.9f).height(1.dp).background(brush = SolidColor(Color.LightGray), shape = RectangleShape))



    }
  }

  fun setReview(bookReview: BookReview) {
    _bookReviewDetailsState.value = bookReview

    lifecycleScope.launch {
      _genreState.value = repository.getGenreById(bookReview.book.genreId)
    }
  }

  fun addNewEntry(entry: String) {
    val data = _bookReviewDetailsState.value.review ?: return

    val updatedReview = data.copy(
      entries = data.entries + ReadingEntry(comment = entry),
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
  }

  fun removeReadingEntry(readingEntry: ReadingEntry) {
    val data = _bookReviewDetailsState.value.review ?: return

    val updatedReview = data.copy(
      entries = data.entries - readingEntry,
      lastUpdatedDate = Date()
    )

    updateReview(updatedReview)
  }

  private fun updateReview(updatedReview: Review) {
    lifecycleScope.launch {
      repository.updateReview(updatedReview)

      setReview(repository.getReviewById(updatedReview.id))
    }
  }
}