package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R


@Composable
fun <T> SpinnerPicker(
    pickerText: String,
    preSelectedItem: T? = null,
    items: List<T>,
    itemToName: (T) -> String,
    onItemPicked: (T) -> Unit
){
    val isPickerExpanded = remember { mutableStateOf(false) }
    val pickedItem = remember { mutableStateOf(preSelectedItem?.let(itemToName) ?: "") }

    Row(modifier = Modifier
        .wrapContentWidth()
        .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        DropdownMenuButton(text = pickerText) {
            isPickerExpanded.value = true
        }

        DropdownMenu(
            modifier = Modifier
                .height(300.dp)
                .width(200.dp),
            expanded = isPickerExpanded.value,
            onDismissRequest = { isPickerExpanded.value = false }) {
            for (item in items) {
                DropdownMenuItem(onClick = {
                    onItemPicked(item)
                    isPickerExpanded.value = false
                    pickedItem.value = itemToName(item)
                }) {
                    Text(text = itemToName(item))
                }
            }
        }

        val currentlySelected = pickedItem.value.ifEmpty { "None" }

        Text(
            text = stringResource(id = R.string.current_selection, currentlySelected),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun DropdownMenuButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.padding(end = 16.dp)) {
        Text(text = text, color = MaterialTheme.colors.onSecondary)
    }
}
