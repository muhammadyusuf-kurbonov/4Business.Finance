package uz.forbusiness.finance.ui.root.transactions

import MainRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import uz.forbusiness.finance.ui.components.EmptyScreen
import uz.forbusiness.finance.ui.components.LoadingScreen
import uz.forbusiness.finance.ui.components.TransactionComponent
import uz.forbusiness.finance.ui.root.transactions.dialogs.add.AddTransactionDialog
import uz.forbusiness.finance.ui.root.transactions.dialogs.add.AddTransactionUIState

@Composable
fun TransactionsListUI(
    modifier: Modifier = Modifier,
    component: TransactionsListComponent
) {
    val state by component.model.subscribeAsState()
    val addTransactionUIState by component.addTransactionUIState.subscribeAsState()

    Column(modifier = modifier) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Transactions", style = MaterialTheme.typography.h5)

                ExtendedFloatingActionButton(
                    onClick = component::openAddDialog,
                    text = { Text(MainRes.string.new_transaction_label) },
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    modifier = Modifier.padding(16.dp)
                )
            }

            AnimatedContent(modifier = modifier.padding(8.dp), targetState = state::class) {
                when (val currentState = state) {
                    is TransactionsListUIState.DataFetched -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(currentState.transactions) { transaction ->
                                TransactionComponent(modifier = Modifier.fillMaxWidth(), transaction = transaction)
                                Divider()
                            }
                        }
                    }

                    TransactionsListUIState.Loading -> {
                        LoadingScreen()
                    }

                    TransactionsListUIState.NoData -> {
                        EmptyScreen()
                    }
                }
            }
        }

        if (addTransactionUIState != AddTransactionUIState.None) {
            AddTransactionDialog(
                onDismissRequest = component::closeAddDialog,
                state = addTransactionUIState,
                save = component::saveTransaction
            )
        }
    }
}