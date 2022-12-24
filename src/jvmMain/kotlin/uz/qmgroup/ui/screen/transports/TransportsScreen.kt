package uz.qmgroup.ui.screen.transports

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.qmgroup.di.AppKoinComponent
import uz.qmgroup.ui.components.EmptyScreen
import uz.qmgroup.ui.components.LoadingScreen
import uz.qmgroup.ui.components.TransportComponent
import uz.qmgroup.viewModel.transports.TransportsScreenState
import uz.qmgroup.viewModel.transports.TransportsViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransportsScreen(
    modifier: Modifier = Modifier,
    viewModel: TransportsViewModel = AppKoinComponent.transportsViewModel
) {
    DisposableEffect(viewModel) {
        viewModel.loadData()

        onDispose { viewModel.clear() }
    }

    val state by viewModel.transportScreenState.collectAsState()
    var dialogState by remember {
        mutableStateOf(false)
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            ExtendedFloatingActionButton(
                onClick = { dialogState = true },
                text = { Text("New transport") },
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.padding(16.dp)
            )
        }

        AnimatedContent(modifier = modifier.padding(8.dp), targetState = state::class) {
            when (val currentState = state) {
                is TransportsScreenState.DataFetched -> {
                    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(currentState.transports) { transport ->
                            TransportComponent(modifier = Modifier.fillMaxWidth().clickable {

                            }, transport = transport)
                            Divider()
                        }
                    }
                }

                TransportsScreenState.Loading -> {
                    LoadingScreen()
                }

                TransportsScreenState.NoData -> {
                    EmptyScreen()
                }
            }
        }
    }

    if (dialogState) {
        NewTransportDialog(
            onDismissRequest = { dialogState = false }
        )
    }
}