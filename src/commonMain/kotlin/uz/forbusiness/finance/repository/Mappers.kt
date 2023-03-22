package uz.forbusiness.finance.repository

import uz.forbusiness.finance.TRANSACTIONS
import uz.forbusiness.finance.models.Transaction
import java.util.*

fun TRANSACTIONS.toTransaction() = Transaction(
    transactionId = id,
    note = note,
    amount = amount.toFloat(),
    dateTime = Date(datetime),
)