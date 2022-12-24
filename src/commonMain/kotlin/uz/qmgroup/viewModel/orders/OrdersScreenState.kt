package uz.qmgroup.viewModel.orders

import uz.qmgroup.models.Shipment

sealed class OrdersScreenState {
    object Loading: OrdersScreenState()
    object NoData: OrdersScreenState()
    class DataFetched(val orders: List<Shipment>): OrdersScreenState()
}