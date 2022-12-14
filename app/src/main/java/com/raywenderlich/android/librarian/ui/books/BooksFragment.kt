package com.raywenderlich.android.librarian.ui.books

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.ui.books.ui.BookFilter
import com.raywenderlich.android.librarian.ui.books.ui.BooksList
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.ProjectColorTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val REQUEST_CODE_ADD_BOOK = 201

@AndroidEntryPoint
class BooksFragment : Fragment() {

  private val addBookContract by lazy {
    registerForActivityResult(AddBookContract()) { isBookCreated ->
      if (isBookCreated) {
        booksViewModel.loadBooks()
        activity?.toast("Book added!")
      }
    }
  }

  private val booksViewModel by viewModels<BooksViewModel>()

  @ExperimentalFoundationApi
  @ExperimentalMaterialApi
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    addBookContract
    return ComposeView(requireContext()).apply {
      setContent {
        ProjectColorTheme() {
          BooksContent()
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    booksViewModel.loadGenres()
    booksViewModel.loadBooks()
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  @ExperimentalFoundationApi
  @ExperimentalMaterialApi
  @Composable
  fun BooksContent() {
    val bookFilterDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    Scaffold(topBar = { BooksTopBar(bookFilterDrawerState) },
      floatingActionButton = { AddNewBook(bookFilterDrawerState) }) {
      BookFilterModalDrawer(bookFilterDrawerState)
    }
  }

  @ExperimentalMaterialApi
  @Composable
  fun BooksTopBar(bookFilterDrawerState: BottomDrawerState) {
    TopBar(title = stringResource(id = R.string.my_books_title),
      actions = { FilterButton(bookFilterDrawerState) })
  }

  @ExperimentalMaterialApi
  @Composable
  fun FilterButton(bookFilterDrawerState: BottomDrawerState) {
    val scope = rememberCoroutineScope()

    IconButton(onClick = {
      scope.launch {
        if (!bookFilterDrawerState.isClosed) {
          bookFilterDrawerState.close()
        } else {
          bookFilterDrawerState.expand()
        }
      }
    }) {
      Icon(Icons.Default.Edit, tint = MaterialTheme.colors.onSecondary, contentDescription = "Filter")
    }
  }

  @ExperimentalMaterialApi
  @Composable
  fun AddNewBook(bookFilterDrawerState: BottomDrawerState) {
    val scope = rememberCoroutineScope()

    FloatingActionButton(
      content = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Book") },
      onClick = {
        scope.launch {
          bookFilterDrawerState.close()
          showAddBook()
        }
      })
  }

  @ExperimentalFoundationApi
  @ExperimentalMaterialApi
  @Composable
  fun BookFilterModalDrawer(bookFilterDrawerState: BottomDrawerState) {
    val books by booksViewModel.booksState.observeAsState(emptyList())
    val deleteDialogBook by booksViewModel.deleteBookState.observeAsState()

    val bookToDelete = deleteDialogBook

    BottomDrawer(
      gesturesEnabled = false,
      drawerState = bookFilterDrawerState,
      drawerContent = {
        BookFilterModalDrawerContent(Modifier.align(CenterHorizontally), bookFilterDrawerState)
      },
      content = {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Center) {

          if (bookToDelete != null) {
            DeleteDialog(item = bookToDelete,
              message = stringResource(id = R.string.delete_message, bookToDelete.book.name),
              onDeleteItem = {
                booksViewModel.removeBook(it.book)
                booksViewModel.cancelDeleteBook()
              },
              onDismiss = { booksViewModel.cancelDeleteBook() })
          }

          BooksList(books, onLongItemTap = { booksViewModel.showDeleteBook(it) })
        }
      })
  }

  @ExperimentalMaterialApi
  @Composable
  fun BookFilterModalDrawerContent(
    modifier: Modifier,
    bookFilterDrawerState: BottomDrawerState
  ) {
    val scope = rememberCoroutineScope()
    val genres by booksViewModel.genresState.observeAsState(emptyList())
    val filter = booksViewModel.filter

    BookFilter(modifier, filter, genres, onFilterSelected = { newFilter ->
      scope.launch { bookFilterDrawerState.close() }
      booksViewModel.filter = newFilter
      booksViewModel.loadBooks()
    })
  }

  private fun showAddBook() {
    addBookContract.launch(REQUEST_CODE_ADD_BOOK)
  }
}