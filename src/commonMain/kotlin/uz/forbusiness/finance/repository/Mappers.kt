package uz.forbusiness.finance.repository

import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.Transaction
import uzforbusinessfinance.Accounts
import uzforbusinessfinance.Transactions
import java.util.*

fun Transactions.toTransaction() = Transaction(
    transactionId = id,
    note = note,
    amount = amount,
    dateTime = Date(datetime),
    fromAccount = Account(
        accountId = 0,
        name = "",
        balance = 0.0
    ),
    toAccount = Account(
        accountId = 0,
        name = "",
        balance = 0.0
    ),
)

fun Accounts.toDomainModel() = Account(
    accountId = id,
    name = name,
    balance = balance,
)