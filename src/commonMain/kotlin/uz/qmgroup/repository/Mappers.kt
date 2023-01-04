package uz.qmgroup.repository

import uz.qmgroup.Database
import uz.qmgroup.ORDERS
import uz.qmgroup.TRANSPORTS
import uz.qmgroup.models.Shipment
import uz.qmgroup.models.ShipmentStatus
import uz.qmgroup.models.Transport
import uz.qmgroup.models.TransportType

fun ORDERS.toShipment(database: Database) = Shipment(
    orderId = orderId,
    orderPrefix = orderPrefix.orEmpty(),
    note = note.orEmpty(),
    transportId = transportId,
    status = try {
        ShipmentStatus.valueOf(status.uppercase())
    } catch (e: IllegalArgumentException) {
        ShipmentStatus.UNKNOWN
    },
    transport = transportId?.let {
        try {
            database.transportQueries.getById(it).executeAsOne().toTransport()
        } catch (e: NullPointerException) {
            null
        }
    },
    pickoffPlace = pickoffPlace,
    destinationPlace = destinationPlace,
    price = price,
    author = author
)

fun TRANSPORTS.toTransport() = Transport(
    transportId = transportId,
    driverName = driverName,
    driverPhone = driverPhone,
    transportNumber = transportNumber,
    type = TransportType.values().first { it.stringValue == type }
)