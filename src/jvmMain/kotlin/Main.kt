import androidx.compose.animation.Crossfade
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import screens.layouts.AppLayout
import screens.layouts.AppScreenState
import screens.transports.TransportsScreen
import uz.qmgroup.di.AppKoinComponent

@Composable
@Preview
fun App(viewModel: AppViewModel) {
    val screenState by viewModel.appScreenState.collectAsState()

    MaterialTheme {
        AppLayout(
            modifier = Modifier.fillMaxSize(),
            appScreenState = screenState,
            navigate = viewModel::changeState
        ) {
            Crossfade(targetState = screenState) { state ->
                when(state) {
                    AppScreenState.HomeScreen -> {
                        Text("There will be home screen")
                    }
                    AppScreenState.TransportScreen -> {
                        TransportsScreen(modifier = Modifier.fillMaxSize())
                    }
                    AppScreenState.OrdersScreen -> {
                        Text("All orders")
                    }
                }
            }
        }
    }
}

fun main() = application {
    val viewModel = AppKoinComponent.appViewModel

    Window(onCloseRequest = ::exitApplication, title = "Logistics Control System - LCS", state = rememberWindowState(placement = WindowPlacement.Maximized)) {
        App(viewModel = viewModel)
    }
}
