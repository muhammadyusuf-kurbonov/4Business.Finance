package uz.forbusiness.finance.ui.root.transactions.dialogs.add

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
import uz.forbusiness.finance.models.Transaction
import uz.forbusiness.finance.ui.components.LoadingScreen
import uz.forbusiness.finance.ui.providers.LocalSnackbarProvider
import uz.forbusiness.finance.ui.root.transactions.components.TransactionForm
import java.util.*

@Composable
fun AddTransactionDialog(
    onDismissRequest: () -> Unit,
    state: AddTransactionUIState,
    save: (Transaction) -> Unit
) {
    val (transaction, onTransactionChange) = remember {
        mutableStateOf(
            Transaction(
                note = "",
                debit = 0,
                credit = 0,
                amount = 0f,
                dateTime = Date(),
                transactionId = 0
            )
        )
    }

    when (state) {
        AddTransactionUIState.SaveCompleted -> {
            val dispatcher = LocalSnackbarProvider.current
            LaunchedEffect(Unit) {
                dispatcher.dispatch(MainRes.string.new_transaction_saved_message)
                onDismissRequest()
            }
        }

        else -> {
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
                        modifier = Modifier.fillMaxWidth().sizeIn(maxHeight = 756.dp, minHeight = 336.dp).height(IntrinsicSize.Min)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            when (state) {
                                AddTransactionUIState.LoadingAccounts -> {
                                    LoadingScreen()
                                }

                                is AddTransactionUIState.WaitingForInput -> {
                                    TransactionForm(
                                        modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize(),
                                        initialValue = transaction,
                                        onTransactionChanged = onTransactionChange,
                                        accountsList = state.accounts
                                    )
                                }

                                else -> {}
                            }
                        }


                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = onDismissRequest) {
                                Text("Отмена")
                            }

                            TextButton(
                                onClick = { save(transaction) },
                                enabled = state is AddTransactionUIState.WaitingForInput
                            ) {
                                Text("Сохранить")
                            }
                        }
                    }
                })
        }

    }
}