package uz.qmgroup.ui.screen.shipments

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.qmgroup.di.AppKoinComponent
import uz.qmgroup.models.Shipment
import uz.qmgroup.ui.components.EmptyScreen
import uz.qmgroup.ui.components.LoadingScreen
import uz.qmgroup.ui.components.ShipmentComponent
import uz.qmgroup.viewModel.shipments.ShipmentsScreenState
import uz.qmgroup.viewModel.shipments.ShipmentsViewModel

@Composable
fun ShipmentsScreen(
    modifier: Modifier = Modifier,
    viewModel: ShipmentsViewModel = AppKoinComponent.shipmentsViewModel
) {
    DisposableEffect(viewModel) {
        viewModel.loadData()

        onDispose { viewModel.clear() }
    }

    val state by viewModel.shipmentsScreenState.collectAsState()
    val currentWorkingShipments by viewModel.workingShipmentsList.collectAsState()
    var currentActiveShipment by remember { mutableStateOf<Shipment?>(null) }
    var showSelectDriverDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.animateContentSize()) {
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp),
            text = "Грузы",
            style = MaterialTheme.typography.h5
        )

        when (val currentState = state) {
            is ShipmentsScreenState.DataFetched -> {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize().padding(),
                    columns = GridCells.Adaptive(450.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(currentState.orders) { shipment ->
                        ShipmentComponent(
                            shipment = shipment,
                            isInProgress = currentWorkingShipments.contains(shipment.orderId),
                            cancelShipment = {
                                viewModel.cancelShipment(shipment)
                            },
                            requestDriverSelect = {
                                currentActiveShipment = shipment
                                showSelectDriverDialog = true
                            }
                        )
                    }
                }

                if (showSelectDriverDialog)
                    SelectDriverDialog(
                        onCloseRequest = { showSelectDriverDialog = false },
                        selectDriver = {
                            val shipment = currentActiveShipment

                            if (shipment != null)
                                viewModel.assignShipment(
                                    shipment = shipment,
                                    transport = it
                                )
                        }
                    )
            }

            ShipmentsScreenState.Loading -> {
                LoadingScreen()
            }

            ShipmentsScreenState.NoData -> {
                EmptyScreen()
            }
        }
    }
}