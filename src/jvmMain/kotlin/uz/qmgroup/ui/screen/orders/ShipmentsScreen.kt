package uz.qmgroup.ui.screen.orders

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
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
import uz.qmgroup.viewModel.orders.OrdersScreenState
import uz.qmgroup.viewModel.orders.OrdersViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShipmentsScreen(
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = AppKoinComponent.ordersViewModel
) {
    DisposableEffect(viewModel) {
        viewModel.loadData()

        onDispose { viewModel.clear() }
    }

    val state by viewModel.ordersScreenState.collectAsState()

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp),
            text = "Грузы",
            style = MaterialTheme.typography.h5
        )

        AnimatedContent(modifier = modifier.padding(8.dp), targetState = state::class) {
            when (val currentState = state) {
                is OrdersScreenState.DataFetched -> {
                    LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Adaptive(450.dp), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(currentState.orders) { order ->
                            ShipmentComponent(
                                modifier = Modifier,
                                shipment = order
                            )
                        }
                    }
                }

                OrdersScreenState.Loading -> {
                    LoadingScreen()
                }

                OrdersScreenState.NoData -> {
                    EmptyScreen()
                }
            }
        }
    }
}