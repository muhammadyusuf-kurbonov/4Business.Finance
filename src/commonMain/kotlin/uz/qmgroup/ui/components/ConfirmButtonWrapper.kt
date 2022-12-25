package uz.qmgroup.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState

class ConfirmButtonWrapperProps(
    val onClick: () -> Unit
)

@Composable
fun ConfirmButtonWrapper(
    onConfirmed: () -> Unit,
    title: String = "Подтвердить",
    message: String = "Вы уверены?",
    content: @Composable (props: ConfirmButtonWrapperProps) -> Unit
) {
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }
    val updatedOnConfirmAction by rememberUpdatedState(onConfirmed)

    val onClick = {
        showConfirmDialog = true
    }

    content(
        ConfirmButtonWrapperProps(onClick = onClick)
    )

    if (showConfirmDialog)
        Dialog(
            onCloseRequest = { showConfirmDialog = false },
            state = rememberDialogState(
                height = Dp.Unspecified
            ),
            title = title,
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = message, style = MaterialTheme.typography.body1)

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(
                        onClick = { showConfirmDialog = false }
                    ) {
                        Text("Отменить")
                    }

                    TextButton(
                        onClick = {
                            updatedOnConfirmAction()
                            showConfirmDialog = false
                        }
                    ) {
                        Text("Подтвердить")
                    }
                }
            }
        }
}