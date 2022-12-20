import java.util.Stack

fun main() {

    fun prepareGrid(
        input: List<String>,
        x: Int,
        y: Int,
    ): Triple<Array<Array<Vertex>>, Pair<Int, Int>, Pair<Int, Int>> {
        val grid = Array(x) { Array(y) { Vertex() } }
        var source = 0 to 0
        var destination = 0 to 0
        input
            .filter { it.isNotEmpty() }
            .map { it.asSequence() }
            .forEachIndexed { row, columns ->
                columns.forEachIndexed { column, height ->
                    when (height) {
                        'S' -> {
                            with(grid[row][column]) {
                                this.row = row
                                this.column = column
                                this.height = 'a'
                                this.distance = 0
                            }
                            source = row to column
                        }

                        'E' -> {
                            with(grid[row][column]) {
                                this.row = row
                                this.column = column
                                this.height = 'z'
                                this.distance = 0
                            }
                            destination = row to column
                        }

                        else ->
                            with(grid[row][column]) {
                                this.row = row
                                this.column = column
                                this.height = height
                            }
                    }
                }
            }
        return Triple(grid, source, destination)
    }

    fun upOrNull(vertex: Vertex, grid: Array<Array<Vertex>>) =
        if (vertex.row != 0) grid[vertex.row - 1][vertex.column] else null

    fun rightOrNull(vertex: Vertex, y: Int, grid: Array<Array<Vertex>>) =
        if (vertex.column != y - 1) grid[vertex.row][vertex.column + 1] else null

    fun downOrNull(vertex: Vertex, x: Int, grid: Array<Array<Vertex>>) =
        if (vertex.row != x - 1) grid[vertex.row + 1][vertex.column] else null

    fun leftOrNull(vertex: Vertex, grid: Array<Array<Vertex>>) =
        if (vertex.column != 0) grid[vertex.row][vertex.column - 1] else null

    fun part1(input: List<String>): Int {
        val x = input.size
        val y = input.first().length
        val (grid, source, destination) = prepareGrid(input, x, y)
        val queue = ArrayDeque<Vertex>()
        val visited = Stack<Vertex>()

        queue.add(grid[source.first][source.second])
        visited.add(grid[source.first][source.second])
        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()
            listOfNotNull(
                upOrNull(vertex, grid),
                rightOrNull(vertex, y, grid),
                downOrNull(vertex, x, grid),
                leftOrNull(vertex, grid)
            )
                .filter { vertex.height + 1 >= it.height }
                .filter { !visited.contains(it) }
                .forEach {
                    if (vertex.distance != null) {
                        it.distance = vertex.distance
                            ?.plus(1)
                            ?: error("previous vertex's distance not set")
                    }
                    queue.add(it)
                    visited.add(it)
                }
        }

        return grid[destination.first][destination.second].distance ?: 0
    }

    fun part2(input: List<String>): Int {
        val x = input.size
        val y = input.first().length
        val (grid, _, destination) = prepareGrid(input, x, y)
        val queue = ArrayDeque<Vertex>()
        val visited = Stack<Vertex>()
        val possibleSources = mutableListOf<Vertex>()

        queue.add(grid[destination.first][destination.second])
        visited.add(grid[destination.first][destination.second])

        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()
            listOfNotNull(
                upOrNull(vertex, grid),
                rightOrNull(vertex, y, grid),
                downOrNull(vertex, x, grid),
                leftOrNull(vertex, grid)
            )
                .filter { vertex.height - 1 <= it.height }
                .filter { !visited.contains(it) }
                .forEach {
                    if (vertex.distance != null) {
                        it.distance = vertex.distance
                            ?.plus(1)
                            ?: error("previous vertex's distance not set")
                    }
                    queue.add(it)
                    visited.add(it)
                    if (it.height == 'a') {
                        possibleSources.add(it)
                    }
                }
        }

        return possibleSources
            .sortedBy { it.distance }
            .first()
            .distance
            ?: 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

data class Vertex(
    var row: Int = 0,
    var column: Int = 0,
    var height: Char = 'a',
    var distance: Int? = null,
)
