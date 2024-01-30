package uz.forbusiness.finance.ui.root.transactions

import uz.forbusiness.finance.models.Transaction

sealed class TransactionsListUIState {
    object Loading: TransactionsListUIState()
    object NoData: TransactionsListUIState()
    class DataFetched(val transactions: List<Transaction>): TransactionsListUIState()
}