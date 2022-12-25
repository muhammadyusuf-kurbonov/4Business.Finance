package uz.qmgroup.viewModel.shipments

import uz.qmgroup.models.Shipment

sealed class ShipmentsScreenState {
    object Loading: ShipmentsScreenState()
    object NoData: ShipmentsScreenState()
    class DataFetched(val orders: List<Shipment>): ShipmentsScreenState()
}