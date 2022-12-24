package uz.qmgroup.ui.screen.transports

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import uz.qmgroup.models.TransportType
import uz.qmgroup.utils.PhoneNumberVisualTransformation

val transportTypeLabels = mapOf(
    TransportType.TENTOVKA to "Тентовка",
    TransportType.REFRIGERATOR_MODE to "РЕФ с режимом",
    TransportType.REFRIGERATOR_NO_MODE to "РЕФ без режима"
)

@Composable
fun TransportForm(
    modifier: Modifier = Modifier,
    driverName: String,
    onDriverNameChange: (String) -> Unit,
    driverPhone: String,
    onDriverPhoneChange: (String) -> Unit,
    transportNumber: String,
    onTransportNumberChange: (String) -> Unit,
    transportType: TransportType,
    onTransportTypeChange: (TransportType) -> Unit,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        OutlinedTextField(
            value = driverName,
            onValueChange = onDriverNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Driver name")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = driverPhone,
            onValueChange = onDriverPhoneChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Driver phone")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            visualTransformation = PhoneNumberVisualTransformation()
        )

        OutlinedTextField(
            value = transportNumber,
            onValueChange = onTransportNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Transport number")
            },
            singleLine = true
        )

        Box(modifier = Modifier) {
            OutlinedTextField(
                value = transportTypeLabels[transportType] ?: transportType.name,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().onFocusChanged {
                    dropdownExpanded = it.isFocused
                },
                label = {
                    Text("Type")
                },
                readOnly = true,
                singleLine = true
            )


            if (dropdownExpanded) {
                Popup(onDismissRequest = { dropdownExpanded = false }) {
                    Card {
                        LazyColumn(modifier = Modifier.padding(8.dp)) {
                            TransportType.values().forEach {
                                item(key = it.stringValue) {
                                    Row (modifier = Modifier.clickable {
                                        onTransportTypeChange(it); dropdownExpanded = false
                                    }) {
                                        Text(transportTypeLabels[it] ?: it.name)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTransportForm() {
    TransportForm(
        modifier = Modifier.fillMaxSize(),
        driverName = "", onDriverNameChange = {},
        driverPhone = "+998913975538", onDriverPhoneChange = {},
        transportNumber = "40L533FA", onTransportNumberChange = {},
        transportType = TransportType.TENTOVKA, onTransportTypeChange = {}
    )
}