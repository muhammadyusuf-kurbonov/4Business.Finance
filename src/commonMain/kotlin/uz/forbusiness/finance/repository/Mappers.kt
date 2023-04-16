package uz.forbusiness.finance.repository

import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.Transaction
import uzforbusinessfinance.Accounts
import uzforbusinessfinance.Transactions
import java.util.*

fun Transactions.toTransaction() = Transaction(
    transactionId = id,
    note = note,
    amount = amount.toFloat(),
    dateTime = Date(datetime),
)

fun Accounts.toDomainModel() = Account(
    accountId = id,
    name = name,
    balance = balance
)