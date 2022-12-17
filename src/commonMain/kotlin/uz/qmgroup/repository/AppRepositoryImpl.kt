package uz.qmgroup.repository

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import uz.qmgroup.Database
import uz.qmgroup.models.Transport

class AppRepositoryImpl(private val database: Database, driver: SqlDriver) : AppRepository {
    init {
        Database.Schema.create(driver)
    }
    override suspend fun createNewTransport(
        driverName: String,
        driverPhone: String,
        transportNumber: String,
        type: String
    ) = withContext(Dispatchers.IO) {
        val transportQueries = database.transportQueries

        transportQueries.createNew(
            driverName,
            driverPhone,
            transportNumber,
            type
        )
    }

    override fun getAllTransports(): Flow<List<Transport>> = database.transportQueries.queryAll().asFlow().mapToList()
    override suspend fun getTransportById(id: Long): Transport? {
        return try {
            database.transportQueries.getById(id).executeAsOne()
        } catch (e: NullPointerException) {
            null
        }
    }
}