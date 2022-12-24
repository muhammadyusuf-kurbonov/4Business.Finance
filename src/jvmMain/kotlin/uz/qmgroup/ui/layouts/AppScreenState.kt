package uz.qmgroup.ui.layouts

sealed class AppScreenState {
    object HomeScreen: AppScreenState()
    object TransportScreen : AppScreenState()
    object OrdersScreen : AppScreenState()
}
