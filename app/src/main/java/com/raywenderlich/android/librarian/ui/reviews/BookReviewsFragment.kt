package com.raywenderlich.android.librarian.ui.reviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookReview
import com.raywenderlich.android.librarian.ui.bookReviewDetails.BookReviewDetailsActivity
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.ProjectColorTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.reviews.ui.BookReviewsList
import com.raywenderlich.android.librarian.utils.toast
import dagger.hilt.android.AndroidEntryPoint

private const val REQUEST_CODE_ADD_REVIEW = 202

@AndroidEntryPoint
class BookReviewsFragment : Fragment() {

  private val bookReviewsViewModel by viewModels<BookReviewsViewModel>()

  private val addReviewContract by lazy {
    registerForActivityResult(AddBookReviewContract()) { isReviewAdded ->
      if (isReviewAdded) {
        activity?.toast("Review added!")
      }
    }
  }

  @ExperimentalFoundationApi
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    addReviewContract

    return ComposeView(requireContext()).apply {
      setContent {
        ProjectColorTheme {
          BookReviewsContent()
        }
      }
    }
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  @ExperimentalFoundationApi
  @Composable
  fun BookReviewsContent() {
    Scaffold(
      topBar = { BookReviewsTopBar() },
      floatingActionButton = { AddBookReview() }
    ) {
      BookReviewsContentWrapper()
    }
  }

  @Composable
  fun BookReviewsTopBar() {
    TopBar(title = stringResource(id = R.string.book_reviews_title))
  }

  @Composable
  fun AddBookReview() {
    FloatingActionButton(onClick = { startAddBookReview() }) {
      Icon(imageVector = Icons.Default.Add, contentDescription = "Add Book Review")
    }
  }

  @ExperimentalFoundationApi
  @Composable
  fun BookReviewsContentWrapper() {
    val bookReviews by bookReviewsViewModel.bookReviewsState.observeAsState(emptyList())
    val deleteReviewState by bookReviewsViewModel.deleteReviewState.observeAsState()

    val reviewToDelete = deleteReviewState

    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center) {

      BookReviewsList(
        bookReviews,
        onItemClick = ::onItemSelected,
        onItemLongClick = { bookReview -> bookReviewsViewModel.onItemLongTapped(bookReview) }
      )

      if (reviewToDelete != null) {
        DeleteDialog(
          item = reviewToDelete,
          message = stringResource(id = R.string.delete_review_message, reviewToDelete.book.name),
          onDeleteItem = { bookReview -> bookReviewsViewModel.deleteReview(bookReview) },
          onDismiss = { bookReviewsViewModel.onDialogDismissed() }
        )
      }
    }
  }

  private fun startAddBookReview() {
    addReviewContract.launch(REQUEST_CODE_ADD_REVIEW)
  }

  private fun onItemSelected(item: BookReview) {
    startActivity(BookReviewDetailsActivity.getIntent(requireContext(), item))
  }
}