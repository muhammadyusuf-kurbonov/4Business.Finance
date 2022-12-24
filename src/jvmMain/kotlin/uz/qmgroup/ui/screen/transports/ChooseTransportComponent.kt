package uz.qmgroup.ui.screen.transports

import androidx.compose.foundation.layout.Box
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import uz.qmgroup.models.Transport

@Composable
fun ChooseTransportComponent(
    modifier: Modifier = Modifier,
    selectedTransport: Transport?,
    onTransportSelect: (Transport?) -> Unit,
    onNewTransportRequest: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    if (selectedTransport == null) {
        Box {
            OutlinedTextField(
                modifier = modifier,
                value = searchQuery,
                onValueChange = { searchQuery = it },
                singleLine = true
            )

        }
    }
}