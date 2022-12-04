fun main() {

    fun parse(input: List<String>): List<List<IntRange>> =
        input
            .flatMap { it.split(",") }
            .map { it.split("-") }
            .map { (start, end) -> IntRange(start.toInt(), end.toInt()) }
            .chunked(2)

    fun part1(input: List<List<IntRange>>): Int =
        input
            .count { (first, second) ->
                first.contains(second.first) && first.contains(second.last) ||
                        second.contains(first.first) && second.contains(first.last)
            }


    fun part2(input: List<List<IntRange>>): Int =
        input
            .map { (first, second) -> first.intersect(second) }
            .count { it.isNotEmpty() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(parse(testInput)) == 2)
    check(part2(parse(testInput)) == 4)

    val input = readInput("Day04")
    println(part1(parse(input)))
    println(part2(parse(input)))
}
