package uz.forbusiness.finance.ui.screen.accounts

import MainRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.forbusiness.finance.di.AppKoinComponent
import uz.forbusiness.finance.ui.components.EmptyScreen
import uz.forbusiness.finance.ui.components.LoadingScreen
import uz.forbusiness.finance.viewModel.accounts.AccountsScreenState
import uz.forbusiness.finance.viewModel.accounts.AccountsViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AccountsScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = AppKoinComponent.accountsViewModel
) {
    DisposableEffect(viewModel) {
        viewModel.loadData()

        onDispose { viewModel.clear() }
    }

    val state by viewModel.accountsScreenState.collectAsState()
    var dialogState by remember {
        mutableStateOf(false)
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ExtendedFloatingActionButton(
                onClick = { dialogState = true },
                text = { Text(MainRes.string.new_account_label) },
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.padding(16.dp)
            )
        }

        AnimatedContent(modifier = modifier.padding(8.dp), targetState = state::class) {
            when (val currentState = state) {
                is AccountsScreenState.DataFetched -> {
                    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(currentState.accounts) { account ->
                            Text(account.name)
                            Divider()
                        }
                    }
                }

                AccountsScreenState.Loading -> {
                    LoadingScreen()
                }

                AccountsScreenState.NoData -> {
                    EmptyScreen()
                }
            }
        }
    }

    if (dialogState) {
        NewAccountDialog(onDismissRequest = {
            dialogState = false
        })
    }
}