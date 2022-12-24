package uz.qmgroup.ui.components

import androidx.compose.runtime.*
import uz.qmgroup.utils.NumberFormatVisualTransformer
import java.text.NumberFormat
import java.text.ParseException

class NumberTextFieldProps(
    val valueAsString: String,
    val onValueChanged: (String) -> Unit,
    val visualTransformer: NumberFormatVisualTransformer
)

@Composable
fun NumberTextFieldWrapper(
    value: Number,
    onValueChanged: (Number) -> Unit,
    content: @Composable (NumberTextFieldProps) -> Unit
) {
    val simpleNumberFormatter: NumberFormat = remember {
        NumberFormat.getNumberInstance().apply {
            this.minimumFractionDigits = 2
        }
    }

    val updatedValue by rememberUpdatedState(value)
    val valueAsString by derivedStateOf {
        simpleNumberFormatter.format(updatedValue)
    }

    val onStringValueChanged: (String) -> Unit = remember {
        {
            try {
                onValueChanged(simpleNumberFormatter.parse(it.trim().trim('.', ',')))
            } catch (e: ParseException) {
                onValueChanged(0.0)
            }
        }
    }

    val props by derivedStateOf {
        NumberTextFieldProps(
            valueAsString = valueAsString,
            onValueChanged = onStringValueChanged,
            visualTransformer = NumberFormatVisualTransformer(originalValue = value)
        )
    }
    content(props)
}