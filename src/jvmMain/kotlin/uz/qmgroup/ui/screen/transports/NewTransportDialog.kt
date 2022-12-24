package uz.qmgroup.ui.screen.transports

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import uz.qmgroup.di.AppKoinComponent
import uz.qmgroup.models.Transport
import uz.qmgroup.models.TransportType
import uz.qmgroup.ui.providers.LocalSnackbarProvider
import uz.qmgroup.viewModel.transports.TransportDialogState
import uz.qmgroup.viewModel.transports.TransportDialogViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun NewTransportDialog(
    onDismissRequest: () -> Unit,
    viewModel: TransportDialogViewModel = AppKoinComponent.transportDialogViewModel
) {
    val (driverName, onDriverNameChange) = remember { mutableStateOf("") }
    val (driverPhone, onDriverPhoneChange) = remember { mutableStateOf("") }
    val (transportNumber, onTransportNumberChange) = remember { mutableStateOf("") }
    val (transportType, onTransportTypeChange) = remember { mutableStateOf(TransportType.TENTOVKA) }

    DisposableEffect(Unit) {
        viewModel.initialize()

        onDispose { viewModel.clear() }
    }

    val currentState by viewModel.state.collectAsState()

    when (currentState) {
        TransportDialogState.Default, TransportDialogState.SavePending -> {
            Dialog(
                onCloseRequest = onDismissRequest,
                state = rememberDialogState(
                    height = Dp.Unspecified
                ),
                title = "Новый транспорт",
                resizable = false,
                onKeyEvent = {
                    when (it.key) {
                        Key.Escape -> onDismissRequest()
                    }
                    false
                }
            ) {
                AnimatedContent(currentState) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TransportForm(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            driverName = driverName,
                            onDriverNameChange = onDriverNameChange,
                            driverPhone = driverPhone,
                            onDriverPhoneChange = onDriverPhoneChange,
                            transportNumber = transportNumber,
                            onTransportNumberChange = onTransportNumberChange,
                            transportType = transportType,
                            onTransportTypeChange = onTransportTypeChange
                        )

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = onDismissRequest) {
                                Text("Отмена")
                            }

                            TextButton(onClick = {
                                viewModel.save(
                                    Transport(
                                        transportId = 0,
                                        driverName = driverName,
                                        driverPhone = driverPhone,
                                        transportNumber = transportNumber,
                                        type = transportType
                                    )
                                )
                            }, enabled = it == TransportDialogState.Default) {
                                Text("Сохранить")
                            }
                        }
                    }
                }
            }
        }
        is TransportDialogState.SaveFailed -> {}
        TransportDialogState.SaveCompleted -> {
            val dispatcher = LocalSnackbarProvider.current
            LaunchedEffect(Unit) {
                dispatcher.dispatch("Новый транспорт сохранён")
                onDismissRequest()
            }
        }
        else -> {}
    }
}