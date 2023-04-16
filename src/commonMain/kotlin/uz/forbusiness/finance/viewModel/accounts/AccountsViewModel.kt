package uz.forbusiness.finance.viewModel.accounts

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.forbusiness.finance.repository.AppRepository
import uz.forbusiness.finance.viewModel.base.BaseViewModel

class AccountsViewModel(private val repository: AppRepository): BaseViewModel() {
    private val _accountsScreenState = MutableStateFlow<AccountsScreenState>(AccountsScreenState.Loading)
    val accountsScreenState = _accountsScreenState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _accountsScreenState.tryEmit(AccountsScreenState.Loading)
            _accountsScreenState.emitAll(
                repository.getAllAccounts().stateIn(viewModelScope).map { list ->
                    if (list.isEmpty())
                        AccountsScreenState.NoData
                    else
                        AccountsScreenState.DataFetched(list)
                }
            )
        }
    }
}