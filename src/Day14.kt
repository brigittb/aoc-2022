
private val sourceOfSand = Point(x = 500, y = 0)

private const val X_MAX_THRESHOLD = 1000

fun main() {

    fun parse(input: List<String>) = input
        .filter { it.isNotEmpty() }
        .map {
            it
                .split(" -> ")
                .map { coordinates ->
                    val (x, y) = coordinates.split(",")
                    Point(x = x.toInt(), y = y.toInt())
                }
        }

    fun getXRange(parsedInput: List<Point>): Pair<Int, Int> {
        val xCoordinates = parsedInput.map { it.x }
        val highest = xCoordinates.maxOf { it }
        val lowest = xCoordinates.minOf { it }
        return lowest to highest
    }

    fun getRange(from: Int, to: Int): IntProgression =
        if (from <= to) from..to else from downTo to

    fun setupInitialState(
        highestY: Int,
        highestX: Int,
        rockPositions: List<List<Point>>,
    ): Array<Array<Point>> {
        val grid = Array(highestY + 1) { y -> Array(highestX + 1) { x -> Point(x = x, y = y) } }
        rockPositions
            .map {
                it
                    .windowed(2)
                    .map { (first, second) -> first to second }
            }
            .forEach { coordinates ->
                coordinates.forEach { (from, to) ->
                    for (y in getRange(from.y, to.y)) {
                        for (x in getRange(from.x, to.x)) {
                            with(grid[y][x]) {
                                this.x = x
                                this.y = y
                                this.type = '#'
                            }
                        }
                    }
                }
            }
        grid[sourceOfSand.y][sourceOfSand.x].type = '+'
        return grid
    }

    fun printState(grid: Array<Array<Point>>, lowestX: Int) {
        grid.forEachIndexed { _, columns ->
            columns.forEachIndexed { _, point ->
                if (point.x >= lowestX) {
                    print(point.type)
                }
            }
            println()
        }
    }

    fun moveSand(sourceOfSand: Point, grid: Array<Array<Point>>, lowestX: Int): Point {
        var sand = sourceOfSand
        do {
            sand = sand.move(grid, lowestX)
        } while (sand.nextPoint(grid, lowestX) != null)
        grid[sand.y][sand.x] = sand
        return sand
    }

    fun part1(input: List<String>): Int {
        val rockPositions = parse(input)
        val availableRockPositions = rockPositions.flatten()
        val (lowestX, highestX) = getXRange(availableRockPositions)
        val highestY = availableRockPositions
            .map { it.y }
            .maxOf { it }
        val grid = setupInitialState(highestY, highestX, rockPositions)

        var round = 0
        do {
            val sand = moveSand(sourceOfSand = sourceOfSand.copy(type = 'o'), grid = grid, lowestX = lowestX)
            round++
        } while (!sand.fallingDown(grid, lowestX))

        println("-- result --")
        printState(grid, lowestX)
        return round - 1
    }

    fun part2(input: List<String>): Int {
        val rockPositions = parse(input)
        val availableRockPositions = rockPositions.flatten()
        val (_, highestX) = getXRange(availableRockPositions)
        val highestY = availableRockPositions
            .map { it.y }
            .maxOf { it }
            .plus(2)
        val grid = setupInitialState(highestY, highestX + X_MAX_THRESHOLD, rockPositions)
        grid[highestY].forEach { grid[it.y][it.x].type = '#' }

        var round = 0
        do {
            val sourceOfSandIsBlocked = grid[sourceOfSand.y][sourceOfSand.x].type == 'o'
            moveSand(sourceOfSand = sourceOfSand.copy(type = 'o'), grid = grid, lowestX = 0)
            round++
        } while (!sourceOfSandIsBlocked)

        println("-- result --")
        printState(grid, 0)
        return round - 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

data class Point(
    var x: Int,
    var y: Int,
    var type: Char = '.',
) {

    fun move(grid: Array<Array<Point>>, lowestX: Int): Point {
        val target = this.nextPoint(grid, lowestX)
        if (target != null)
            return Point(x = target.x, y = target.y, type = type)
        
        if (this == sourceOfSand.copy(type = 'o'))
            return this
        
        error("sand should not flow into the abyss at this point.")
    }

    fun nextPoint(grid: Array<Array<Point>>, lowestX: Int): Point? {
        if (fallingDown(grid, lowestX))
            return null
        
        val leftDown = grid[this.y + 1][this.x - 1]
        val rightDown = grid[this.y + 1][this.x + 1]
        val down = grid[this.y + 1][this.x]
        return listOf(down, leftDown, rightDown).firstOrNull { it.type == '.' }
    }

    fun fallingDown(
        grid: Array<Array<Point>>,
        lowestX: Int,
    ): Boolean =
        this.y >= grid.size - 1 || this.x >= grid[0].size - 1 || this.x < lowestX
}
