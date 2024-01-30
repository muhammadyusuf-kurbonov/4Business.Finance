package uz.forbusiness.finance.models

data class Account(
    val name: String,
    val code: Int,
    val type: AccountType
)