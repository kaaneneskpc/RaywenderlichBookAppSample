package com.raywenderlich.android.librarian.ui.addReview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Review
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.model.state.AddBookReviewState
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.addReview.ui.BookPicker
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton
import com.raywenderlich.android.librarian.ui.composeUi.InputField
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_AND_GENRE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddBookReviewActivity : AppCompatActivity(), AddReviewView {

  @Inject
  lateinit var repository: LibrarianRepository

  private val _bookReviewState = MutableLiveData(AddBookReviewState())
  private val _books = mutableStateOf(emptyList<BookAndGenre>())

  companion object {
    fun getIntent(context: Context) = Intent(context, AddBookReviewActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { AddBookReviewContent() }
    loadBooks()
  }

  private fun loadBooks() {
    lifecycleScope.launch {
      _books.value = repository.getBooks()
    }
  }

  @Composable
  fun AddBookReviewContent() {
    Scaffold(topBar = { AddBookReviewTopBar() }) {
      AddBookReviewForm()
    }
  }

  @Composable
  fun AddBookReviewTopBar() {
    TopBar(
      onBackPressed = { onBackPressedDispatcher.onBackPressed() },
      title = stringResource(id = R.string.add_review_title)
    )
  }

  @Composable
  fun AddBookReviewForm() {
    val bookUrl = remember { mutableStateOf("") }
    val bookNotes = remember { mutableStateOf("") }
    val currentRatingFilter = remember { mutableStateOf(0) }
    val currentlySelectedBook = remember { mutableStateOf(EMPTY_BOOK_AND_GENRE) }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(state = rememberScrollState()),
      horizontalAlignment = CenterHorizontally
    ) {
      Text(
        text = stringResource(id = R.string.book_picker_hint),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(8.dp))

      BookPicker(
        books = _books.value, selectedBookId = currentlySelectedBook.value.book.id,
        onItemPicked = { bookAndGenre ->
          _bookReviewState.value = _bookReviewState.value?.copy(bookAndGenre = bookAndGenre)

          currentlySelectedBook.value = bookAndGenre
        })

      Spacer(modifier = Modifier.height(8.dp))

      InputField(
        label = stringResource(id = R.string.book_image_url_input_hint),
        value = bookUrl.value,
        onStateChanged = { url ->
          _bookReviewState.value = _bookReviewState.value?.copy(bookImageUrl = url)
          bookUrl.value = url
        },
        isInputValid = bookUrl.value.isNotEmpty()
      )

      Spacer(modifier = Modifier.height(16.dp))

      RatingBar(
        modifier = Modifier.align(CenterHorizontally),
        range = 1..5,
        currentRating = currentRatingFilter.value,
        isLargeRating = true,
        onRatingChanged = { newRating ->
          currentRatingFilter.value = newRating

          _bookReviewState.value = _bookReviewState.value?.copy(rating = newRating)
        })

      Spacer(modifier = Modifier.height(16.dp))

      InputField(
        label = stringResource(id = R.string.review_notes_hint),
        value = bookNotes.value,
        onStateChanged = { notes ->
          _bookReviewState.value = _bookReviewState.value?.copy(notes = notes)
          bookNotes.value = notes
        },
        isInputValid = bookNotes.value.isNotEmpty()
      )

      Spacer(modifier = Modifier.height(16.dp))

      val pickedBook = _bookReviewState.value?.bookAndGenre

      ActionButton(
        modifier = Modifier.fillMaxWidth(0.7f),
        text = stringResource(id = R.string.add_book_review_text),
        onClick = ::addBookReview,
        isEnabled = bookNotes.value.isNotEmpty() && bookUrl.value.isNotEmpty() && pickedBook != null && pickedBook != EMPTY_BOOK_AND_GENRE
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }

  fun addBookReview() {
    val state = _bookReviewState.value ?: return

    lifecycleScope.launch {
      val bookId = state.bookAndGenre.book.id
      val imageUrl = state.bookImageUrl
      val notes = state.notes
      val rating = state.rating

      if (bookId.isNotEmpty() && imageUrl.isNotBlank() && notes.isNotBlank()) {
        val bookReview = Review(
          bookId = bookId,
          rating = rating,
          notes = notes,
          imageUrl = imageUrl,
          lastUpdatedDate = Date(),
          entries = emptyList()
        )
        repository.addReview(bookReview)

        onReviewAdded()
      }
    }
  }

  override fun onReviewAdded() {
    setResult(RESULT_OK)
    finish()
  }
}