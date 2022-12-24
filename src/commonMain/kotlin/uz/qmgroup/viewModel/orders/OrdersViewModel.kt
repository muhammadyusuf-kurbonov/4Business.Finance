package uz.qmgroup.viewModel.orders

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.qmgroup.repository.AppRepository
import uz.qmgroup.viewModel.base.BaseViewModel

class OrdersViewModel(private val repository: AppRepository): BaseViewModel() {
    private val _ordersScreenState = MutableStateFlow<OrdersScreenState>(OrdersScreenState.Loading)
    val ordersScreenState = _ordersScreenState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _ordersScreenState.tryEmit(OrdersScreenState.Loading)
            _ordersScreenState.emitAll(
                repository.getAllShipments().stateIn(viewModelScope).map { list ->
                    if (list.isEmpty())
                        OrdersScreenState.NoData
                    else
                        OrdersScreenState.DataFetched(list)
                }
            )
        }
    }
}