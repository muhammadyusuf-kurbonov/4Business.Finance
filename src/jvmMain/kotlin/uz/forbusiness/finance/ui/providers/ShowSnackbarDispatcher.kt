package uz.forbusiness.finance.ui.providers

fun interface ShowSnackbarDispatcher {
    suspend fun dispatch(text: String)
}