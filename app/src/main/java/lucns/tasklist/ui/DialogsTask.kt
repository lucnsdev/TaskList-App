package lucns.tasklist.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lucns.tasklist.R

class DialogsTask {

    @Composable
    fun customTextFieldColors(): TextFieldColors {
        val background = colorResource(R.color.edittext_background)
        val border = colorResource(R.color.edittext_border)

        return OutlinedTextFieldDefaults.colors(
            focusedContainerColor = background,
            unfocusedContainerColor = background,
            disabledContainerColor = background,
            focusedBorderColor = border,
            unfocusedBorderColor = border,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray
        )
    }

    @Composable
    fun ShowDialogConfirmation(onDismiss: (confirmed: Boolean) -> Unit, title: String) {
        BasicAlertDialog(
            onDismissRequest = { onDismiss(false) }
        ) {
            Surface(
                modifier = Modifier
                    .width(360.dp)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.dialog_background),
                    ),
                    border = BorderStroke(1.dp, colorResource(R.color.dialog_border)),
                ) {
                    Column() {
                        Text(
                            text = title,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.labelLarge,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            TextButton(
                                contentPadding = PaddingValues(0.dp), modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp), onClick = {
                                    onDismiss(false)
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White,
                                )
                            ) {
                                Text(
                                    stringResource(android.R.string.cancel),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }
                            TextButton(
                                contentPadding = PaddingValues(0.dp), modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp), onClick = {
                                    onDismiss(true)
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White,
                                )
                            ) {
                                Text(
                                    stringResource(R.string.confirm),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    color = colorResource(R.color.accent_text)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ShowDialogRegisterTask(initialTile: String?, initialDescription: String?, onCanceled: () -> Unit, onConfirmed: (title: String, description: String) -> Unit) {
        var title by rememberSaveable { mutableStateOf(initialTile ?: "") }
        var description by rememberSaveable { mutableStateOf(initialDescription ?: "") }
        val keyboardController = LocalSoftwareKeyboardController.current
        BasicAlertDialog(
            onDismissRequest = {}
        ) {
            Surface(
                modifier = Modifier
                    .width(360.dp)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.dialog_background),
                    ),
                    border = BorderStroke(1.dp, colorResource(R.color.dialog_border)),
                ) {
                    Column() {
                        Text(
                            text = stringResource(if (initialTile == null) R.string.add_task else R.string.update_task),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                                .fillMaxWidth(),
                            value = title,
                            maxLines = 1,
                            textStyle = TextStyle(fontSize = 20.sp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, capitalization = KeyboardCapitalization.Sentences),
                            colors = customTextFieldColors(),
                            label = { Text(stringResource(R.string.title)) },
                            shape = RoundedCornerShape(24.dp),
                            onValueChange = {
                                title = it
                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(top = 8.dp, start = 24.dp, end = 24.dp)
                                .fillMaxWidth(),
                            value = description,
                            textStyle = TextStyle(fontSize = 20.sp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.Sentences),
                            label = { Text(stringResource(R.string.description)) },
                            colors = customTextFieldColors(),
                            shape = RoundedCornerShape(24.dp),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    onConfirmed(title, description)
                                }
                            ),
                            onValueChange = {
                                description = it
                            },
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            TextButton(
                                contentPadding = PaddingValues(0.dp), modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp), onClick = {
                                    onCanceled()
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White,
                                )
                            ) {
                                Text(
                                    stringResource(android.R.string.cancel),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }
                            TextButton(
                                contentPadding = PaddingValues(0.dp), modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp), onClick = {
                                    onConfirmed(title, description)
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White,
                                )
                            ) {
                                Text(
                                    stringResource(if (initialTile == null) R.string.add else R.string.update),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    color = colorResource(R.color.accent_text)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}