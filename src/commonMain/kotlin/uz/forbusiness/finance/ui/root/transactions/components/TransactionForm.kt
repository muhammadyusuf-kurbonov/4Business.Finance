package uz.forbusiness.finance.ui.root.transactions.components

import MainRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.Transaction
import uz.forbusiness.finance.ui.components.SelectField
import java.util.*

@Composable
fun TransactionForm(
    modifier: Modifier = Modifier,
    accountsList: List<Account>,
    initialValue: Transaction,
    onTransactionChanged: (Transaction) -> Unit
) {
    val (amount, onAmountChanged) = remember(initialValue.toString()) {
        mutableStateOf(
            initialValue.amount
        )
    }
    val (note, onNoteChanged) = remember(initialValue.toString()) {
        mutableStateOf(
            initialValue.note
        )
    }
    val (debit, onDebitChanged) = remember(initialValue.toString()) {
        mutableStateOf(accountsList.find { account -> account.code == initialValue.debit })
    }
    val (credit, onCreditChanged) = remember(initialValue.toString()) {
        mutableStateOf(accountsList.find { account -> account.code == initialValue.credit })
    }

    val transaction by remember(amount, note, debit, credit) {
        derivedStateOf {
            Transaction(
                transactionId = 0,
                note = note,
                amount = amount,
                dateTime = Date(),
                debit = debit?.code ?: -1,
                credit = credit?.code ?: -1
            )
        }
    }

    LaunchedEffect(key1 = transaction.toString()) {
        onTransactionChanged(transaction)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectField(
                modifier = Modifier.weight(1f),
                value = credit,
                onValueChanged = onCreditChanged,
                options = accountsList,
                transformer = { account -> account.name },
                placeholder = "Credit"
            )

            Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)

            SelectField(
                modifier = Modifier.weight(1f),
                value = debit,
                onValueChanged = onDebitChanged,
                options = accountsList,
                transformer = { account -> account.name },
                placeholder = "Debit"
            )
        }

        OutlinedTextField(
            value = amount.toString(),
            onValueChange = {
                onAmountChanged(it.toDoubleOrNull()?.toFloat() ?: 0.0f)
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(MainRes.string.amount_label)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
        )


        OutlinedTextField(
            value = note,
            onValueChange = onNoteChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(MainRes.string.note_label)
            },
            maxLines = 2
        )
    }
}

