package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.model.relations.BookAndGenre

@Composable
fun BooksList(
  books: List<BookAndGenre>,
  onLongItemTap: (BookAndGenre) -> Unit = {}
) {
  LazyColumn(modifier = Modifier.padding(top = 16.dp),
    verticalArrangement = Arrangement.spacedBy(2.dp)) {
    items(books) { bookAndGenre ->
      BookListItem(bookAndGenre, onLongItemTap)
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookListItem(bookAndGenre: BookAndGenre, onLongItemTap: (BookAndGenre) -> Unit) {
  Card(modifier = Modifier
    .wrapContentHeight()
    .fillMaxWidth()
    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    .combinedClickable(
      interactionSource = remember { MutableInteractionSource() },
      onClick = {},
      onLongClick = { onLongItemTap(bookAndGenre) },
      indication = null),
    elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp)) {

    Row(modifier = Modifier.fillMaxSize()) {
      Spacer(modifier = Modifier.width(16.dp))

      Column {
        Text(
          modifier = Modifier.padding(top = 16.dp),
          text = bookAndGenre.book.name,
          color = MaterialTheme.colors.primary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(text = bookAndGenre.genre.name,
          fontSize = 16.sp,
          fontStyle = FontStyle.Italic)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = bookAndGenre.book.description,
          fontSize = 12.sp,
          overflow = TextOverflow.Ellipsis,
          fontStyle = FontStyle.Italic,
          modifier = Modifier
            .fillMaxHeight()
            .padding(end = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}