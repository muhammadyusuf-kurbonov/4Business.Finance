package uz.forbusiness.finance.viewModel.transports

import uz.forbusiness.finance.models.Transaction

sealed class TransportDialogState {
    object Default: TransportDialogState()

    object Fetching: TransportDialogState()

    class DataFetched(val transaction: Transaction): TransportDialogState()

    object SavePending: TransportDialogState()

    object SaveCompleted: TransportDialogState()

    object SaveFailed : TransportDialogState()
}
