package uz.forbusiness.finance.repository

import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.Transaction
import uzforbusinessfinance.ACCOUNTS
import uzforbusinessfinance.TRANSACTIONS
import java.util.*

fun TRANSACTIONS.toTransaction() = Transaction(
    transactionId = id,
    note = note,
    amount = amount.toFloat(),
    dateTime = Date(datetime),
    debit = debit_account.toInt(),
    credit = credit_account.toInt()
)

fun ACCOUNTS.toAccount() = Account(
    name = name,
    code = code.toInt(),
    type = type
)