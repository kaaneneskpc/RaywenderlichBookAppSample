package com.raywenderlich.android.librarian.ui.addBook

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.state.AddBookState
import com.raywenderlich.android.librarian.ui.composeUi.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBookActivity : AppCompatActivity(), AddBookView {

  private val addBookViewModel by viewModels<AddBookViewModel>()

  companion object {
    fun getIntent(context: Context): Intent = Intent(context, AddBookActivity::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ProjectColorTheme {
        AddBookContent()
      }
    }
    addBookViewModel.setView(this)
    addBookViewModel.loadGenres()
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  @Composable
  fun AddBookContent() {
    Scaffold(topBar = { AddBookTopBar() }) {
      AddBookFormContent()
    }
  }

  @Composable
  fun AddBookTopBar() {
    TopBar(
      title = stringResource(id = R.string.add_book_title),
      onBackPressed = { onBackPressedDispatcher.onBackPressed() })
  }

  @Composable
  fun AddBookFormContent() {
    val genres by addBookViewModel.genresState.observeAsState(emptyList())
    val addBookState by addBookViewModel.addBookState.observeAsState(AddBookState())

    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally) {
      InputField(value = addBookState.name,
        onStateChanged = { newValue -> addBookViewModel.onNameChanged(newValue) },
        label = stringResource(id = R.string.book_title_hint),
        isInputValid = addBookState.name.isNotEmpty())

      InputField(value = addBookState.description,
        onStateChanged = { newValue -> addBookViewModel.onDescriptionChanged(newValue) },
        label = stringResource(id = R.string.book_description_hint),
        isInputValid = addBookState.description.isNotEmpty())

      SpinnerPicker(
        pickerText = stringResource(id = R.string.genre_select),
        items = genres,
        itemToName = { it.name },
        onItemPicked = { addBookViewModel.genrePicked(it) })

      ActionButton(
        text = stringResource(id = R.string.add_book_button_text),
        onClick = { addBookViewModel.onAddBookTapped() },
        isEnabled = addBookState.name.isNotEmpty()
                && addBookState.description.isNotEmpty()
                && addBookState.genreId.isNotEmpty())
    }
  }

  override fun onBookAdded() {
    setResult(RESULT_OK)
    finish()
  }
}