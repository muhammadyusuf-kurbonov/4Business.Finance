package uz.forbusiness.finance.viewModel.accounts

import uz.forbusiness.finance.models.Account

sealed class AccountsScreenState {
    object Loading: AccountsScreenState()
    object NoData: AccountsScreenState()
    class DataFetched(val transactions: List<Account>): AccountsScreenState()
}