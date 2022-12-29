package uz.qmgroup.viewModel.shipments

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.qmgroup.models.Shipment
import uz.qmgroup.models.Transport
import uz.qmgroup.repository.AppRepository
import uz.qmgroup.viewModel.base.BaseViewModel

class ShipmentsViewModel(private val repository: AppRepository): BaseViewModel() {
    private val _shipmentsScreenState = MutableStateFlow<ShipmentsScreenState>(ShipmentsScreenState.Loading)
    val shipmentsScreenState = _shipmentsScreenState.asStateFlow()

    private val _workingShipmentsList = MutableStateFlow<MutableList<Long>>(mutableListOf())
    val workingShipmentsList: StateFlow<List<Long>> = _workingShipmentsList.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _shipmentsScreenState.tryEmit(ShipmentsScreenState.Loading)
            _shipmentsScreenState.emitAll(
                repository.getAllShipments().stateIn(viewModelScope).map { list ->
                    if (list.isEmpty())
                        ShipmentsScreenState.NoData
                    else
                        ShipmentsScreenState.DataFetched(list)
                }
            )
        }
    }

    fun cancelShipment(shipment: Shipment) {
        _workingShipmentsList.update { it.toMutableList().apply { add(shipment.orderId) } }
        viewModelScope.launch {
            repository.cancelShipment(shipment.orderId)
            _workingShipmentsList.update { it.toMutableList().apply { remove(shipment.orderId) } }
        }
    }

    fun assignShipment(shipment: Shipment, transport: Transport) {
        _workingShipmentsList.update { it.toMutableList().apply { add(shipment.orderId) } }
        viewModelScope.launch {
            repository.assignTransportToShipment(shipment.orderId, transport)
            _workingShipmentsList.update { it.toMutableList().apply { remove(shipment.orderId) } }
        }
    }
}