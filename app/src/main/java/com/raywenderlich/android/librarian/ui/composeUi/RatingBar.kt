package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R

@Composable
fun RatingBar(
  modifier: Modifier = Modifier,
  range: IntRange,
  isLargeRating: Boolean = true,
  isSelectable: Boolean = true,
  currentRating: Int = 0,
  onRatingChanged: (Int) -> Unit
) {
  val selectedRating = remember { mutableStateOf(currentRating) }

  LazyRow(modifier = modifier) {
    items(range.toList()) { index ->
      RatingItem(
        isSelected = index <= selectedRating.value,
        isSelectable = isSelectable,
        rating = index,
        isLargeRating = isLargeRating
      ) { newRating ->
        selectedRating.value = newRating
        onRatingChanged(selectedRating.value)
      }
    }
  }
}

@Composable
fun RatingItem(
  isSelected: Boolean,
  isSelectable: Boolean,
  rating: Int,
  isLargeRating: Boolean,
  onRatingSelected: (Int) -> Unit
) {
  val padding = if (isLargeRating) 2.dp else 0.dp
  val size = if (isLargeRating) 32.dp else 16.dp

  val baseModifier = if (isSelectable) {
    Modifier.clickable(onClick = { onRatingSelected(rating) })
  } else {
    Modifier
  }

  Icon(
    modifier = baseModifier
      .size(size)
      .padding(padding),
    imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarOutline,
    contentDescription = rating.toString(),
    tint = colorResource(id = R.color.orange_200)
  )
}