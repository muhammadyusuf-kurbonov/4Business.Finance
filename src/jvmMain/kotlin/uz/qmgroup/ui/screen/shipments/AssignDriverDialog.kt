package uz.qmgroup.ui.screen.shipments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import uz.qmgroup.di.AppKoinComponent
import uz.qmgroup.models.Transport
import uz.qmgroup.ui.components.EmptyScreen
import uz.qmgroup.ui.components.LoadingScreen
import uz.qmgroup.ui.components.TransportComponent
import uz.qmgroup.viewModel.transports.TransportsScreenState
import uz.qmgroup.viewModel.transports.TransportsSearchViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectDriverDialog(
    onCloseRequest: () -> Unit,
    selectDriver: (Transport) -> Unit,
    viewModel: TransportsSearchViewModel = AppKoinComponent.transportsSearchViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        viewModel.searchData(searchQuery)
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.clear() }
    }

    val state by viewModel.searchState.collectAsState()

    Dialog(
        onCloseRequest = onCloseRequest,
        undecorated = true,
        transparent = true,
        onKeyEvent = {
            when (it.key) {
                Key.Escape -> {
                    onCloseRequest()
                }

                else -> {}
            }
            true
        }
    ) {
        Card(elevation = 12.dp, modifier = Modifier.padding(32.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Выберите гразовика", style = MaterialTheme.typography.h5)

                    IconButton(
                        onClick = onCloseRequest
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close select driver dialog")
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Поиск") }
                )

                when (val currentState = state) {
                    is TransportsScreenState.DataFetched -> {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(currentState.transports) {
                                TransportComponent(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        selectDriver(it)
                                        onCloseRequest()
                                    },
                                    transport = it
                                )
                            }
                        }
                    }

                    TransportsScreenState.Loading -> {
                        LoadingScreen(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    TransportsScreenState.NoData -> {
                        EmptyScreen(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}