package uz.qmgroup.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.qmgroup.models.Shipment
import uz.qmgroup.models.ShipmentStatus
import java.text.NumberFormat

val statusLabels = mapOf(
    ShipmentStatus.CANCELLED to "Отменен",
    ShipmentStatus.CREATED to "Открыт",
    ShipmentStatus.ASSIGNED to "Назначен",
    ShipmentStatus.UNKNOWN to "Неизвестно"
)

@Composable
fun ShipmentComponent(
    modifier: Modifier = Modifier,
    shipment: Shipment,
    isInProgress: Boolean,
    cancelShipment: () -> Unit,
    requestDriverSelect: () -> Unit
) {
    Card(modifier = modifier.width(IntrinsicSize.Min), shape = RectangleShape, elevation = 1.dp) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "${shipment.pickoffPlace} - ${shipment.destinationPlace}",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )

                val color = when (shipment.status) {
                    ShipmentStatus.CANCELLED -> MaterialTheme.colors.error
                    ShipmentStatus.CREATED -> MaterialTheme.colors.primaryVariant
                    else -> MaterialTheme.colors.secondary
                }

                CompositionLocalProvider(LocalContentColor provides color) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.size(15.dp).clip(CircleShape).background(LocalContentColor.current))

                        Text(
                            statusLabels[shipment.status] ?: shipment.status.name,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Номер груза", style = MaterialTheme.typography.caption)
                    Text(
                        "${shipment.orderPrefix}${shipment.orderId}",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("Номер грузовика", style = MaterialTheme.typography.caption)
                    if (shipment.transport != null) {
                        Text(
                            shipment.transport.transportNumber,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text("- -", style = MaterialTheme.typography.body1)
                    }

                    Spacer(Modifier.height(8.dp))

                    Text("Стоимость", style = MaterialTheme.typography.caption)
                    Text(
                        "${NumberFormat.getNumberInstance().format(shipment.price)} сум",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("Оператор", style = MaterialTheme.typography.caption)
                    Text(shipment.author, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
                }
//                Image(
//                    modifier = Modifier.weight(4f).aspectRatio(1f),
//                    painter = painterResource("tent-truck.webp"),
//                    contentDescription = "Tent-truck",
//                    contentScale = ContentScale.Inside,
//                )

            }

            if (!isInProgress) {
                when (shipment.status) {
                    ShipmentStatus.CANCELLED -> {
                        Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                            Text("Отменен")
                        }
                    }

                    else -> {
                        Row(modifier = Modifier.align(Alignment.End)) {
                            ConfirmButtonWrapper(
                                onConfirmed = cancelShipment,
                                message = "Вы точно хотите отменить этот груз?",
                            ) {
                                TextButton(onClick = it.onClick, modifier = Modifier.weight(1f)) {
                                    Text("Отменить")
                                }
                            }
                            Button(onClick = requestDriverSelect, modifier = Modifier.weight(1f)) {
                                Text("Назначить водителя")
                            }
                        }
                    }
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Preview
@Composable
fun PreviewShipmentComponent() {
    MaterialTheme {
        ShipmentComponent(
            modifier = Modifier.fillMaxWidth(), Shipment(
                orderId = 15,
                note = "Test shipment",
                orderPrefix = "CC",
                transportId = null,
                status = ShipmentStatus.CREATED,
                pickoffPlace = "Tashkent",
                destinationPlace = "Chiroqchi",
                price = 5000000.0,
                author = "Diyorbek",
                transport = null,
            ),
            isInProgress = false,
            cancelShipment = {},
            requestDriverSelect = {}
        )
    }
}