import kotlin.math.abs

fun main() {

    fun checkSignalStrength(
        cycle: Int,
        register: Int,
        signalStrengths: MutableList<Int>,
    ) {
        if (cycle in listOf(20, 60, 100, 140, 180, 220)) {
            signalStrengths.add(register * cycle)
        }
    }

    fun part1(input: List<String>): Int {
        var cycle = 0
        var register = 1
        val signalStrengths = mutableListOf<Int>()

        input
            .filter { it.isNotEmpty() }
            .forEach {
                if (it.contains("noop")) {
                    cycle++
                    checkSignalStrength(cycle, register, signalStrengths)
                } else {
                    val value = it.substringAfter(" ").toInt()
                    repeat(2) {
                        cycle++
                        checkSignalStrength(cycle, register, signalStrengths)
                    }
                    register += value
                }
            }

        return signalStrengths.sum()
    }

    fun drawPixel(register: Int, cycle: Int) {
        if (abs(register - (cycle % 40)) <= 1) print("#") else print(".")
        if ((cycle + 1) % 40 == 0) println()
    }

    fun part2(input: List<String>) {
        var cycle = 0
        var register = 1

        input
            .filter { it.isNotEmpty() }
            .forEach {
                if (it.contains("noop")) {
                    drawPixel(register, cycle)
                    cycle++
                } else {
                    val value = it.substringAfter(" ").toInt()
                    repeat(2) {
                        drawPixel(register, cycle)
                        cycle++
                    }
                    register += value
                }
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
    part2(input)
}
