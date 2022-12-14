package com.raywenderlich.android.librarian.ui.addReview.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.BookAndGenre

@Composable
fun BookPicker(
    books: List<BookAndGenre>,
    selectedBookId: String,
    onItemPicked: (BookAndGenre) -> Unit
) {
    val isPickerOpen = remember { mutableStateOf(false) }
    val selectedBookName = books.firstOrNull { it.book.id == selectedBookId }?.book?.name
        ?: "None"

    Row(verticalAlignment = Alignment.CenterVertically) {

        TextButton(onClick = { isPickerOpen.value = true },
            content = { Text(text = stringResource(id = R.string.book_select)) })

        DropdownMenu(expanded = isPickerOpen.value,
            onDismissRequest = { isPickerOpen.value = false }) {
            for (book in books) {
                DropdownMenuItem(onClick = {
                    onItemPicked(book)
                    isPickerOpen.value = false
                }) {
                    Text(text = book.book.name, color = MaterialTheme.colors.onPrimary)
                }
            }
        }

        Text(text = selectedBookName, color = MaterialTheme.colors.onPrimary)
    }
}