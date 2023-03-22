package uz.forbusiness.finance.ui.screen.transactions

import MainRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.forbusiness.finance.di.AppKoinComponent
import uz.forbusiness.finance.ui.components.EmptyScreen
import uz.forbusiness.finance.ui.components.LoadingScreen
import uz.forbusiness.finance.ui.components.TransactionComponent
import uz.forbusiness.finance.viewModel.transports.TransactionsScreenState
import uz.forbusiness.finance.viewModel.transports.TransactionsViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionsViewModel = AppKoinComponent.transactionsViewModel
) {
    DisposableEffect(viewModel) {
        viewModel.loadData()

        onDispose { viewModel.clear() }
    }

    val state by viewModel.transactionsScreenStateState.collectAsState()
    var dialogState by remember {
        mutableStateOf(false)
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ExtendedFloatingActionButton(
                onClick = { dialogState = true },
                text = { Text(MainRes.string.new_transaction_label) },
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.padding(16.dp)
            )
        }

        AnimatedContent(modifier = modifier.padding(8.dp), targetState = state::class) {
            when (val currentState = state) {
                is TransactionsScreenState.DataFetched -> {
                    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(currentState.transactions) { transaction ->
                            TransactionComponent(modifier = Modifier.fillMaxWidth().clickable {

                            }, transaction = transaction)
                            Divider()
                        }
                    }
                }

                TransactionsScreenState.Loading -> {
                    LoadingScreen()
                }

                TransactionsScreenState.NoData -> {
                    EmptyScreen()
                }
            }
        }
    }

    if (dialogState) {
        NewTransactionDialog(
            onDismissRequest = { dialogState = false }
        )
    }
}