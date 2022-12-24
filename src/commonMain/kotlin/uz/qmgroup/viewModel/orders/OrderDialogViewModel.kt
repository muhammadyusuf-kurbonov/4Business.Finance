package uz.qmgroup.viewModel.orders

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.qmgroup.models.Shipment
import uz.qmgroup.repository.AppRepository
import uz.qmgroup.viewModel.base.BaseViewModel

class OrderDialogViewModel(private val repository: AppRepository) : BaseViewModel() {
    private val _state = MutableStateFlow<OrderDialogState>(OrderDialogState.Default)
    val state = _state.asStateFlow()

    fun initialize() {
        _state.update { OrderDialogState.Default }
    }

    fun save(shipment: Shipment) {
        viewModelScope.launch {
            _state.update { OrderDialogState.SavePending }
            try {
                repository.createNewShipment(
                    shipment.note,
                    shipment.orderPrefix,
                    shipment.transportId,
                    shipment.status,
                    shipment.pickoffPlace,
                    shipment.destinationPlace,
                    shipment.price,
                    shipment.author
                )

                _state.update { OrderDialogState.SaveCompleted }
            } catch (e: Exception) {
                _state.update { OrderDialogState.SaveFailed(e.localizedMessage) }
            }
        }
    }

    fun fetch(id: Long) {
        viewModelScope.launch {
            _state.update { OrderDialogState.Fetching }
            val transport = repository.getTransportById(id)
            if (transport != null)
                _state.update { OrderDialogState.DataFetched(transport) }
        }
    }
}