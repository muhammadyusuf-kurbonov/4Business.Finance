package screens.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import providers.LocalSnackbarProvider
import providers.ShowSnackbarDispatcher

@Composable
fun AppLayout(
    modifier: Modifier = Modifier,
    appScreenState: AppScreenState,
    navigate: (AppScreenState) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val showSnackbarDispatcher = ShowSnackbarDispatcher {
        scaffoldState.snackbarHostState.showSnackbar(it)
    }

    CompositionLocalProvider(LocalSnackbarProvider provides showSnackbarDispatcher) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text("LCS - Logistics Control System")
                })
            },
            scaffoldState = scaffoldState
        ) {
            Row(modifier = modifier) {
                NavigationRail {
                    NavigationRailItem(
                        selected = appScreenState == AppScreenState.HomeScreen,
                        onClick = { navigate(AppScreenState.HomeScreen) },
                        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
                        label = { Text("Home") },
                    )
                    NavigationRailItem(
                        selected = appScreenState == AppScreenState.OrdersScreen,
                        onClick = { navigate(AppScreenState.OrdersScreen) },
                        icon = { Icon(imageVector = Icons.Default.Inventory2, contentDescription = null) },
                        label = { Text("Orders") }
                    )
                    NavigationRailItem(
                        selected = appScreenState == AppScreenState.TransportScreen,
                        onClick = { navigate(AppScreenState.TransportScreen) },
                        icon = { Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null) },
                        label = { Text("Transports") }
                    )
                }

                Column {
                    content()
                }
            }
        }
    }
}