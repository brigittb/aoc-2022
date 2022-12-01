import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun parse(input: List<String>): MutableList<List<Int>> {
    val parsedInput = mutableListOf<List<Int>>()
    var current = mutableListOf<Int>()
    input
        .forEach {
            if (it.isEmpty() && current.isNotEmpty()) {
                parsedInput.add(current)
                current = mutableListOf()
            } else if (it.isNotEmpty()) {
                current.add(it.toInt())
            }
        }
        .also {
            if (current.isNotEmpty()) {
                parsedInput.add(current)
            }
        }
    return parsedInput
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
