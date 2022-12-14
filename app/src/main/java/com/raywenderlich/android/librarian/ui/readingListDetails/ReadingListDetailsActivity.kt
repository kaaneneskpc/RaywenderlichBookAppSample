package com.raywenderlich.android.librarian.ui.readingListDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.BookItem
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.ui.books.ui.BooksList
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.ProjectColorTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.readingListDetails.ui.BookPicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReadingListDetailsActivity : AppCompatActivity() {

  private val readingListDetailsViewModel by viewModels<ReadingListDetailsViewModel>()
  private val LocalReadingList = compositionLocalOf<ReadingListsWithBooks?> {
    error("No reading list!")
  }

  companion object {
    private const val KEY_READING_LIST = "reading_list"

    fun getIntent(context: Context, readingList: ReadingListsWithBooks): Intent {
      val intent = Intent(context, ReadingListDetailsActivity::class.java)

      intent.putExtra(KEY_READING_LIST, readingList)
      return intent
    }
  }

  @ExperimentalFoundationApi
  @ExperimentalMaterialApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val readingList = if (Build.VERSION.SDK_INT >= 33) {
      intent.getParcelableExtra(KEY_READING_LIST, ReadingListsWithBooks::class.java)
    } else {
      intent.getParcelableExtra(KEY_READING_LIST)
    }

    if (readingList != null) {
      readingListDetailsViewModel.setReadingList(readingList)
    } else {
      finish()
      return
    }

    setContent {
      ProjectColorTheme {
        ReadingListDetailsContent()
      }
    }
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  @ExperimentalFoundationApi
  @ExperimentalMaterialApi
  @Composable
  fun ReadingListDetailsContent() {
    val readingListState by readingListDetailsViewModel.readingListState.observeAsState()
    val bottomDrawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)

    CompositionLocalProvider(LocalReadingList provides readingListState) {
      Scaffold(
        topBar = { ReadingListDetailsTopBar() },
        floatingActionButton = { AddBookToReadingList(bottomDrawerState) }
      ) {
        ReadingListDetailsModalDrawer(bottomDrawerState)
      }
    }
  }

  @ExperimentalMaterialApi
  @Composable
  fun AddBookToReadingList(bottomDrawerState: BottomDrawerState) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(onClick = {
      if (bottomDrawerState.isClosed) {
        readingListDetailsViewModel.refreshBooks()

        coroutineScope.launch {
          bottomDrawerState.expand()
        }
      }
    }) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Add Books",
        tint = MaterialTheme.colors.onSecondary
      )
    }
  }

  @Composable
  fun ReadingListDetailsTopBar() {
    val readingList = LocalReadingList.current

    val title = readingList?.name ?: stringResource(id = R.string.reading_list)

    TopBar(title = title, onBackPressed = { onBackPressed() })
  }

  @ExperimentalFoundationApi
  @ExperimentalMaterialApi
  @Composable
  fun ReadingListDetailsModalDrawer(
    drawerState: BottomDrawerState
  ) {
    val readingList = LocalReadingList.current
    val deleteBookState by readingListDetailsViewModel.deleteBookState.observeAsState()
    val addBookState by readingListDetailsViewModel.addBookState.observeAsState(emptyList())

    val bookToDelete = deleteBookState

    BottomDrawer(
      modifier = Modifier.fillMaxWidth(),
      drawerState = drawerState,
      gesturesEnabled = false,
      drawerContent = {
        ReadingListDetailsModalDrawerContent(
          modifier = Modifier.align(Alignment.CenterHorizontally),
          drawerState = drawerState,
          addBookState
        )
      }) {
      Box(
        modifier =
        Modifier
          .fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        BooksList(
          readingList?.books ?: emptyList(),
          onLongItemTap = { book -> readingListDetailsViewModel.onItemLongTapped(book) }
        )

        if (bookToDelete != null) {
          DeleteDialog(
            item = bookToDelete,
            message = stringResource(id = R.string.delete_message, bookToDelete.book.name),
            onDeleteItem = {
              readingListDetailsViewModel.removeBookFromReadingList(it.book.id)
              readingListDetailsViewModel.onDialogDismiss()
            },
            onDismiss = { readingListDetailsViewModel.onDialogDismiss() }
          )
        }
      }
    }
  }

  @ExperimentalMaterialApi
  @Composable
  fun ReadingListDetailsModalDrawerContent(
    modifier: Modifier,
    drawerState: BottomDrawerState,
    addBookState: List<BookItem>
  ) {
    val coroutineScope = rememberCoroutineScope()

    BookPicker(
      modifier = modifier,
      books = addBookState,
      onBookSelected = { readingListDetailsViewModel.bookPickerItemSelected(it) },
      onBookPicked = {
        readingListDetailsViewModel.addBookToReadingList(addBookState.firstOrNull { it.isSelected }?.bookId)

        coroutineScope.launch { drawerState.close() }
      }, onDismiss = { coroutineScope.launch { drawerState.close() } })
  }
}