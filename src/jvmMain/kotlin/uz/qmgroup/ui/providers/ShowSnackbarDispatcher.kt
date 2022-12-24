package uz.qmgroup.ui.providers

fun interface ShowSnackbarDispatcher {
    suspend fun dispatch(text: String)
}