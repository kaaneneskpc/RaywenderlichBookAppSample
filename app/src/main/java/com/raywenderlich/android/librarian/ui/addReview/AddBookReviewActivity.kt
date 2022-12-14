package com.raywenderlich.android.librarian.ui.addReview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.state.AddBookReviewState
import com.raywenderlich.android.librarian.ui.composeUi.*
import com.raywenderlich.android.librarian.utils.EMPTY_BOOK_AND_GENRE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBookReviewActivity : AppCompatActivity(), AddReviewView {

  private val addBookReviewViewModel by viewModels<AddReviewViewModel>()

  companion object {
    fun getIntent(context: Context) = Intent(context, AddBookReviewActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addBookReviewViewModel.setView(this)
    setContent {
      ProjectColorTheme() {
        AddBookReviewContent()
      }
    }
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  @Composable
  fun AddBookReviewContent() {
    Scaffold(topBar = { AddBookReviewTopBar() }) {
      AddBookReviewForm()
    }
  }

  @Composable
  fun AddBookReviewTopBar() {
    TopBar(
      onBackPressed = { onBackPressed() },
      title = stringResource(id = R.string.add_review_title)
    )
  }

  @Composable
  fun AddBookReviewForm() {
    val books by addBookReviewViewModel.booksState.observeAsState(emptyList())
    val reviewState by addBookReviewViewModel.bookReviewState.observeAsState(AddBookReviewState())

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
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onPrimary
      )

      Spacer(modifier = Modifier.height(8.dp))

      SpinnerPicker(
        pickerText = stringResource(id = R.string.book_select),
        items = books,
        itemToName = { it.book.name },
        preSelectedItem = reviewState.bookAndGenre,
        onItemPicked = { bookAndGenre -> addBookReviewViewModel.onBookPicked(bookAndGenre) })

      Spacer(modifier = Modifier.height(8.dp))

      InputField(
        label = stringResource(id = R.string.book_image_url_input_hint),
        value = reviewState.bookImageUrl,
        onStateChanged = { url -> addBookReviewViewModel.onImageUrlChanged(url) },
        isInputValid = reviewState.bookImageUrl.isNotEmpty()
      )

      Spacer(modifier = Modifier.height(16.dp))

      RatingBar(
        modifier = Modifier.align(CenterHorizontally),
        range = 1..5,
        currentRating = reviewState.rating,
        isSelectable = true,
        isLargeRating = true,
        onRatingChanged = { newRating -> addBookReviewViewModel.onRatingSelected(newRating) })

      Spacer(modifier = Modifier.height(16.dp))

      InputField(
        label = stringResource(id = R.string.review_notes_hint),
        value = reviewState.notes,
        onStateChanged = { notes -> addBookReviewViewModel.onNotesChanged(notes) },
        isInputValid = reviewState.notes.isNotEmpty()
      )

      Spacer(modifier = Modifier.height(16.dp))

      ActionButton(
        modifier = Modifier.fillMaxWidth(0.7f),
        text = stringResource(id = R.string.add_book_review_text),
        onClick = addBookReviewViewModel::addBookReview,
        isEnabled = reviewState.bookImageUrl.isNotEmpty() &&
                reviewState.notes.isNotEmpty() &&
                reviewState.bookAndGenre != EMPTY_BOOK_AND_GENRE
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }

  override fun onReviewAdded() {
    setResult(RESULT_OK)
    finish()
  }
}