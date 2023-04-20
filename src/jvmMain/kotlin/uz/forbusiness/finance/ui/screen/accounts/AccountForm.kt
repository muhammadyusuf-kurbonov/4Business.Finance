package uz.forbusiness.finance.ui.screen.accounts

import MainRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.forbusiness.finance.models.Account

@Composable
fun AccountForm(
    modifier: Modifier = Modifier,
    initialAccount: Account? = null,
    onAccountChange: (Account) -> Unit,
) {
    val (name, onNameChanged) = remember(initialAccount.toString()) {
        mutableStateOf(initialAccount?.name ?: "")
    }

    val account by remember(name) {
        derivedStateOf {
            Account(
                accountId = 0,
                name = name,
                balance = 0.0
            )
        }
    }

    LaunchedEffect(key1 = account.toString()) {
        onAccountChange(account)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(MainRes.string.account_name_label)
            },
            maxLines = 1
        )
    }
}
