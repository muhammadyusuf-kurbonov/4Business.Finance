package uz.qmgroup.models

enum class ShipmentStatus {
    CREATED, CANCELLED, ASSIGNED, ON_WAY, COMPLETED, UNKNOWN
}

data class Shipment(
    val orderId: Long,
    val note: String,
    val orderPrefix: String,
    val transportId: Long?,
    val transport: Transport?,
    val status: ShipmentStatus,
    val pickoffPlace: String,
    val destinationPlace: String,
    val price: Double,
    val author: String
)