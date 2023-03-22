package uz.forbusiness.finance.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import uz.forbusiness.finance.Database
import uz.forbusiness.finance.models.Transaction
import java.util.*

class AppRepositoryImpl(private val database: Database) : AppRepository {

    override suspend fun insertNewTransaction(note: String, amount: Float, dateTime: Date) = withContext(Dispatchers.IO) {
        val transportQueries = database.transactionsQueries

        transportQueries.insert(
            note = note,
            amount = amount.toDouble(),
            datetime = dateTime.time
        )
    }

    override fun getAllTransactions(): Flow<List<Transaction>> = database.transactionsQueries.queryAll().asFlow().mapToList()
        .map { it.map { transactions -> transactions.toTransaction() } }

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