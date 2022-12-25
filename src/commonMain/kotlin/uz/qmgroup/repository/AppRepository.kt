package uz.qmgroup.repository

import kotlinx.coroutines.flow.Flow
import uz.qmgroup.models.Shipment
import uz.qmgroup.models.Transport

interface AppRepository {
    suspend fun createNewTransport(
        driverName: String,
        driverPhone: String,
        transportNumber: String,
        type: String
    )

    fun getAllTransports(): Flow<List<Transport>>

    suspend fun getTransportById(id: Long): Transport?

    fun getAllShipments(): Flow<List<Shipment>>

    suspend fun searchTransport(query: String): List<Transport>

    suspend fun createNewShipment(
        note: String?,
        orderPrefix: String?,
        transportId: Long?,
        status: String,
        pickoffPlace: String,
        destinationPlace: String,
        price: Double,
        author: String
    )

    suspend fun cancelShipment(id: Long)
}