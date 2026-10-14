import com.apadmi.mockzilla.codegen.formatSource
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatSourceTest {
    @Test
    fun `kotlin output is formatted in process`() {
        assertEquals("val a = 1\n", formatSource("out.kt", "val   a=1"))
    }

    @Test
    fun `other output is untouched`() {
        assertEquals("a   b", formatSource("out.dart", "a   b"))
    }
}
