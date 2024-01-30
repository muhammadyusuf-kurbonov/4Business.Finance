package uz.forbusiness.finance.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier

fun <T> defaultTransformer(value: T): String {
    return value.toString()
}

@Composable
fun <T : Any> SelectField(
    modifier: Modifier = Modifier,
    value: T?,
    onValueChanged: (T) -> Unit,
    options: List<T>,
    placeholder: String = "Select account",
    transformer: (T) -> String = ::defaultTransformer
) {
    val transform = rememberUpdatedState(transformer)

    val (optionsExpanded, setOptionsExpanded) = remember { mutableStateOf(false) }

    Box(modifier = modifier.clickable {
        setOptionsExpanded(!optionsExpanded)

        println("Select options shown")
    }) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                setOptionsExpanded(!optionsExpanded)

                println("Select options shown")
            },
        ) {
            Text(value?.let(transform.value) ?: placeholder)
        }

        DropdownMenu(
            expanded = optionsExpanded,
            onDismissRequest = { setOptionsExpanded(false) },
        ) {
            options.forEach {
                DropdownMenuItem(
                    onClick = {
                        onValueChanged(it)
                        setOptionsExpanded(false)
                    }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = transform.value(it)
                    )
                }
            }
        }
    }
}

