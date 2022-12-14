package com.raywenderlich.android.librarian.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Book
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.model.relations.BookAndGenre
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.books.filter.ByGenre
import com.raywenderlich.android.librarian.ui.books.filter.ByRating
import com.raywenderlich.android.librarian.ui.books.filter.Filter
import com.raywenderlich.android.librarian.ui.books.ui.BookFilter
import com.raywenderlich.android.librarian.ui.books.ui.BooksList
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val REQUEST_CODE_ADD_BOOK = 201

@AndroidEntryPoint
class BooksFragment : Fragment() {

  private val addBookContract by lazy {
    registerForActivityResult(AddBookContract()) { isBookCreated ->
      if (isBookCreated) {
        loadBooks()
        activity?.toast("Book added!")
      }
    }
  }

  @Inject
  lateinit var repository: LibrarianRepository

  private val _booksState = mutableStateOf(emptyList<BookAndGenre>())
  private val _genresState = mutableStateOf<List<Genre>>(emptyList())
  private val _deleteBookState = mutableStateOf<BookAndGenre?>(null)
  var filter: Filter? = null

  @ExperimentalMaterialApi
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    addBookContract
    return ComposeView(requireContext()).apply {
      setContent {
        BooksContent()
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loadGenres()
    loadBooks()
  }

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
      Icon(imageVector = Icons.Default.Edit, tint = Color.White, contentDescription = "Filter")
    }
  }

  @ExperimentalMaterialApi
  @Composable
  fun AddNewBook(bookFilterDrawerState: BottomDrawerState) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(content = {
     Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Book")
    }, onClick = {
      coroutineScope.launch {
        bookFilterDrawerState.close()
        showAddBook()
      }
    })
  }

  @ExperimentalMaterialApi
  @Composable
  fun BookFilterModalDrawer(bookFilterDrawerState: BottomDrawerState) {
    val books = _booksState.value ?: emptyList()

    BottomDrawer(
      drawerState = bookFilterDrawerState,
      gesturesEnabled = false,
      drawerContent ={
        BookFilterModalDrawerContent(Modifier.align(CenterHorizontally), bookFilterDrawerState)
      }, content = {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Center) {
          val bookToDelete = _deleteBookState.value

          if (bookToDelete != null) {
            DeleteDialog(item = bookToDelete,
              message = stringResource(id = R.string.delete_message, bookToDelete.book.name),
              onDeleteItem = {
                removeBook(it.book)
                _deleteBookState.value = null
              },
              onDismiss = { _deleteBookState.value = null })
          }

          BooksList(books, onLongItemTap = { _deleteBookState.value = it })
        }
      })
  }



  @ExperimentalMaterialApi
  @Composable
  fun BookFilterModalDrawerContent(modifier: Modifier, bookFilterDrawerState: BottomDrawerState) {
    val scope = rememberCoroutineScope()
    val genres = _genresState.value ?: emptyList()

    BookFilter(modifier, filter, genres, onFilterSelected = { newFilter ->
      scope.launch {
        bookFilterDrawerState.close()
        filter = newFilter
        loadBooks()
      }
    })
  }

  fun loadGenres() {
    lifecycleScope.launch {
      val genres = repository.getGenres()

      _genresState.value = genres
    }
  }

  fun loadBooks() {
    lifecycleScope.launch {

      val books = when (val currentFilter = filter) {
        is ByGenre -> repository.getBooksByGenre(currentFilter.genreId)
        is ByRating -> repository.getBooksByRating(currentFilter.rating)
        else -> repository.getBooks()
      }

      _booksState.value = books
    }
  }

  fun removeBook(book: Book) {
    lifecycleScope.launch {
      repository.removeBook(book)
      loadBooks()
    }
  }

  private fun showAddBook() {
    addBookContract.launch(REQUEST_CODE_ADD_BOOK)
  }
}