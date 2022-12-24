package uz.qmgroup.ui.screen.app

import androidx.compose.animation.Crossfade
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import uz.qmgroup.ui.layouts.AppLayout
import uz.qmgroup.ui.layouts.AppScreenState
import uz.qmgroup.ui.screen.home.HomeScreenContent
import uz.qmgroup.ui.screen.orders.NewOrderDialog
import uz.qmgroup.ui.screen.orders.ShipmentsScreen
import uz.qmgroup.ui.screen.transports.TransportsScreen
import uz.qmgroup.ui.theme.LCSAppTheme


@Composable
@Preview
fun AppScreen(viewModel: AppViewModel) {
    val screenState by viewModel.appScreenState.collectAsState()
    var showNewShipmentDialog by remember { mutableStateOf(false) }
    LCSAppTheme(useDarkTheme = false) {
        AppLayout(
            modifier = Modifier.fillMaxSize(),
            appScreenState = screenState,
            navigate = viewModel::changeState,
            openNewShipmentDialog = { showNewShipmentDialog = true }
        ) {
            Crossfade(targetState = screenState) { state ->
                when (state) {
                    AppScreenState.HomeScreen -> {
                        HomeScreenContent(modifier = Modifier.fillMaxSize())
                    }

                    AppScreenState.TransportScreen -> {
                        TransportsScreen(modifier = Modifier.fillMaxSize())
                    }

                    AppScreenState.OrdersScreen -> {
                        ShipmentsScreen(modifier = Modifier.fillMaxSize())
                    }
                }
            }

            if (showNewShipmentDialog)
                NewOrderDialog(onDismissRequest = { showNewShipmentDialog = false })
        }
    }
}
