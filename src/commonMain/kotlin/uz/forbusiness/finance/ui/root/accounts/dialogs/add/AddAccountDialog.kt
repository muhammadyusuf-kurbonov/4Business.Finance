package uz.forbusiness.finance.ui.root.accounts.dialogs.add

import MainRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.AccountType
import uz.forbusiness.finance.ui.providers.LocalSnackbarProvider
import uz.forbusiness.finance.ui.root.accounts.components.AccountForm


@Composable
fun AddAccountDialog(
    onDismissRequest: () -> Unit,
    state: AddAccountUIState,
    save: (Account) -> Unit
) {
    val (account, onAccountChange) = remember {
        mutableStateOf(
            Account(
                code = 0,
                name = "",
                type = AccountType.ASSETS
            )
        )
    }

    when (state) {
        AddAccountUIState.WaitingForInput, AddAccountUIState.Saving -> {
            DialogWindow(
                onCloseRequest = onDismissRequest,
                state = rememberDialogState(
                    height = Dp.Unspecified
                ),
                title = MainRes.string.new_account_title,
                resizable = false,
                onKeyEvent = {
                    when (it.key) {
                        Key.Escape -> onDismissRequest()
                    }
                    false
                },
                content = {
                    Column(
                        modifier = Modifier.fillMaxWidth().sizeIn(maxHeight = 512.dp).height(IntrinsicSize.Min).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AccountForm(
                            modifier = Modifier.verticalScroll(rememberScrollState()).weight(1f),
                            initialAccount = account,
                            onAccountChange = onAccountChange
                        )

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = onDismissRequest) {
                                Text("Отмена")
                            }

                            TextButton(
                                onClick = {
                                    save(account)
                                },
                                enabled = state == AddAccountUIState.WaitingForInput
                            ) {
                                Text("Сохранить")
                            }
                        }
                    }
                })
        }

        AddAccountUIState.SaveCompleted -> {
            val dispatcher = LocalSnackbarProvider.current
            LaunchedEffect(Unit) {
                dispatcher.dispatch(MainRes.string.new_transaction_saved_message)
                onDismissRequest()
            }
        }

        else -> {}
    }
}