package uz.qmgroup.viewModel.shipments

import uz.qmgroup.models.Transport

sealed class ShipmentDialogState {
    object Default: ShipmentDialogState()

    object Fetching: ShipmentDialogState()

    class DataFetched(val transport: Transport): ShipmentDialogState()

    object SavePending: ShipmentDialogState()

    object SaveCompleted: ShipmentDialogState()

    class SaveFailed(val error: String): ShipmentDialogState()
}
