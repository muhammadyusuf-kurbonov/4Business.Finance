package uz.forbusiness.finance.viewModel.transports

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.forbusiness.finance.repository.AppRepository
import uz.forbusiness.finance.viewModel.base.BaseViewModel

class TransactionsViewModel(private val repository: AppRepository): BaseViewModel() {
    private val transactionsScreenState = MutableStateFlow<TransactionsScreenState>(TransactionsScreenState.Loading)
    val transactionsScreenStateState = transactionsScreenState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            transactionsScreenState.tryEmit(TransactionsScreenState.Loading)
            transactionsScreenState.emitAll(
                repository.getAllTransactions().stateIn(viewModelScope).map { list ->
                    if (list.isEmpty())
                        TransactionsScreenState.NoData
                    else
                        TransactionsScreenState.DataFetched(list)
                }
            )
        }
    }
}