package uz.forbusiness.finance.viewModel.accounts

import uz.forbusiness.finance.models.Transaction

sealed class AccountDialogState {
    object Default: AccountDialogState()

    object Fetching: AccountDialogState()

    class DataFetched(val transaction: Transaction): AccountDialogState()

    object SavePending: AccountDialogState()

    object SaveCompleted: AccountDialogState()

    object SaveFailed : AccountDialogState()
}
