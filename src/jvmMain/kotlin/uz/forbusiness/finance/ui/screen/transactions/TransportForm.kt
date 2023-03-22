package uz.forbusiness.finance.ui.screen.transactions

import MainRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.forbusiness.finance.models.Transaction
import java.util.*

@Composable
fun TransactionForm(
    modifier: Modifier = Modifier,
    initialTransaction: Transaction? = null,
    onTransactionChange: (Transaction) -> Unit,
) {
    val (amount, onAmountChanged) = remember(initialTransaction.toString()) {
        mutableStateOf(
            initialTransaction?.amount ?: 0f
        )
    }
    val (note, onNoteChanged) = remember(initialTransaction.toString()) {
        mutableStateOf(
            initialTransaction?.note ?: ""
        )
    }

    val transaction by remember(amount, note) {
        derivedStateOf {
            Transaction(
                transactionId = 0,
                note = note,
                amount = amount,
                dateTime = Date()
            )
        }
    }

    LaunchedEffect(key1 = transaction.toString()) {
        onTransactionChange(transaction)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
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
