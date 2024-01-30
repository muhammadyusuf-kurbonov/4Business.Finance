package uz.forbusiness.finance.ui.root.transactions.dialogs.add

import uz.forbusiness.finance.models.Account

sealed class AddTransactionUIState {
    object None: AddTransactionUIState()

    object Saving: AddTransactionUIState()

    object LoadingAccounts: AddTransactionUIState()

    data class WaitingForInput(val accounts: List<Account>): AddTransactionUIState()

    object SaveCompleted: AddTransactionUIState()
}