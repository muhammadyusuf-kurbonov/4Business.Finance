package uz.forbusiness.finance.models

import java.util.Date

data class Transaction(
    val transactionId: Long,
    val note: String,
    val amount: Float,
    val dateTime: Date
)