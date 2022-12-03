fun main() {

    val priority = ('a'..'z') + ('A'..'Z')

    fun part1(input: List<String>): Int =
        input
            .map { it.chunked(it.length / 2) }
            .map { (first, second) ->
                val itemsOfFirst = first.asSequence().toSet()
                val itemsOfSecond = second.asSequence().toSet()
                itemsOfFirst
                    .intersect(itemsOfSecond)
                    .first()
            }
            .sumOf { priority.indexOf(it) + 1 }


    fun part2(input: List<String>): Int =
        input
            .chunked(3)
            .map { (first, second, third) ->
                val itemsOfFirst = first.asSequence().toSet()
                val itemsOfSecond = second.asSequence().toSet()
                val itemsOfThird = third.asSequence().toSet()
                itemsOfFirst
                    .intersect(itemsOfSecond)
                    .intersect(itemsOfThird)
                    .first()
            }
            .sumOf { priority.indexOf(it) + 1 }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
