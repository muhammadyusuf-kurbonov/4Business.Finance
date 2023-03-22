package uz.forbusiness.finance.ui.screen.app

import androidx.compose.animation.Crossfade
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import uz.forbusiness.finance.ui.layouts.AppLayout
import uz.forbusiness.finance.ui.layouts.AppScreenState
import uz.forbusiness.finance.ui.screen.home.HomeScreenContent
import uz.forbusiness.finance.ui.screen.transactions.TransactionsScreen
import uz.forbusiness.finance.ui.theme.App4BusinessFinanceTheme


@Composable
@Preview
fun AppScreen(viewModel: AppViewModel) {
    val screenState by viewModel.appScreenState.collectAsState()
    var showNewShipmentDialog by remember { mutableStateOf(false) }
    App4BusinessFinanceTheme(useDarkTheme = false) {
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

                    AppScreenState.TransactionsScreen -> {
                        TransactionsScreen(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
