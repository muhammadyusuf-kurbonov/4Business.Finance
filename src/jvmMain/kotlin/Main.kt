import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import uz.qmgroup.di.AppKoinComponent
import uz.qmgroup.ui.screen.app.AppScreen

fun main() = application {
    val viewModel = AppKoinComponent.appViewModel

    Window(onCloseRequest = ::exitApplication, title = "LogiAdmin", state = rememberWindowState(placement = WindowPlacement.Maximized)) {
        AppScreen(viewModel = viewModel)
    }
}
