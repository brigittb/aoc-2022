fun main() {

    fun indexOfMarker(input: String, length: Int): Int {
        val unique = input
            .windowed(length)
            .map { it.toSet() }
            .first { it.size == length }
            .joinToString("")
        return input.indexOf(unique) + length
    }

    fun part1(input: List<String>): Int =
        indexOfMarker(input = input.first(), length = 4)

    fun part2(input: List<String>): Int =
        indexOfMarker(input = input.first(), length = 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 26)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
