package uz.qmgroup.viewModel.transports

import uz.qmgroup.models.Transport

sealed class TransportDialogState {
    object Default: TransportDialogState()

    object Fetching: TransportDialogState()

    class DataFetched(val transport: Transport): TransportDialogState()

    object SavePending: TransportDialogState()

    object SaveCompleted: TransportDialogState()

    class SaveFailed(val error: String): TransportDialogState()
}
