package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onDeleteAction: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onDeleteAction,
        content = {
            Icon(imageVector = Icons.Default.Delete,
                tint = Color.Red,
                contentDescription = "Delete")
        })
}