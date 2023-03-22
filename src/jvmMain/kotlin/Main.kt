import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.skeptick.libres.LibresSettings
import uz.forbusiness.finance.di.AppKoinComponent
import uz.forbusiness.finance.ui.screen.app.AppScreen

fun main() = application {
    val viewModel = AppKoinComponent.appViewModel

    LibresSettings.languageCode = "ru"

    Window(onCloseRequest = ::exitApplication, title = "4Business.Finance", state = rememberWindowState(placement = WindowPlacement.Maximized)) {
        AppScreen(viewModel = viewModel)
    }
}
