package uz.qmgroup.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.*


class PhoneNumberVisualTransformation(
    countryCode: String = Locale.getDefault().country
) : VisualTransformation {

    private val phoneNumberFormatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter(countryCode)

    override fun filter(text: AnnotatedString): TransformedText {
        val transformation = reformat(text)

        return TransformedText(AnnotatedString(transformation.formatted ?: ""), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return transformation.originalToTransformed[offset]
            }
            override fun transformedToOriginal(offset: Int): Int {
                return transformation.transformedToOriginal[offset]
            }
        })
    }

    private fun reformat(s: CharSequence): Transformation {
        phoneNumberFormatter.clear()

        var formatted: String? = null
        var lastNonSeparator = 0.toChar()

        s.forEach { char ->
            if (isNonSeparator(char)) {
                if (lastNonSeparator.code != 0) {
                    formatted = getFormattedNumber(lastNonSeparator)
                }
                lastNonSeparator = char
            }
        }

        if (lastNonSeparator.code != 0) {
            formatted = getFormattedNumber(lastNonSeparator)
        }
        val originalToTransformed = mutableListOf<Int>()
        val transformedToOriginal = mutableListOf<Int>()
        var specialCharsCount = 0
        formatted?.forEachIndexed { index, char ->
            if (!isNonSeparator(char)) {
                specialCharsCount++
            } else {
                originalToTransformed.add(index)
            }
            transformedToOriginal.add(index - specialCharsCount)
        }
        originalToTransformed.add(originalToTransformed.maxOrNull()?.plus(1) ?: 0)
        transformedToOriginal.add(transformedToOriginal.maxOrNull()?.plus(1) ?: 0)

        return Transformation(formatted, originalToTransformed, transformedToOriginal)
    }

    private fun getFormattedNumber(lastNonSeparator: Char): String? {
        return phoneNumberFormatter.inputDigit(lastNonSeparator)
    }

    private fun isNonSeparator(c: Char): Boolean {
        return c in '0'..'9' || c == '*' || c == '#' || c == '+' || c == 'N' || c == ';' || c == ','
    }

    private data class Transformation(
        val formatted: String?,
        val originalToTransformed: List<Int>,
        val transformedToOriginal: List<Int>
    )
}