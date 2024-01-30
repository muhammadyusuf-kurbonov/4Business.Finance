package uz.forbusiness.finance.repository

import kotlinx.coroutines.flow.Flow
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.AccountWithBalance
import uz.forbusiness.finance.models.Transaction
import java.util.*

interface AppRepository {
    suspend fun insertNewTransaction(
        note: String,
        amount: Float,
        dateTime: Date,
        debit: Int,
        credit: Int
    )

    fun getAllTransactions(): Flow<List<Transaction>>

    suspend fun searchTransactions(query: String): List<Transaction>

    suspend fun getTransportById(id: Long): Transaction?

    suspend fun searchTransport(query: String): List<Transaction>

    fun getAllAccounts(): Flow<List<Account>>

    fun getAllAccountsWithBalances(): Flow<List<AccountWithBalance>>

    suspend fun saveNewAccount(account: Account)
}