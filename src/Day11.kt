import java.util.function.Function

fun main() {

    fun initializeTestMonkeys(): List<Monkey> {
        return listOf(
            Monkey(
                items = mutableListOf(79, 98),
                operation = { it * 19 },
                test = 23,
                trueCase = 2,
                falseCase = 3,
            ),
            Monkey(
                items = mutableListOf(54, 65, 75, 74),
                operation = { it + 6 },
                test = 19,
                trueCase = 2,
                falseCase = 0,
            ),
            Monkey(
                items = mutableListOf(79, 60, 97),
                operation = { it * it },
                test = 13,
                trueCase = 1,
                falseCase = 3,
            ),
            Monkey(
                items = mutableListOf(74),
                operation = { it + 3 },
                test = 17,
                trueCase = 0,
                falseCase = 1,
            ),
        )
    }

    fun initializeMonkeys(): List<Monkey> {
        return listOf(
            Monkey(
                items = mutableListOf(74, 64, 74, 63, 53),
                operation = { it * 7 },
                test = 5,
                trueCase = 1,
                falseCase = 6,
            ),
            Monkey(
                items = mutableListOf(69, 99, 95, 62),
                operation = { it * it },
                test = 17,
                trueCase = 2,
                falseCase = 5,
            ),
            Monkey(
                items = mutableListOf(59, 81),
                operation = { it + 8 },
                test = 7,
                trueCase = 4,
                falseCase = 3,
            ),
            Monkey(
                items = mutableListOf(50, 67, 63, 57, 63, 83, 97),
                operation = { it + 4 },
                test = 13,
                trueCase = 0,
                falseCase = 7,
            ),
            Monkey(
                items = mutableListOf(61, 94, 85, 52, 81, 90, 94, 70),
                operation = { it + 3 },
                test = 19,
                trueCase = 7,
                falseCase = 3,
            ),
            Monkey(
                items = mutableListOf(69),
                operation = { it + 5 },
                test = 3,
                trueCase = 4,
                falseCase = 2,
            ),
            Monkey(
                items = mutableListOf(54, 55, 58),
                operation = { it + 7 },
                test = 11,
                trueCase = 1,
                falseCase = 5,
            ),
            Monkey(
                items = mutableListOf(79, 51, 83, 88, 93, 76),
                operation = { it * 3 },
                test = 2,
                trueCase = 0,
                falseCase = 6,
            ),
        )
    }

    fun part1(monkeys: List<Monkey>): Long {
        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.items.forEach {
                    val newWorryLevel = monkey.operation.apply(it)
                    val afterInspection = newWorryLevel.floorDiv(3)
                    if (afterInspection % monkey.test == 0L) {
                        monkeys[monkey.trueCase].items.add(afterInspection)
                    } else {
                        monkeys[monkey.falseCase].items.add(afterInspection)
                    }
                    monkey.inspectionCount++
                }
                monkey.items = mutableListOf()
            }
        }

        val (monkey1, monkey2) = monkeys
            .sortedByDescending { it.inspectionCount }
            .take(2)
            .map { it.inspectionCount }
        return monkey1 * monkey2
    }

    fun part2(monkeys: List<Monkey>): Long {
        val lcm = monkeys
            .map { it.test }
            .reduce { acc, i -> acc * i }

        repeat(10000) {
            monkeys.forEachIndexed { i, monkey ->
                monkey.items.forEach { item ->
                    val newWorryLevel = monkey.operation.apply(item)
                    val mod = newWorryLevel % lcm
                    if (mod % monkey.test == 0L) {
                        monkeys[monkey.trueCase].items.add(mod)
                    } else {
                        monkeys[monkey.falseCase].items.add(mod)
                    }
                    monkey.inspectionCount++
                }
                monkey.items = mutableListOf()
            }
        }

        val (monkey1, monkey2) = monkeys
            .sortedByDescending { it.inspectionCount }
            .take(2)
            .map { it.inspectionCount }
        return monkey1 * monkey2
    }

    // test if implementation meets criteria from the description, like:
    check(part1(initializeTestMonkeys()) == 10605L)
    check(part2(initializeTestMonkeys()) == 2713310158)

    println(part1(initializeMonkeys()))
    println(part2(initializeMonkeys()))
}

data class Monkey(
    var items: MutableList<Long>,
    val operation: Function<Long, Long>,
    val test: Int,
    val trueCase: Int,
    val falseCase: Int,
    var inspectionCount: Long = 0L,
)
