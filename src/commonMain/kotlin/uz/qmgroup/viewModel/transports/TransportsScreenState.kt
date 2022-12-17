package uz.qmgroup.viewModel.transports

import uz.qmgroup.models.Transport

sealed class TransportsScreenState {
    object Loading: TransportsScreenState()
    object NoData: TransportsScreenState()
    class DataFetched(val transports: List<Transport>): TransportsScreenState()
}