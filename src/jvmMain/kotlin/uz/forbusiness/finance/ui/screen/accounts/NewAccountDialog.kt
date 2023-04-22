package uz.forbusiness.finance.ui.screen.accounts

import MainRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import uz.forbusiness.finance.di.AppKoinComponent
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.ui.providers.LocalSnackbarProvider
import uz.forbusiness.finance.viewModel.accounts.AccountDialogState
import uz.forbusiness.finance.viewModel.accounts.AccountDialogViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NewAccountDialog(
    onDismissRequest: () -> Unit,
    viewModel: AccountDialogViewModel = AppKoinComponent.accountDialogViewModel
) {
    val (account, onAccountChange) = remember {
        mutableStateOf(
            Account(
                accountId = 0,
                name = "",
                balance = 0.0
            )
        )
    }

    DisposableEffect(Unit) {
        viewModel.initialize()

        onDispose { viewModel.clear() }
    }

    val currentState by viewModel.state.collectAsState()

    when (currentState) {
        AccountDialogState.Default, AccountDialogState.SavePending -> {
            Dialog(
                onCloseRequest = onDismissRequest,
                state = rememberDialogState(
                    height = Dp.Unspecified
                ),
                title = MainRes.string.new_transaction_title,
                resizable = false,
                onKeyEvent = {
                    when (it.key) {
                        Key.Escape -> onDismissRequest()
                    }
                    false
                }
            ) {
                AnimatedContent(currentState) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AccountForm(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            initialAccount = account,
                            onAccountChange = onAccountChange
                        )

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = onDismissRequest) {
                                Text("Отмена")
                            }

                            TextButton(onClick = {
                                viewModel.save(account)
                            }, enabled = it == AccountDialogState.Default) {
                                Text("Сохранить")
                            }
                        }
                    }
                }
            }
        }

        is AccountDialogState.SaveFailed -> {}
        AccountDialogState.SaveCompleted -> {
            val dispatcher = LocalSnackbarProvider.current
            LaunchedEffect(Unit) {
                dispatcher.dispatch(MainRes.string.new_account_saved_message)
                onDismissRequest()
            }
        }

        else -> {}
    }
}