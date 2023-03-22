package uz.forbusiness.finance.ui.screen.app

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.forbusiness.finance.ui.layouts.AppScreenState
import uz.forbusiness.finance.viewModel.base.BaseViewModel

class AppViewModel : BaseViewModel() {
    private val _appScreenState = MutableStateFlow<AppScreenState>(AppScreenState.HomeScreen)
    val appScreenState = _appScreenState.asStateFlow()

    fun changeState(appScreenState: AppScreenState) {
        _appScreenState.tryEmit(appScreenState)
    }
}