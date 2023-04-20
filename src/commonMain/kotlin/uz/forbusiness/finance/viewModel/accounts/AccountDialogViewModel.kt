package uz.forbusiness.finance.viewModel.accounts

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.forbusiness.finance.models.Account
import uz.forbusiness.finance.repository.AppRepository
import uz.forbusiness.finance.viewModel.base.BaseViewModel

class AccountDialogViewModel(private val repository: AppRepository) : BaseViewModel() {
    private val _state = MutableStateFlow<AccountDialogState>(AccountDialogState.Default)
    val state = _state.asStateFlow()

    fun initialize() {
        _state.update { AccountDialogState.Default }
    }

    fun save(account: Account) {
        viewModelScope.launch {
            _state.update { AccountDialogState.SavePending }
            try {
                repository.addNewAccount(account)

                _state.update { AccountDialogState.SaveCompleted }
            } catch (e: Exception) {
                _state.update { AccountDialogState.SaveFailed }
            }
        }
    }
}