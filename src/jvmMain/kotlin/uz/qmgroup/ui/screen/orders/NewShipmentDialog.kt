package uz.qmgroup.ui.screen.orders

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
import uz.qmgroup.ui.providers.LocalSnackbarProvider
import uz.qmgroup.viewModel.orders.OrderDialogState
import uz.qmgroup.viewModel.orders.OrderDialogViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewOrderDialog(
    onDismissRequest: () -> Unit,
    viewModel: OrderDialogViewModel = AppKoinComponent.orderDialogViewModel
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
        OrderDialogState.Default, OrderDialogState.SavePending -> {
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
                                        status = "Created",
                                        pickoffPlace = pickupAddress,
                                        destinationPlace = destinationAddress,
                                        price = price,
                                        author = "Diyorbek"
                                    )
                                )
                            }, enabled = currentState == OrderDialogState.Default) {
                                Text("Сохранить")
                            }
                        }
                    }
                }
            }
        }

        is OrderDialogState.SaveFailed -> {}
        OrderDialogState.SaveCompleted -> {
            val dispatcher = LocalSnackbarProvider.current
            LaunchedEffect(Unit) {
                dispatcher.dispatch("Новый груз добавлен")
                onDismissRequest()
            }
        }

        else -> {}
    }
}