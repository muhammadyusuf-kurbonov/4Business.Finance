package uz.qmgroup.viewModel.transports

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.qmgroup.models.Transport
import uz.qmgroup.repository.AppRepository

class TransportDialogViewModel(private val repository: AppRepository): BaseViewModel() {
    private val _state = MutableStateFlow<TransportDialogState>(TransportDialogState.Default)
    val state = _state.asStateFlow()

    fun initialize() {
        _state.update { TransportDialogState.Default }
    }

    fun save(transport: Transport) {
        viewModelScope.launch {
            _state.update { TransportDialogState.SavePending }
            try {
                repository.createNewTransport(
                    transport.driverName,
                    transport.driverPhone,
                    transport.transportNumber,
                    transport.type
                )
                _state.update { TransportDialogState.SaveCompleted }
            } catch (e: Exception) {
                _state.update { TransportDialogState.SaveFailed(e.localizedMessage) }
            }
        }
    }

    fun fetch(id: Long) {
        viewModelScope.launch {
            _state.update { TransportDialogState.Fetching }
            val transport = repository.getTransportById(id)
            if (transport != null)
                _state.update { TransportDialogState.DataFetched(transport) }
        }
    }
}