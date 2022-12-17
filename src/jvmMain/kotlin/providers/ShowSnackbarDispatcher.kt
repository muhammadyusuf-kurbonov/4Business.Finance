package providers

fun interface ShowSnackbarDispatcher {
    suspend fun dispatch(text: String)
}