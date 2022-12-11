fun main() {

    fun getMoves(input: List<String>) = input
        .filter { it.isNotEmpty() }
        .map {
            Movement(
                direction = it.substringBefore(" ").first(),
                step = it.substringAfter(" ").toInt(),
            )
        }

    fun getSize(moves: List<Movement>, directions: Set<Char>) = moves
        .filter { it.direction in directions }
        .maxBy { it.step }
        .step
        .plus(1)

    fun diff(tail: Position, head: Position): Pair<Int, Int> {
        val xDiff = head.x - tail.x
        val yDiff = head.y - tail.y
        return Pair(xDiff, yDiff)
    }

    fun move(
        tail: Position,
        head: Position,
    ): Position {
        val (xDiff, yDiff) = diff(tail, head)
        val newPosition = when {
            xDiff == -2 && yDiff <= -1 -> Position(index = tail.index, x = tail.x - 1, y = tail.y - 1)
            xDiff == -2 && yDiff >= 1 -> Position(index = tail.index, x = tail.x - 1, y = tail.y + 1)
            xDiff == 2 && yDiff <= -1 -> Position(index = tail.index, x = tail.x + 1, y = tail.y - 1)
            xDiff == 2 && yDiff >= 1 -> Position(index = tail.index, x = tail.x + 1, y = tail.y + 1)
            xDiff <= -1 && yDiff == -2 -> Position(index = tail.index, x = tail.x - 1, y = tail.y - 1)
            xDiff >= 1 && yDiff == -2 -> Position(index = tail.index, x = tail.x + 1, y = tail.y - 1)
            xDiff <= -1 && yDiff == 2 -> Position(index = tail.index, x = tail.x - 1, y = tail.y + 1)
            xDiff >= 1 && yDiff == 2 -> Position(index = tail.index, x = tail.x + 1, y = tail.y + 1)
            xDiff == 2 -> tail.down()
            xDiff == -2 -> tail.up()
            yDiff == 2 -> tail.right()
            yDiff == -2 -> tail.left()
            else -> tail
        }
        return newPosition
    }

    fun part1(input: List<String>): Int {
        val moves = getMoves(input)
        val x = getSize(moves, setOf('U', 'D'))

        var head = Position(x = x - 1, y = 0)
        var tail = Position(x = x - 1, y = 0)
        val tailPositions = mutableSetOf(tail)

        moves.forEach { (direction, step) ->
            when (direction) {
                'R' -> {
                    for (i in 1 until step + 1) {
                        head = head.right()
                        tail = move(tail, head)
                        tailPositions.add(tail)
                    }
                }

                'L' -> {
                    for (i in 1 until step + 1) {
                        head = head.left()
                        tail = move(tail, head)
                        tailPositions.add(tail)
                    }
                }

                'U' -> {
                    for (i in 1 until step + 1) {
                        head = head.up()
                        tail = move(tail, head)
                        tailPositions.add(tail)
                    }
                }

                'D' -> {
                    for (i in 1 until step + 1) {
                        head = head.down()
                        tail = move(tail, head)
                        tailPositions.add(tail)
                    }
                }
            }
        }

        return tailPositions.size
    }

    val tailSize = 9

    fun moveTail(
        newPositions: MutableList<Position>,
        head: Position,
        tailEndPositions: MutableSet<Position>,
    ) {
        newPositions[0] = move(newPositions.first(), head)
        newPositions
            .windowed(2)
            .forEach { (_, current) ->
                newPositions[current.index] = move(
                    tail = current,
                    head = newPositions[current.index - 1],
                )
                if (current.index == tailSize - 1) tailEndPositions.add(current)
            }
    }

    fun part2(input: List<String>): Int {
        val moves = getMoves(input)
        val startPosition = Position(x = getSize(moves, setOf('U', 'D')) - 1, y = 0)
        var head = startPosition
        var tail = MutableList(tailSize) { i -> Position(index = i, x = startPosition.x, y = startPosition.y) }
        val tailEndPositions = mutableSetOf<Position>()

        moves.forEach { (direction, step) ->
            val newPositions = tail
            when (direction) {
                'R' -> {
                    for (i in 1 until step + 1) {
                        head = head.right()
                        moveTail(newPositions, head, tailEndPositions)
                    }
                }

                'L' -> {
                    for (i in 1 until step + 1) {
                        head = head.left()
                        moveTail(newPositions, head, tailEndPositions)
                    }
                }

                'U' -> {
                    for (i in 1 until step + 1) {
                        head = head.up()
                        moveTail(newPositions, head, tailEndPositions)
                    }
                }

                'D' -> {
                    for (i in 1 until step + 1) {
                        head = head.down()
                        moveTail(newPositions, head, tailEndPositions)
                    }
                }
            }
            tail = newPositions
        }

        return tailEndPositions.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

data class Movement(
    val direction: Char,
    val step: Int,
)

data class Position(
    val index: Int = 0,
    var x: Int,
    var y: Int,
) {
    fun right() = Position(index = index, x = x, y = y + 1)

    fun left() = Position(index = index, x = x, y = y - 1)

    fun up() = Position(index = index, x = x - 1, y = y)

    fun down() = Position(index = index, x = x + 1, y = y)
}
