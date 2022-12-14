package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.Genre
import com.raywenderlich.android.librarian.ui.books.filter.ByGenre
import com.raywenderlich.android.librarian.ui.books.filter.ByRating
import com.raywenderlich.android.librarian.ui.books.filter.Filter
import com.raywenderlich.android.librarian.ui.composeUi.ActionButton
import com.raywenderlich.android.librarian.ui.composeUi.RatingBar
import com.raywenderlich.android.librarian.ui.composeUi.SpinnerPicker

@Composable
fun BookFilter(
  modifier: Modifier = Modifier,
  filter: Filter?,
  genres: List<Genre>,
  onFilterSelected: (Filter?) -> Unit
) {
  val currentFilter = remember {
    mutableStateOf(when (filter) {
      null -> 0
      is ByGenre -> 1
      is ByRating -> 2
    })
  }

  val currentGenreFilter = remember { mutableStateOf<Genre?>(null) }
  val currentRatingFilter = remember { mutableStateOf(0) }

  Column(modifier = modifier.fillMaxSize(),
    horizontalAlignment = CenterHorizontally) {

    Column {
      Row {
        RadioButton(selected = currentFilter.value == 0,
          onClick = { currentFilter.value = 0 },
          modifier = Modifier.padding(8.dp)
        )

        Text(text = stringResource(id = R.string.no_filter),
          modifier = Modifier.align(CenterVertically),
          color = MaterialTheme.colors.onPrimary)
      }

      Row {
        RadioButton(selected = currentFilter.value == 1,
          onClick = { currentFilter.value = 1 },
          modifier = Modifier.padding(8.dp)
        )

        Text(text = stringResource(id = R.string.filter_by_genre),
          modifier = Modifier.align(CenterVertically), color = MaterialTheme.colors.onPrimary)
      }

      Row {
        RadioButton(selected = currentFilter.value == 2,
          onClick = { currentFilter.value = 2 },
          modifier = Modifier.padding(8.dp)
        )

        Text(text = stringResource(id = R.string.filter_by_rating),
          modifier = Modifier.align(CenterVertically), color = MaterialTheme.colors.onPrimary)
      }
    }

    val currentlySelectedGenre = currentGenreFilter.value

    if (currentFilter.value == 1) {
      SpinnerPicker(
        pickerText = stringResource(id = R.string.genre_select),
        items = genres,
        preSelectedItem = currentlySelectedGenre,
        itemToName = { it.name },
        onItemPicked = { currentGenreFilter.value = it })
    }

    if (currentFilter.value == 2) {
      RatingBar(
        modifier = Modifier.align(CenterHorizontally),
        range = 1..5,
        currentRating = currentRatingFilter.value,
        isLargeRating = true,
        onRatingChanged = { newRating -> currentRatingFilter.value = newRating }
      )
    }

    ActionButton(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(id = R.string.confirm_filter), isEnabled = true,
      onClick = {
        val newFilter = when (currentFilter.value) {
          0 -> null
          1 -> ByGenre(currentGenreFilter.value?.id ?: "")
          2 -> ByRating(currentRatingFilter.value)
          else -> throw IllegalArgumentException("Unknown filter!")
        }

        onFilterSelected(newFilter)
      }
    )
  }
}