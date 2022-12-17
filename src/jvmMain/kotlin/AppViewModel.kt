import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import screens.layouts.AppScreenState

class AppViewModel {
    private val _appScreenState = MutableStateFlow<AppScreenState>(AppScreenState.HomeScreen)
    val appScreenState = _appScreenState.asStateFlow()

    fun changeState(appScreenState: AppScreenState) {
        _appScreenState.tryEmit(appScreenState)
    }
}