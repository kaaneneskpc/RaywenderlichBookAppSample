package com.raywenderlich.android.librarian.ui.readingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.ReadingList
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.repository.LibrarianRepository
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.readingListDetails.ReadingListDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReadingListFragment : Fragment() {

  @Inject
  lateinit var repository: LibrarianRepository

  private val readingListsState = mutableStateOf(emptyList<ReadingListsWithBooks>())
  private val _isShowingAddReadingListState = mutableStateOf(false)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        ReadingListContent()
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loadReadingLists()
  }

  private fun loadReadingLists() {
    lifecycleScope.launch {
      readingListsState.value = repository.getReadingLists()
    }
  }

  @Composable
  fun ReadingListContent() {
    Scaffold(
      topBar = { ReadingListTopBar() },
      floatingActionButton = { AddReadingListButton() }) {
      ReadingListContentWrapper()
    }
  }

  @Composable
  fun ReadingListContentWrapper() {
    val readingLists = readingListsState.value

    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {

      ReadingLists(readingLists = readingLists, onItemClick = { onItemSelected(it) }, onDeleteItemClick = { deleteReadingList(it) })

      val isShowingAddList = _isShowingAddReadingListState.value


      if (isShowingAddList) {
        AddReadingList(
          onDismiss = { _isShowingAddReadingListState.value = false },
          onAddList = { name ->
            addReadingList(name)
            _isShowingAddReadingListState.value = false
          }
        )
      }
    }
  }

  @Composable
  fun AddReadingListButton() {
    FloatingActionButton(onClick = {
      _isShowingAddReadingListState.value = true
    }) {
      Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reading List")
    }
  }

  @Composable
  fun ReadingListTopBar() {
    TopBar(title = stringResource(id = R.string.reading_lists_title))
  }

  fun deleteReadingList(readingListsWithBooks: ReadingListsWithBooks) {
    lifecycleScope.launch {
      repository.removeReadingList(
        ReadingList(
          readingListsWithBooks.id,
          readingListsWithBooks.name,
          readingListsWithBooks.books.map { it.book.id }
        )
      )
      readingListsState.value = repository.getReadingLists()
    }
  }

  fun addReadingList(readingListName: String) {
    lifecycleScope.launch {
      repository.addReadingList(ReadingList(name = readingListName, bookIds = emptyList()))
      readingListsState.value = repository.getReadingLists()
    }
  }

  private fun onItemSelected(readingList: ReadingListsWithBooks) {
    startActivity(ReadingListDetailsActivity.getIntent(requireContext(), readingList))
  }
}