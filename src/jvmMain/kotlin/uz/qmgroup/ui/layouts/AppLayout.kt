package uz.qmgroup.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import uz.qmgroup.ui.components.NavigationItem
import uz.qmgroup.ui.providers.LocalSnackbarProvider
import uz.qmgroup.ui.providers.ShowSnackbarDispatcher

@Composable
fun AppLayout(
    modifier: Modifier = Modifier,
    appScreenState: AppScreenState,
    navigate: (AppScreenState) -> Unit,
    openNewShipmentDialog: () -> Unit,
    content: @Composable () -> Unit
) {
    val snackbatHostState = remember { SnackbarHostState() }

    val showSnackbarDispatcher = ShowSnackbarDispatcher {
        snackbatHostState.showSnackbar(it)
    }

    CompositionLocalProvider(LocalSnackbarProvider provides showSnackbarDispatcher) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbatHostState) },
        ) {
            Row(modifier = modifier.padding(it)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(IntrinsicSize.Max).background(MaterialTheme.colors.surface)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colors.secondary)) {
                            append("Logi")
                        }
                        withStyle(SpanStyle(color = MaterialTheme.colors.onBackground)) {
                            append("Admin")
                        }
                    }, style = MaterialTheme.typography.h5, modifier = Modifier.padding(8.dp, 16.dp))

                    Divider()

                    NavigationItem(
                        selected = appScreenState == AppScreenState.HomeScreen,
                        onClick = { navigate(AppScreenState.HomeScreen) },
                        icon = Icons.Outlined.Home,
                        label = "Главная",
                    )
                    NavigationItem(
                        selected = appScreenState == AppScreenState.OrdersScreen,
                        onClick = { navigate(AppScreenState.OrdersScreen) },
                        icon = Icons.Outlined.Inventory2,
                        label = "Грузы"
                    )
                    NavigationItem(
                        selected = appScreenState == AppScreenState.TransportScreen,
                        onClick = { navigate(AppScreenState.TransportScreen) },
                        icon = Icons.Outlined.LocalShipping,
                        label = "Грузовики"
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = openNewShipmentDialog,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)

                        Text("Новый груз")
                    }
                }

                Box(modifier = Modifier.padding(32.dp, 16.dp)) {
                    content()
                }
            }
        }
    }
}