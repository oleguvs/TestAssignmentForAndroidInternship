import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import kotlin.math.pow

/** Constant represents Unicode character 'DIGIT ZERO'(U+0030).*/
const val CHAR_0 = '0'

/**Constant represents the base of the decimal system.*/
const val RADIX_10 = 10

/**Constant is used to check whether string representation of float is written in exponential notation.*/
const val E_NOTATION = 'e'

/**
 * This class is used to be represent the String class in Java.
 * The 'JavaString' class represents a string literal as a sequence of characters
 * which are elements the CharArray instance.
 * The class JavaString includes functions for concatenating, searching and parsing strings,
 * for extracting substrings and for converting integers.
 */
class JavaString {

    /** The [charArray] array is used for character storage. */
    private val charArray: CharArray

    /** The hash code for this object. */
    private var hash = 0

    companion object {
        /**
         * Returns the JavaString representation of the integer [i] argument.
         */
        fun fromInt(i: Int): JavaString {
            /**
             * Creates a local function which returns the number of digits in the specified [i] integer
             */
            fun countDigits(i: Int): Int {
                var num = if (i < 0) -i else i
                var count = 0
                do {
                    num /= RADIX_10
                    count++
                } while (num > 0)
                return count
            }

            var number = i
            val size = countDigits(number) + if (number < 0) 1 else 0
            //adds additional array cell for ASCII minus sign

            val charArray = CharArray(size)
            //Starts filling array backwards with the least significant digit
            var startIndex = size - 1
            var endIndex = 0
            //sets ASCII minus sign to the first array element if necessary
            if (number < 0) {
                charArray[endIndex++] = '-'
                number = -number
            }
            for (index in startIndex downTo endIndex) {
                val digit = number % RADIX_10 // gets the least significant digit
                charArray[index] = CHAR_0 + digit //converts digit to character
                number /= RADIX_10 //truncates the least significant digit
            }
            return JavaString(charArray)
        }
    }

    /**
     * Creates a new JavaString object so that it represents the sequence
     * of characters currently contained in the String [str] argument.
     */
    constructor(str: String = "") {
        charArray = str.toCharArray()
    }

    /**
     * Creates a new JavaString object so that it represents the sequence of
     * characters currently contained in the character array [charArray]
     * argument. The contents of [charArray] are copied; subsequent modification
     * of [charArray] does not affect the newly created JavaString.
     */
    constructor(charArray: CharArray) {
        this.charArray = charArrayOf(*charArray)
    }

    /**
     * Returns a JavaString object obtained by concatenating this one
     * with the JavaString representation of the given [other] object.
     */
    operator fun plus(other: Any?): JavaString {
        return this.concat(JavaString(other.toString()))
    }

    /**
     * Concatenates the specified JavaString [str] object to the end of this one.
     * Returns a JavaString object that represents the concatenation of this
     * object's characters followed by the JavaString argument's characters.
     */
    fun concat(str: JavaString): JavaString {
        if (str.charArray.isEmpty()) return this
        return JavaString(this.charArray + str.charArray)
    }

    /**
     * Returns a JavaString object that is a substring of this JavaString.
     * Substring begins at the specified [startIndex] index and if [endIndex] is not set
     * it extends to the end of this string.
     * @throws JavaStringIndexOutOfBoundsException If an JavaString has been accessed with an illegal index.
     */
    fun substring(startIndex: Int, endIndex: Int = charArray.size): JavaString {
        if (startIndex < 0) {
            throw JavaStringIndexOutOfBoundsException(startIndex)
        }
        if (endIndex > charArray.size) {
            throw JavaStringIndexOutOfBoundsException(endIndex)
        }
        val newLength: Int = endIndex - startIndex;
        if (newLength < 0) {
            throw JavaStringIndexOutOfBoundsException(newLength)
        }
        if (newLength == charArray.size) return this
        val newCharArray = charArray.copyOfRange(startIndex, endIndex)
        return JavaString(newCharArray)
    }

    /**
     * Returns the index within this JavaString object of the first occurrence of the
     * specified [substring] substring, starting at the specified [startIndex] index.
     * If [startIndex] is not set the search starts from the first index of the string.
     * If there is no such occurrence the function returns -1.
     */
    fun indexOf(substring: JavaString, startIndex: Int = 0): Int {
        if (startIndex >= this.charArray.size) {
            return if (substring.charArray.isEmpty()) this.charArray.size else -1
        }
        if (substring.charArray.isEmpty()) {
            return startIndex
        }
        val endIndex = this.charArray.size - substring.charArray.size
        /*Use as a mark of the first occurrence of the substring in the string*/
        val firstSubstringCharacter: Char = substring.charArray[0]

        for (index in startIndex..endIndex) {
            //checking for the first occurrence
            if (this.charArray[index] == firstSubstringCharacter) {
                //variables are used to get the subsequent characters in while-loop
                var strIndex = index + 1
                var subIndex = 1

                //matching the rest characters of the substring against the subsequent characters of string
                while (subIndex < substring.charArray.size
                    && substring.charArray[subIndex] == this.charArray[strIndex]
                ) {
                    strIndex++
                    subIndex++
                }
                //checks whether the whole substring was found
                if (subIndex == substring.charArray.size) {
                    return index
                }
            }
        }
        return -1
    }

    /**
     * Parses this [JavaString] object as a [Float] number and returns the result.
     * The method is based on sequential computation: integer and fractional parts of number,
     * and followed by adding them.
     * @throws NumberFormatException if the string is not a valid representation of a number.
     */
    fun toFloat(): Float {
        if (charArray.isEmpty()) {
            throw NumberFormatException("empty JavaString")
        }
        var curIndex = 0
        var isAnyDigitPresent = false
        var isNegativeNumber = false

        /**
         * Creating a local [convertDigitsToNum] function to convert the specified range
         * of the array chars to a decimal number. The range can represent one of two parts
         * of decimal number - either integer part or fractional part. The range lie between
         * the specified [startInd] (inclusive) and [endInd] (exclusive). Argument [isIntPart]
         * specifies whether this part of number is integer part or not. Argument [isExpFound]
         * indicates that exponent notation symbol was found
         */
        fun convertDigitsToNum(
            startInd: Int = 0,
            endInd: Int = charArray.size,
            isIntPart: Boolean = true,
            isExpFound: Boolean = false
        ): Float {
            var result = 0
            var countDigits = 0
            curIndex = startInd
            outerLoop@ while (curIndex < endInd) {
                val character = charArray[curIndex++]
                when (true) {
                    character.isDigit() -> {
                        if (!isAnyDigitPresent) {
                            isAnyDigitPresent = true
                        }
                        if (!isIntPart) countDigits++
                        val digit = character - CHAR_0
                        result = result * RADIX_10 + digit
                    }
                    // check if the current character is floating literal
                    character.equals('f', true) && curIndex == charArray.size -> {
                        break@outerLoop
                    }
                    // check if the current character is exponent notation symbol
                    character.equals(E_NOTATION, true) && !isExpFound -> {
                        break@outerLoop
                    }

                    else -> throw NumberFormatException(": For input JavaString: \"$this\"")
                }
            }
            //check if there is at least a one digit in this part of number
            if (result == 0 && !isAnyDigitPresent) {
                throw NumberFormatException(": For input JavaString: \"$this\"")
            }

            return if (isIntPart) result.toFloat() else result / (RADIX_10.toFloat().pow(countDigits))
        }

        when (charArray[curIndex]) {
            '+' -> curIndex++
            '-' -> {
                isNegativeNumber = true
                curIndex++
            }
        }
        //skips starting sequence of zeros
        while (curIndex < charArray.size && charArray[curIndex] == CHAR_0) {
            if (!isAnyDigitPresent) isAnyDigitPresent = true
            curIndex++
        }
        val splitIndex = charArray.indexOf('.')
        //variable which the function returns
        var floatNumber = when (splitIndex) {
            -1 -> convertDigitsToNum(curIndex)//for integer part of number
            curIndex -> {
                convertDigitsToNum(splitIndex + 1, isIntPart = false)//for fraction part of number
            }
            else -> {//if number contains both parts
                (convertDigitsToNum(curIndex, splitIndex)   //integer part of number
                        + convertDigitsToNum(splitIndex + 1, isIntPart = false))//fraction part of number
            }
        }
        floatNumber = if (isNegativeNumber) -floatNumber else floatNumber
        //computes exponent part of number if for its Representation uses exponential notation
        if (curIndex < charArray.size) {
            val mantissa = floatNumber
            var isPositiveExp = true
            when (charArray[curIndex]) {
                '+' -> curIndex++
                '-' -> {
                    isPositiveExp = false
                    curIndex++
                }
            }
            isAnyDigitPresent = false
            var exponent = convertDigitsToNum(curIndex, isExpFound = true)
            if (!isPositiveExp) {
                exponent = -exponent
            }
            return mantissa * RADIX_10.toFloat().pow(exponent)
        }
        return floatNumber
    }

    /**Returns a string representation of this JavaString object.*/
    override fun toString() = charArray.joinToString("")

    /**Indicates whether some other object is "equal to" this one.*/
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is JavaString && this.charArray contentEquals other.charArray) return true
        return false
    }

    /**Returns a hash code charArray for the object.*/
    override fun hashCode(): Int {
        var newHash = hash
        if (newHash != 0 || this.charArray.isEmpty()) {
            return hash
        }
        newHash = this.charArray.contentHashCode()
        hash = newHash
        return hash
    }
}

/**
 * Thrown by [JavaString] methods to indicate that an index
 * is either negative or greater than the size of the string.
 */
class JavaStringIndexOutOfBoundsException : IndexOutOfBoundsException {
    /**
     * Constructs an [JavaStringIndexOutOfBoundsException] with no
     * detail message.
     */
    constructor() : super()

    /**
     * Constructs an [JavaStringIndexOutOfBoundsException] with the
     * specified [s] detail message.
     */
    constructor(s: String) : super(s)

    /**
     * Constructs an [JavaStringIndexOutOfBoundsException] with
     * an [index] argument indicating the illegal index.
     */
    constructor(index: Int) : super("JavaString index is out of range: $index")
}

