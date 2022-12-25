package uz.qmgroup.ui.screen.shipments

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.qmgroup.di.AppKoinComponent
import uz.qmgroup.ui.components.EmptyScreen
import uz.qmgroup.ui.components.LoadingScreen
import uz.qmgroup.ui.components.ShipmentComponent
import uz.qmgroup.viewModel.shipments.ShipmentsScreenState
import uz.qmgroup.viewModel.shipments.ShipmentsViewModel

@OptIn(ExperimentalAnimationApi::class)
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

    LaunchedEffect(currentWorkingShipments) {
        println("Current working shipments changed to $currentWorkingShipments")
    }

    Column(modifier = modifier.animateContentSize()) {
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp),
            text = "Грузы",
            style = MaterialTheme.typography.h5
        )

        when (val currentState = state) {
            is ShipmentsScreenState.DataFetched -> {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(450.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(currentState.orders) { shipment ->
                        ShipmentComponent(
                            modifier = Modifier,
                            shipment = shipment,
                            isInProgress = currentWorkingShipments.contains(shipment.orderId),
                            cancelShipment = {
                                viewModel.cancelShipment(shipment)
                            },
                            requestDriverSelect = {}
                        )
                    }
                }
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