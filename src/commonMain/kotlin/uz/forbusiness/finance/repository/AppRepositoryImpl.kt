package uz.forbusiness.finance.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import uz.forbusiness.finance.Database
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.AccountWithBalance
import uz.forbusiness.finance.models.Transaction
import java.util.*

class AppRepositoryImpl(private val database: Database) : AppRepository {
    override suspend fun insertNewTransaction(note: String, amount: Float, dateTime: Date, debit: Int, credit: Int) =
        withContext(Dispatchers.IO) {
            val transportQueries = database.transactionsQueries

            transportQueries.insert(
                note = note,
                amount = amount.toDouble(),
                datetime = dateTime.time,
                debit_account = debit.toLong(),
                credit_account = credit.toLong()
            )
        }

    override fun getAllTransactions(): Flow<List<Transaction>> =
        database.transactionsQueries.queryAll().asFlow().mapToList(Dispatchers.IO)
            .map { it.map { transactions -> transactions.toTransaction() } }

    override fun getAllAccounts(): Flow<List<Account>> =
        database.accountsQueries.queryAll().asFlow().mapToList(Dispatchers.IO)
            .map { it.map { account -> account.toAccount() } }

    override fun getAllAccountsWithBalances(): Flow<List<AccountWithBalance>> =
        database.accountsQueries.queryWithBalances().asFlow()
            .mapToList(Dispatchers.IO)
            .map {
                it.map { account ->
                    AccountWithBalance(
                        Account(account.name, account.code.toInt(), account.type),
                        balance = account.balance?.toFloat() ?: 0f
                    )
                }
            }

    override suspend fun saveNewAccount(account: Account) = withContext(Dispatchers.IO) {
        database.accountsQueries.insert(
            account.code.toLong(),
            account.name,
            account.type
        )
    }


    override suspend fun searchTransactions(query: String): List<Transaction> = withContext(Dispatchers.IO) {
        val queryResult = if (query.isEmpty())
            database.transactionsQueries.queryAll().executeAsList()
        else
            database.transactionsQueries.search(query).executeAsList()

        queryResult.map { transport -> transport.toTransaction() }
    }

    override suspend fun getTransportById(id: Long): Transaction? = withContext(Dispatchers.IO) {
        try {
            database.transactionsQueries.getById(id).executeAsOne().toTransaction()
        } catch (e: NullPointerException) {
            null
        }
    }

    override suspend fun searchTransport(query: String): List<Transaction> = withContext(Dispatchers.IO) {
        database.transactionsQueries.search(query).executeAsList().map { it.toTransaction() }
    }
}