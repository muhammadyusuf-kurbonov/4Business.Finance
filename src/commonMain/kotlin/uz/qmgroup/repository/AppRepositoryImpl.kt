package uz.qmgroup.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import uz.qmgroup.Database
import uz.qmgroup.models.Shipment
import uz.qmgroup.models.Transport

class AppRepositoryImpl(private val database: Database) : AppRepository {

    override suspend fun createNewTransport(
        driverName: String,
        driverPhone: String,
        transportNumber: String,
        type: String
    ) = withContext(Dispatchers.IO) {
        val transportQueries = database.transportQueries

        transportQueries.createNew(
            driverName = driverName,
            driverPhone = driverPhone,
            transportNumber = transportNumber,
            type = type
        )
    }

    override fun getAllTransports(): Flow<List<Transport>> = database.transportQueries.queryAll().asFlow().mapToList()
        .map { it.map { transport -> transport.toTransport() } }

    override suspend fun searchTransports(query: String): List<Transport> = withContext(Dispatchers.IO) {
        val queryResult = if (query.isEmpty())
            database.transportQueries.queryAll().executeAsList()
        else
            database.transportQueries.search("%$query%").executeAsList()

        queryResult.map { transport -> transport.toTransport() }
    }

    override suspend fun getTransportById(id: Long): Transport? = withContext(Dispatchers.IO) {
        try {
            database.transportQueries.getById(id).executeAsOne().toTransport()
        } catch (e: NullPointerException) {
            null
        }
    }

    override fun getAllShipments(): Flow<List<Shipment>> =
        database.orderQueries.queryAll().asFlow().mapToList().map { it.map { order -> order.toShipment(database) } }

    override suspend fun searchTransport(query: String): List<Transport> = withContext(Dispatchers.IO) {
        database.transportQueries.search(query).executeAsList().map { it.toTransport() }
    }

    override suspend fun createNewShipment(
        note: String?,
        orderPrefix: String?,
        transportId: Long?,
        status: String,
        pickoffPlace: String,
        destinationPlace: String,
        price: Double,
        author: String
    ) = withContext(Dispatchers.IO) {
        database.orderQueries.createNewOrder(
            note = note,
            transportId = transportId,
            orderPrefix = orderPrefix,
            status = status,
            pickoffPlace = pickoffPlace,
            destinationPlace = destinationPlace,
            price = price,
            author = author
        )
    }

    override suspend fun cancelShipment(id: Long) = withContext(Dispatchers.IO) {
        database.orderQueries.cancelOrder(id)
    }

    override suspend fun startShipment(id: Long) = withContext(Dispatchers.IO) {
        database.orderQueries.executeOrder(id)
    }

    override suspend fun completeShipment(id: Long) = withContext(Dispatchers.IO) {
        database.orderQueries.completeOrder(id)
    }

    override suspend fun assignTransportToShipment(id: Long, transport: Transport) = withContext(Dispatchers.IO) {
        database.orderQueries.assignOrder(transport.transportId, id)
    }
}