package uz.qmgroup.models

data class Transport(
    val transportId: Long,
    val driverName: String,
    val driverPhone: String,
    val transportNumber: String,
    val type: TransportType
)