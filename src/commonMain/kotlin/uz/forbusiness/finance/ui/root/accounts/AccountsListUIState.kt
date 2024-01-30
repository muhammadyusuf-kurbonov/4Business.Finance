package uz.forbusiness.finance.ui.root.accounts

import uz.forbusiness.finance.models.AccountWithBalance

sealed class AccountsListUIState {
    object Loading: AccountsListUIState()
    object NoData: AccountsListUIState()
    class DataFetched(val accounts: List<AccountWithBalance>): AccountsListUIState()
}