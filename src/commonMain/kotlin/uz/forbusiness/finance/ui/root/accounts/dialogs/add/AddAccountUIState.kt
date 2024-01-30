package uz.forbusiness.finance.ui.root.accounts.dialogs.add

sealed class AddAccountUIState {
    object None: AddAccountUIState()

    object Saving: AddAccountUIState()

    object WaitingForInput: AddAccountUIState()

    object SaveCompleted: AddAccountUIState()
}