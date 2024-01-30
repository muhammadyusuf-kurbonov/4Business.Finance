package uz.forbusiness.finance.ui.root.accounts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.AccountType

@Composable
fun AccountForm(
    modifier: Modifier = Modifier,
    initialAccount: Account? = null,
    onAccountChange: (Account) -> Unit,
) {
    val (code, onCodeChanged) = remember(initialAccount.toString()) {
        mutableStateOf(initialAccount?.code ?: 0)
    }
    val (name, onNameChanged) = remember(initialAccount.toString()) {
        mutableStateOf(initialAccount?.name ?: "")
    }
    val (accountType, onAccountTypeChanged) = remember(initialAccount.toString()) {
        mutableStateOf(initialAccount?.type ?: AccountType.ASSETS)
    }

    val account by remember(code, name, accountType) {
        derivedStateOf {
            Account(
                code = code,
                type = accountType,
                name = name
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
            value = code.toString(),
            onValueChange = { onCodeChanged(it.toIntOrNull() ?: 0) },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Code")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                onNameChanged(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Name")
            },
            singleLine = true,
        )

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = accountType == AccountType.ASSETS, onClick = {
                onAccountTypeChanged(AccountType.ASSETS)
            })

            Text("Active")
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = accountType == AccountType.PASSIVE, onClick = {
                onAccountTypeChanged(AccountType.PASSIVE)
            })

            Text("Passive")
        }
    }
}
