package uz.qmgroup.viewModel.orders

import uz.qmgroup.models.Transport

sealed class OrderDialogState {
    object Default: OrderDialogState()

    object Fetching: OrderDialogState()

    class DataFetched(val transport: Transport): OrderDialogState()

    object SavePending: OrderDialogState()

    object SaveCompleted: OrderDialogState()

    class SaveFailed(val error: String): OrderDialogState()
}
