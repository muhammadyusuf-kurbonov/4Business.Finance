package uz.forbusiness.finance.ui.screen.transactions

import MainRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.Transaction
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

@Composable
fun TransactionForm(
    modifier: Modifier = Modifier,
    accounts: List<Account> = emptyList(),
    initialTransaction: Transaction? = null,
    onTransactionChange: (Transaction?) -> Unit,
) {
    val (amount, onAmountChanged) = remember(initialTransaction.toString()) {
        mutableStateOf(
            initialTransaction?.amount ?: 0.0
        )
    }
    val (note, onNoteChanged) = remember(initialTransaction.toString()) {
        mutableStateOf(
            initialTransaction?.note ?: ""
        )
    }
    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }
    var selectedFromAccount by remember { mutableStateOf<Account?>(null) }
    var selectedToAccount by remember { mutableStateOf<Account?>(null) }

    val numberFormatter = remember { NumberFormat.getCurrencyInstance() }

    val transaction by remember(amount, note, selectedFromAccount, selectedToAccount) {
        derivedStateOf {
            val fromAccount = selectedFromAccount
            val toAccount = selectedToAccount
            if (fromAccount == null || toAccount == null) null
            else Transaction(
                transactionId = 0,
                note = note,
                amount = amount,
                dateTime = Date(),
                fromAccount = fromAccount,
                toAccount = toAccount
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f).width(IntrinsicSize.Min)
            ) {
                Button(
                    onClick = { expandedFrom = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedFromAccount?.name ?: "From")
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                DropdownMenu(
                    expanded = expandedFrom,
                    onDismissRequest = { expandedFrom = false }
                ) {
                    accounts.forEach {
                        DropdownMenuItem(onClick = {
                            selectedFromAccount = it
                            expandedFrom = false
                        }) {
                            Text(text = it.name)
                        }
                    }
                }
            }

            Text(">")

            Column(
                modifier = Modifier.weight(1f).width(IntrinsicSize.Min)
            ) {
                Button(
                    onClick = { expandedTo = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = selectedToAccount?.name ?: "To")
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                DropdownMenu(
                    expanded = expandedTo,
                    onDismissRequest = { expandedTo = false }
                ) {
                    accounts.forEach {
                        DropdownMenuItem(onClick = {
                            selectedToAccount = it
                            expandedTo = false
                        }) {
                            Text(text = it.name)
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = numberFormatter.format(amount),
            onValueChange = {
                onAmountChanged(
                    try {
                        numberFormatter.parse(it).toDouble()
                    } catch (e: ParseException) {
                        0.0
                    }
                )
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
