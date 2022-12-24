package uz.qmgroup.utils
import java.text.NumberFormat
import kotlin.test.*

class NumberFormatVisualTransformerTest {
    @Test
    fun calculateNewCaretOffset() {
        println((123456789).toString())
        println((123459f).toString())
        println((123456789.0).toString())
        println(NumberFormat.getNumberInstance().apply {
            this.minimumFractionDigits = 1
        }.format("123456789.0".toDouble()))
    }

    @Test
    fun calculateOriginalCaretPosition() {

    }

    @Test
    fun filter() {
    }
}