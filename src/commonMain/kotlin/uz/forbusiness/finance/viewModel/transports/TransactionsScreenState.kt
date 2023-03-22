package uz.forbusiness.finance.viewModel.transports

import uz.forbusiness.finance.models.Transaction

sealed class TransactionsScreenState {
    object Loading: TransactionsScreenState()
    object NoData: TransactionsScreenState()
    class DataFetched(val transactions: List<Transaction>): TransactionsScreenState()
}