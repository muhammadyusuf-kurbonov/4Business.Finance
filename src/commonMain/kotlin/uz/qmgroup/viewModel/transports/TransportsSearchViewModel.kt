package uz.qmgroup.viewModel.transports

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.qmgroup.repository.AppRepository
import uz.qmgroup.viewModel.base.BaseViewModel

class TransportsSearchViewModel(private val repository: AppRepository) : BaseViewModel() {
    private val _searchState = MutableStateFlow<TransportsScreenState>(TransportsScreenState.Loading)
    val searchState = _searchState.asStateFlow()

    fun searchData(query: String) {
        viewModelScope.launch {
            _searchState.tryEmit(TransportsScreenState.Loading)

            val list = repository.searchTransports(query)

            _searchState.emit(
                if (list.isEmpty())
                    TransportsScreenState.NoData
                else
                    TransportsScreenState.DataFetched(list)
            )
        }
    }
}