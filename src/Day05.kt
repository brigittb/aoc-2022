fun main() {

    fun prepareConfiguration(input: List<String>): MutableList<ArrayDeque<String>> {
        val stacks = input
            .first { it.contains("1") }
            .split("   ")
            .map { it.trim() }
            .map { ArrayDeque<String>() }
            .toMutableList()
        input
            .filter { it.contains("[") }
            .map { it.asSequence().chunked(4) }
            .map { values -> values.map { it.joinToString("").trim() } }
            .forEach {
                it.forEachIndexed { index, value ->
                    if (value.isNotEmpty()) {
                        stacks[index].addLast(value)
                    }
                }
            }
        return stacks
    }

    fun getInstructions(input: List<String>) = input
        .filter { it.startsWith("move") }
        .map { instruction ->
            instruction
                .split(Regex("[a-z]{2,4}"))
                .filterNot { it.isEmpty() }
                .map { it.trim() }
        }
        .map { (count, source, destination) ->
            Triple(
                count.toInt(),
                source.toInt(),
                destination.toInt(),
            )
        }

    fun getTopCrates(stacks: MutableList<ArrayDeque<String>>): String =
        stacks
            .map { it.first() }
            .joinToString(separator = "") {
                it.replace(Regex("[\\[\\]]"), "")
            }

    fun part1(input: List<String>): String {
        val stacks = prepareConfiguration(input)
        getInstructions(input).forEach { (count, source, destination) ->
            for (i in 1 until count + 1) {
                val element = stacks[source - 1].removeFirst()
                stacks[destination - 1].addFirst(element)
            }
        }
        return getTopCrates(stacks)
    }


    fun part2(input: List<String>): String {
        val stacks = prepareConfiguration(input)
        getInstructions(input).forEach { (count, source, destination) ->
            val sourceList = stacks[source - 1].toList()

            val elementsToKeep = sourceList.takeLast(sourceList.size - count)
            stacks[source - 1] = ArrayDeque(elementsToKeep)

            val elementsToMove = sourceList.take(count)
            stacks[destination - 1].addAll(0, elementsToMove)
        }
        return stacks
            .map { it.first() }
            .joinToString(separator = "") { it.replace(Regex("[\\[\\]]"), "") }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
