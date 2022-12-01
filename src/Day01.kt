fun main() {

    fun part1(input: List<String>): Int = parse(input)
        .maxOfOrNull { it.sum() }
        ?: 0

    fun part2(input: List<String>): Int = parse(input)
        .map { it.sum() }
        .sortedDescending()
        .slice(0..2)
        .sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
