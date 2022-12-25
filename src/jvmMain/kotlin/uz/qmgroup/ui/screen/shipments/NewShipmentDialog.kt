package uz.qmgroup.ui.screen.shipments

import androidx.compose.foundation.layout.*
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
import uz.qmgroup.models.Shipment
import uz.qmgroup.models.ShipmentStatus
import uz.qmgroup.ui.providers.LocalSnackbarProvider
import uz.qmgroup.viewModel.shipments.ShipmentDialogState
import uz.qmgroup.viewModel.shipments.ShipmentAddEditViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewOrderDialog(
    onDismissRequest: () -> Unit,
    viewModel: ShipmentAddEditViewModel = AppKoinComponent.shipmentAddEditViewModel
) {
    DisposableEffect(Unit) {
        viewModel.initialize()

        onDispose { viewModel.clear() }
    }

    val currentState by viewModel.state.collectAsState()
    val (pickupAddress, onPickupAddressChange) = remember { mutableStateOf("") }
    val (destinationAddress, onDestinationAddressChange) = remember { mutableStateOf("") }
    val (orderPrefix, onOrderPrefixChange) = remember { mutableStateOf("") }
    val (price, onPriceChange) = remember { mutableStateOf(0.0) }

    when (currentState) {
        ShipmentDialogState.Default, ShipmentDialogState.SavePending -> {
            Dialog(
                onCloseRequest = onDismissRequest,
                state = rememberDialogState(
                    height = Dp.Unspecified
                ),
                title = "Новый заказ",
                resizable = false,
                onKeyEvent = {
                    when (it.key) {
                        Key.Escape -> onDismissRequest()
                    }
                    false
                },
            ) {
                Surface {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ShipmentForm(
                            modifier = Modifier.fillMaxWidth(),
                            pickupAddress = pickupAddress,
                            onPickupAddressChange = onPickupAddressChange,
                            destinationAddress = destinationAddress,
                            onDestinationAddressChange = onDestinationAddressChange,
                            orderType = orderPrefix,
                            onOrderTypeChanged = onOrderPrefixChange,
                            price = price,
                            onPriceChanged = onPriceChange
                        )

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            TextButton(onClick = onDismissRequest) {
                                Text("Отмена")
                            }

                            TextButton(onClick = {
                                viewModel.save(
                                    Shipment(
                                        orderId = 0,
                                        orderPrefix = orderPrefix,
                                        note = "",
                                        transportId = 0,
                                        status = ShipmentStatus.CREATED,
                                        pickoffPlace = pickupAddress,
                                        destinationPlace = destinationAddress,
                                        price = price,
                                        author = "Diyorbek",
                                        transport = null
                                    )
                                )
                            }, enabled = currentState == ShipmentDialogState.Default) {
                                Text("Сохранить")
                            }
                        }
                    }
                }
            }
        }

        is ShipmentDialogState.SaveFailed -> {}
        ShipmentDialogState.SaveCompleted -> {
            val dispatcher = LocalSnackbarProvider.current
            LaunchedEffect(Unit) {
                dispatcher.dispatch("Новый груз добавлен")
                onDismissRequest()
            }
        }

        else -> {}
    }
}