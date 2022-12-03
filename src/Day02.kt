fun main() {

    fun parse(input: List<String>): List<Pair<String, String>> = input
        .map { it.split(" ") }
        .filter { it.size == 2 }
        .map { (opponent, own) -> opponent to own }

    fun mapToAbc(own: String): String = when (own) {
        "X" -> "A"
        "Y" -> "B"
        else -> "C"
    }

    fun part1(input: List<String>): Int {
        return parse(input)
            .map { (opponent, own) -> opponent to mapToAbc(own) }
            .map { (opponent, own) -> Option.valueOf(opponent) to Option.valueOf(own) }
            .sumOf { (opponent, own) ->
                when (opponent.name) {
                    own.win -> 6 + own.ordinal + 1
                    own.draw -> 3 + own.ordinal + 1
                    else -> own.ordinal + 1
                }
            }
    }


    fun part2(input: List<String>): Int =
        parse(input)
            .map { (opponent, choice) -> Option.valueOf(opponent) to Choice.valueOf(choice) }
            .sumOf { (opponent, choice) ->
                when (choice) {
                    Choice.X -> Option.valueOf(opponent.win).ordinal + 1 + choice.score
                    Choice.Y -> Option.valueOf(opponent.draw).ordinal + 1 + choice.score
                    Choice.Z -> Option.valueOf(opponent.loss).ordinal + 1 + choice.score
                }
            }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

private enum class Option(
    val win: String,
    val draw: String,
    val loss: String
) {
    A("C", "A", "B"), // rock
    B("A", "B", "C"), // paper
    C("B", "C", "A") // scissor
}

private enum class Choice(val score: Int) {
    X(0),
    Y(3),
    Z(6)
}
