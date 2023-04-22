package uz.forbusiness.finance.ui.screen.transactions

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
import uz.forbusiness.finance.models.Transaction
import uz.forbusiness.finance.ui.providers.LocalSnackbarProvider
import uz.forbusiness.finance.viewModel.transports.TransactionDialogViewModel
import uz.forbusiness.finance.viewModel.transports.TransportDialogState

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NewTransactionDialog(
    onDismissRequest: () -> Unit,
    viewModel: TransactionDialogViewModel = AppKoinComponent.transactionDialogViewModel
) {
    val (transaction, onTransactionChange) = remember {
        mutableStateOf<Transaction?>(null)
    }

    DisposableEffect(Unit) {
        viewModel.initialize()

        onDispose { viewModel.clear() }
    }

    val currentState by viewModel.state.collectAsState()
    val accounts by produceState<List<Account>>(emptyList(), Unit) {
        this.value = viewModel.fetchAccounts()
    }

    when (currentState) {
        TransportDialogState.Default, TransportDialogState.SavePending -> {
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
                        TransactionForm(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            accounts = accounts,
                            initialTransaction = transaction,
                            onTransactionChange = onTransactionChange
                        )

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = onDismissRequest) {
                                Text("Отмена")
                            }

                            TextButton(
                                onClick = {
                                    if (transaction == null) return@TextButton
                                    viewModel.save(transaction)
                                },
                                enabled = it == TransportDialogState.Default && transaction != null
                            ) {
                                Text("Сохранить")
                            }
                        }
                    }
                }
            }
        }

        is TransportDialogState.SaveFailed -> {}
        TransportDialogState.SaveCompleted -> {
            val dispatcher = LocalSnackbarProvider.current
            LaunchedEffect(Unit) {
                dispatcher.dispatch(MainRes.string.new_transaction_saved_message)
                onDismissRequest()
            }
        }

        else -> {}
    }
}