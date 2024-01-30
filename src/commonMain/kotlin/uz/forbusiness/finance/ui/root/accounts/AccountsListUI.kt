package uz.forbusiness.finance.ui.root.accounts

import MainRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import uz.forbusiness.finance.ui.components.EmptyScreen
import uz.forbusiness.finance.ui.components.LoadingScreen
import uz.forbusiness.finance.ui.root.accounts.dialogs.add.AddAccountDialog
import uz.forbusiness.finance.ui.root.accounts.dialogs.add.AddAccountUIState
import java.text.NumberFormat

@Composable
fun AccountsListUI(modifier: Modifier = Modifier, component: AccountsListComponent) {
    val state by component.model.subscribeAsState()

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Accounts", style = MaterialTheme.typography.h5)

            ExtendedFloatingActionButton(
                onClick = component::startAddDialog,
                text = { Text(MainRes.string.add) },
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.padding(16.dp)
            )
        }

        AnimatedContent(modifier = modifier.padding(8.dp), targetState = state::class) {
            when (val currentState = state) {
                is AccountsListUIState.DataFetched -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(currentState.accounts) { account ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.CreditCard,
                                        contentDescription = null
                                    )

                                    Column {
                                        Text(account.account.name, style = MaterialTheme.typography.body1)
                                        Text(account.account.code.toString(), style = MaterialTheme.typography.body2)
                                    }
                                }

                                Text(
                                    NumberFormat.getCurrencyInstance().format(account.balance),
                                    style = MaterialTheme.typography.body2
                                )
                            }
                            Divider()
                        }
                    }
                }

                AccountsListUIState.Loading -> {
                    LoadingScreen()
                }

                AccountsListUIState.NoData -> {
                    EmptyScreen()
                }
            }
        }
    }

    val addDialogState by component.addDialogState.subscribeAsState()

    if (addDialogState !== AddAccountUIState.None) {
        AddAccountDialog(
            onDismissRequest = component::closeAddDialog,
            state = addDialogState,
            save = component::saveNewAccount
        )
    }
}