package uz.qmgroup.viewModel.transports

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.qmgroup.repository.AppRepository
import uz.qmgroup.viewModel.base.BaseViewModel

class TransportsViewModel(private val repository: AppRepository): BaseViewModel() {
    private val _transportsScreenState = MutableStateFlow<TransportsScreenState>(TransportsScreenState.Loading)
    val transportScreenState = _transportsScreenState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _transportsScreenState.tryEmit(TransportsScreenState.Loading)
            _transportsScreenState.emitAll(
                repository.getAllTransports().stateIn(viewModelScope).map {list ->
                    if (list.isEmpty())
                        TransportsScreenState.NoData
                    else
                        TransportsScreenState.DataFetched(list)
                }
            )
        }
    }
}