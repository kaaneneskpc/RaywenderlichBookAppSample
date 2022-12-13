package com.raywenderlich.android.librarian.ui.composeUi

import android.service.autofill.OnClickAction
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R

@Composable
fun DialogButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    onClickAction: () -> Unit) {
    
    TextButton(
        modifier = modifier.padding(8.dp),
        colors = buttonColors(
            backgroundColor = colorResource(id = R.color.colorPrimary),
            contentColor = Color.White),
        onClick = onClickAction) {

        Text(text = stringResource(id = text))
    }
}