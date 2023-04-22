package uz.forbusiness.finance.viewModel.transports

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.models.Transaction
import uz.forbusiness.finance.repository.AppRepository
import uz.forbusiness.finance.viewModel.base.BaseViewModel

class TransactionDialogViewModel(private val repository: AppRepository) : BaseViewModel() {
    private val _state = MutableStateFlow<TransportDialogState>(TransportDialogState.Default)
    val state = _state.asStateFlow()

    fun initialize() {
        _state.update { TransportDialogState.Default }
    }

    suspend fun fetchAccounts(): List<Account> {
        return repository.getAllAccounts().first()
    }

    fun save(transaction: Transaction) {
        viewModelScope.launch {
            _state.update { TransportDialogState.SavePending }
            try {
                repository.insertNewTransaction(
                    transaction.note,
                    transaction.amount,
                    transaction.dateTime,
                    transaction.fromAccount,
                    transaction.toAccount
                )
                _state.update { TransportDialogState.SaveCompleted }
            } catch (e: Exception) {
                _state.update { TransportDialogState.SaveFailed }
            }
        }
    }
}