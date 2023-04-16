package uz.forbusiness.finance.ui.layouts

sealed class AppScreenState {
    object HomeScreen: AppScreenState()
    object TransactionsScreen : AppScreenState()
    object AccountsScreen : AppScreenState()
}
