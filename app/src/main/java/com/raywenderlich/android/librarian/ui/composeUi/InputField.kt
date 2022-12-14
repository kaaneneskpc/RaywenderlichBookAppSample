package com.raywenderlich.android.librarian.ui.composeUi

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.raywenderlich.android.librarian.R

@Composable
fun InputField(
  modifier: Modifier = Modifier,
  value: String = "",
  label: String,
  keyboardType: KeyboardType = KeyboardType.Text,
  isInputValid: Boolean = true,
  imeAction: ImeAction = ImeAction.Next,
  onStateChanged: (String) -> Unit
) {
  val focusedColor = colorResource(id = R.color.colorPrimary)
  val unfocusedColor = colorResource(id = R.color.colorPrimaryDark)

  OutlinedTextField(
    value = value,
    onValueChange = onStateChanged,
    label = { Text(text = label) },
    modifier = modifier
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
    visualTransformation = getVisualTransformation(keyboardType),
    isError = !isInputValid,
    colors = TextFieldDefaults.textFieldColors(
      focusedIndicatorColor = focusedColor,
      focusedLabelColor = focusedColor,
      unfocusedIndicatorColor = unfocusedColor,
      unfocusedLabelColor = unfocusedColor,
      cursorColor = focusedColor,
      backgroundColor = Color.White /* Update Note: Code added to use white color instead of default theme color. */
    )
  )
}

private fun getVisualTransformation(keyboardType: KeyboardType) =
  if (keyboardType == KeyboardType.Password || keyboardType == KeyboardType.NumberPassword)
    PasswordVisualTransformation()
  else VisualTransformation.None