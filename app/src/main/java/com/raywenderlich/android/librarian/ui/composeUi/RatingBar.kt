/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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