package com.raywenderlich.android.librarian.ui.readingList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.ProjectColorTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.readingList.ui.AddReadingList
import com.raywenderlich.android.librarian.ui.readingList.ui.ReadingLists
import com.raywenderlich.android.librarian.ui.readingListDetails.ReadingListDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadingListFragment : Fragment() {

  private val readingListViewModel by viewModels<ReadingListViewModel>()

  @ExperimentalFoundationApi
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        ProjectColorTheme {
          ReadingListContent()
        }
      }
    }
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  @ExperimentalFoundationApi
  @Composable
  fun ReadingListContent() {
    Scaffold(
      topBar = { ReadingListTopBar() },
      floatingActionButton = { AddReadingListButton() }) {
      ReadingListContentWrapper()
    }
  }

  @ExperimentalFoundationApi
  @Composable
  fun ReadingListContentWrapper() {
    val readingLists by readingListViewModel.readingListsState.observeAsState(emptyList())
    val isShowingAddList by readingListViewModel.isShowingAddReadingListState.observeAsState(false)
    val readingListToDelete by readingListViewModel.deleteReadingListState.observeAsState()

    val deleteList = readingListToDelete

    Box(modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center) {

      ReadingLists(
        readingLists,
        onItemClick = { onItemSelected(it) },
        onLongItemTap = { readingListViewModel.onDeleteReadingList(it) }
      )

      if (isShowingAddList) {
        AddReadingList(
          onDismiss = { readingListViewModel.onDialogDismiss() },
          onAddList = { name ->
            readingListViewModel.addReadingList(name)
            readingListViewModel.onDialogDismiss()
          }
        )
      }

      if (deleteList != null) {
        DeleteDialog(
          item = deleteList,
          message = stringResource(id = R.string.delete_message, deleteList.name),
          onDeleteItem = { readingList ->
            readingListViewModel.deleteReadingList(readingList)
            readingListViewModel.onDialogDismiss()
          },
          onDismiss = { readingListViewModel.onDialogDismiss() }
        )
      }
    }
  }

  @Composable
  fun AddReadingListButton() {
    val isShowingAddReadingList = readingListViewModel.isShowingAddReadingListState.value ?: false
    val size by animateDpAsState(targetValue = if (isShowingAddReadingList) 0.dp else 56.dp)

    FloatingActionButton(
      modifier = Modifier.size(size),
      onClick = {
        readingListViewModel.onAddReadingListTapped()
      }) {
      Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reading List")
    }
  }

  @Composable
  fun ReadingListTopBar() {
    TopBar(title = stringResource(id = R.string.reading_lists_title))
  }

  private fun onItemSelected(readingList: ReadingListsWithBooks) {
    startActivity(ReadingListDetailsActivity.getIntent(requireContext(), readingList))
  }
}