
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


class JavaStringTests {
    val stringABCDEF = JavaString("abcdef")
    val stringABC = JavaString("abc")
    val stringDEF = JavaString("def")
    val stringAB = JavaString("ab")
    val stringCDEF = JavaString("cdef")
    val stringA = JavaString("a")
    val stringBC = JavaString("bc")
    val string = JavaString("")
    val string6 = JavaString("6")
    val string0 = JavaString("0")
    val string_7 = JavaString("-7")
    val string1234567 = JavaString("1234567")

    @Test
    fun testConcat() {
        assertEquals(stringABCDEF, stringABC.concat(stringDEF))
        assertEquals(stringABCDEF, stringAB.concat(stringCDEF))
        assertEquals(stringABCDEF, stringA.concat(stringBC).concat(stringDEF))
        assertEquals(stringABCDEF, stringA + stringBC + stringDEF)
    }

    @Test
    fun testSubstring(){
        assertEquals(stringABC,stringABCDEF.substring(0,3))
        assertEquals(stringA,stringABC.substring(0,1))
        assertEquals(stringBC,stringABC.substring(1))
    }

    @Test
    fun testIndexOf(){
        assertEquals(-1,stringBC.indexOf(stringA))
        assertEquals(1,stringABC.indexOf(stringBC))
        assertEquals(0,stringABCDEF.indexOf(stringAB))
        assertEquals(3,stringABCDEF.indexOf(stringDEF))
        assertEquals(0,stringABC.indexOf(string))
        assertEquals(3,stringABC.indexOf(string,4))
    }

    @Test
    fun testFromInt(){
        assertEquals(string6,JavaString.fromInt(6))
        assertEquals(string0,JavaString.fromInt(0))
        assertEquals(string_7,JavaString.fromInt(-7))
        assertEquals(string1234567,JavaString.fromInt(1234567))
    }

    @Test
    fun testToFloat(){
        assertEquals(123f,JavaString("123f").toFloat())
        assertEquals(-0.456F,JavaString("-0.456F").toFloat())
        assertEquals(00000057.345f,JavaString("00000057.345f").toFloat())
        assertEquals(-2e4f,JavaString("-2e4f").toFloat())
        assertEquals(5E-3F.toDouble(),JavaString("5E-3F").toFloat().toDouble(),1e-2)

    }
}