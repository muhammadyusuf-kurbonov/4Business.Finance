package uz.qmgroup.models

data class Shipment(
    val orderId: Long,
    val note: String,
    val orderPrefix: String,
    val transportId: Long?,
    val transport: Transport?,
    val status: String,
    val pickoffPlace: String,
    val destinationPlace: String,
    val price: Double,
    val author: String
)