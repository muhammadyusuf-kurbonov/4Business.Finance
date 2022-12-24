package uz.qmgroup.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.qmgroup.models.Shipment
import java.text.NumberFormat

@Composable
fun ShipmentComponent(modifier: Modifier = Modifier, shipment: Shipment) {
    Card(modifier = modifier.width(IntrinsicSize.Min)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "${shipment.pickoffPlace} - ${shipment.destinationPlace}",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(15.dp).clip(CircleShape).background(MaterialTheme.colors.secondary))

                    Text(shipment.status, style = MaterialTheme.typography.body2)
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
                    Text("75K105LA", style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)

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
                status = "On way",
                pickoffPlace = "Tashkent",
                destinationPlace = "Chiroqchi",
                price = 5000000.0,
                author = "Diyorbek",
                transport = null
            )
        )
    }
}