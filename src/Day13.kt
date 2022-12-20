fun main() {

    fun convertToList(
        remaining: String,
        sb: StringBuilder = StringBuilder(),
        level: Level,
    ): Level {
        val currentChar = if (remaining.isEmpty()) {
            if (sb.isNotEmpty()) {
                val itemToAdd = sb.toString().toInt()
                level.current.add(itemToAdd)
            }
            return level
        } else remaining[0]

        when {
            currentChar.isDigit() ->
                return convertToList(
                    remaining = remaining.substring(1),
                    sb = sb.append(currentChar),
                    level = level,
                )

            currentChar == '[' -> {
                val newList = mutableListOf<Any>()
                level.current.add(newList)
                return convertToList(
                    remaining = remaining.substring(1),
                    sb = StringBuilder(),
                    level = Level(parent = level, current = newList),
                )
            }

            currentChar == ']' -> {
                val itemToAdd = sb.toString().toIntOrNull()
                itemToAdd?.let { level.current.add(itemToAdd) }
                return convertToList(
                    remaining = remaining.substring(1),
                    sb = StringBuilder(),
                    level = level.parent ?: error("Parent should be set."),
                )
            }

            else -> {
                sb.toString()
                    .toIntOrNull()
                    ?.let { level.current.add(it) }
                return convertToList(
                    remaining = remaining.substring(1),
                    sb = StringBuilder(),
                    level = level,
                )
            }
        }
    }

    fun parse(input: String): List<List<List<Any>>> = input
        .split("\n\n")
        .map {
            it
                .split("\n")
                .filter { listInput -> listInput.isNotEmpty() }
                .map { listInput ->
                    listInput
                        .removePrefix("[")
                        .removeSuffix("]")
                }
                .map { listInput ->
                    convertToList(
                        remaining = listInput,
                        level = Level(
                            current = mutableListOf(),
                        )
                    ).current
                }
        }

    fun comparePairs(
        first: List<Any>,
        second: List<Any>,
    ): Pair<Boolean, Boolean> {
        for (i in first.indices) {
            val left = first[i]
            val right = second.getOrNull(i)
            when {
                right == null -> return false to false

                left is Int && right is Int -> {
                    when {
                        left < right -> return true to false
                        left > right -> return false to false
                        left == right -> continue
                    }
                }

                left is List<*> && right is List<*> -> {
                    val (inRightOrder, shouldContinue) = comparePairs(left as List<Any>, right as List<Any>)
                    if (!inRightOrder || !shouldContinue) {
                        return inRightOrder to shouldContinue
                    }
                }

                left is Int && right is List<*> -> {
                    val (inRightOrder, shouldContinue) = comparePairs(listOf(left), right as List<Any>)
                    if (!inRightOrder || !shouldContinue) {
                        return inRightOrder to shouldContinue
                    }
                }

                left is List<*> && right is Int -> {
                    val (inRightOrder, shouldContinue) = comparePairs(left as List<Any>, listOf(right))
                    if (!inRightOrder || !shouldContinue) {
                        return inRightOrder to shouldContinue
                    }
                }
            }
        }

        if (first.size < second.size) {
            return true to false
        }

        return true to true
    }

    fun part1(input: String): Int {
        val pairsInRightOrder = mutableListOf<Int>()
        val parsedInput = parse(input)
        parsedInput.forEachIndexed { index, lists ->
            val (first, second) = lists
            if (comparePairs(first, second).first) {
                pairsInRightOrder.add(index + 1)
            }
        }
        return pairsInRightOrder.sum()
    }

    fun part2(input: String): Int {
        val dividerPackets = listOf(listOf(listOf(2)), listOf(listOf(6)))
        val parsedInput: List<List<Any>> = input
            .split("\n")
            .filter { it.isNotEmpty() }
            .map { listInput ->
                listInput
                    .removePrefix("[")
                    .removeSuffix("]")
            }
            .map { listInput ->
                convertToList(
                    remaining = listInput,
                    level = Level(
                        current = mutableListOf(),
                    )
                ).current
            }
        val indexOfDividerPackets = parsedInput
            .sortedWith { a, b -> if (comparePairs(a, b).first) -1 else 1 }
            .mapIndexed { idx, item -> idx + 1 to item }
            .filter { (_, it) -> it in dividerPackets }
        return indexOfDividerPackets[0].first * indexOfDividerPackets[1].first
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputAsText("Day13_test")
//    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInputAsText("Day13")
//    println(part1(input)) // 6072
    println(part2(input))
}

data class Level(
    val current: MutableList<Any>,
    val parent: Level? = null,
)
