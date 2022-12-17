package uz.qmgroup.repository

import kotlinx.coroutines.flow.Flow
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
}