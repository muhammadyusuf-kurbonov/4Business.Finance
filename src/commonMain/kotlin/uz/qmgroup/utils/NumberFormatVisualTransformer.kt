package uz.qmgroup.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import kotlin.math.min

class NumberFormatVisualTransformer(
    private val numberFormat: NumberFormat = NumberFormat.getNumberInstance().apply { minimumFractionDigits = 1 },
    private val originalValue: Number
) : VisualTransformation {
    fun calculateNewCaretPosition(originalString: String, formattedString: String, oldPosition: Int): Int {
        val needSubstring = originalString.take(oldPosition)
        var offsetMove = 0

        needSubstring.forEachIndexed { index, character ->
            val currentOffset = index + offsetMove
            while (currentOffset < formattedString.length && formattedString[currentOffset] != character) {
                offsetMove++
            }
        }
        return oldPosition + offsetMove
    }

    fun calculateOriginalCaretPosition(originalString: String, formattedString: String, newPosition: Int): Int {
        val needSubstring = formattedString.take(newPosition)
        var offsetMove = 0

        needSubstring.forEachIndexed { index, character ->
            while (
                (index - offsetMove) < originalString.length &&
                (index - offsetMove) >= 0 &&
                originalString[index - offsetMove] != character
            ) {
                offsetMove++
            }
        }
        return newPosition - offsetMove
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val formattedNumber = numberFormat.format(originalValue)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return min(calculateNewCaretPosition(text.text, formattedNumber, offset), formattedNumber.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return min(calculateOriginalCaretPosition(text.text, formattedNumber, offset), text.length)
            }

        }

        return TransformedText(AnnotatedString(formattedNumber, text.spanStyles), offsetMapping)
    }

}