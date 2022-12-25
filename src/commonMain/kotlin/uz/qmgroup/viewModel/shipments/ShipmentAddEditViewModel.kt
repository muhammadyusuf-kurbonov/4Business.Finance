package uz.qmgroup.viewModel.shipments

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.qmgroup.models.Shipment
import uz.qmgroup.repository.AppRepository
import uz.qmgroup.viewModel.base.BaseViewModel

class ShipmentAddEditViewModel(private val repository: AppRepository) : BaseViewModel() {
    private val _state = MutableStateFlow<ShipmentDialogState>(ShipmentDialogState.Default)
    val state = _state.asStateFlow()

    fun initialize() {
        _state.update { ShipmentDialogState.Default }
    }

    fun save(shipment: Shipment) {
        viewModelScope.launch {
            _state.update { ShipmentDialogState.SavePending }
            try {
                repository.createNewShipment(
                    shipment.note,
                    shipment.orderPrefix,
                    shipment.transportId,
                    shipment.status.name,
                    shipment.pickoffPlace,
                    shipment.destinationPlace,
                    shipment.price,
                    shipment.author
                )

                _state.update { ShipmentDialogState.SaveCompleted }
            } catch (e: Exception) {
                _state.update { ShipmentDialogState.SaveFailed(e.localizedMessage) }
            }
        }
    }

    fun fetch(id: Long) {
        viewModelScope.launch {
            _state.update { ShipmentDialogState.Fetching }
            val transport = repository.getTransportById(id)
            if (transport != null)
                _state.update { ShipmentDialogState.DataFetched(transport) }
        }
    }
}