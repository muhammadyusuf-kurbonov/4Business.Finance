package uz.qmgroup.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.qmgroup.models.Transport

@Composable
fun TransportComponent(modifier: Modifier = Modifier, transport: Transport) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Outlined.LocalShipping, contentDescription = null)

        Column {
            Text(transport.driverName, style = MaterialTheme.typography.body1)
            Text(transport.transportNumber, style = MaterialTheme.typography.body2)
        }
    }
}