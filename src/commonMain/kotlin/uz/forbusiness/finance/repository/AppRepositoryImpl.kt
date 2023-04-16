package uz.forbusiness.finance.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import uz.forbusiness.finance.Database
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.Transaction
import java.util.*

class AppRepositoryImpl(private val database: Database) : AppRepository {

    override suspend fun insertNewTransaction(note: String, amount: Float, dateTime: Date) = withContext(Dispatchers.IO) {
        val transportQueries = database.transactionQueries

        transportQueries.insert(
            note = note,
            amount = amount.toDouble(),
            fromAccount = 0,
            toAccount = 0,
            datetime = dateTime.time
        )
    }

    override fun getAllTransactions(): Flow<List<Transaction>> = database.transactionQueries.queryAll().asFlow().mapToList(Dispatchers.Default)
        .map { it.map { transactions -> transactions.toTransaction() } }

    override fun getAllAccounts(): Flow<List<Account>> = database.accountQueries.queryAll().asFlow().mapToList(Dispatchers.Default)
        .map { it.map { accounts -> accounts.toDomainModel() } }

    override suspend fun searchTransactions(query: String): List<Transaction> = withContext(Dispatchers.IO) {
        val queryResult = if (query.isEmpty())
            database.transactionQueries.queryAll().executeAsList()
        else
            database.transactionQueries.search(query).executeAsList()

        queryResult.map { transport -> transport.toTransaction() }
    }

    override suspend fun getTransportById(id: Long): Transaction? = withContext(Dispatchers.IO) {
        try {
            database.transactionQueries.getById(id).executeAsOne().toTransaction()
        } catch (e: NullPointerException) {
            null
        }
    }

    override suspend fun searchTransport(query: String): List<Transaction> = withContext(Dispatchers.IO) {
        database.transactionQueries.search(query).executeAsList().map { it.toTransaction() }
    }
}