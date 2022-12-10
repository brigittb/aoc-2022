fun main() {

    fun prepareMatrix(size: Int, input: List<String>): Array<Array<Tree>> {
        val grid = Array(size) { Array(size) { Tree() } }
        input
            .map { it.asSequence() }
            .forEachIndexed { row, trees ->
                trees.forEachIndexed { column, tree ->
                    with(grid[row][column]) {
                        x = row
                        y = column
                        height = tree.digitToInt()
                    }
                }
            }
        return grid
    }

    fun part1(input: List<String>): Int {
        val size = input.size
        val grid = prepareMatrix(size, input)
        val result = mutableSetOf<Tree>()

        // from top left
        for (column in 0 until size) {
            val partialResult = mutableListOf<Tree>()
            partialResult.add(grid[0][column])
            for (row in 1 until size) {
                if (grid[row][column].height > partialResult.maxOf { it.height }) {
                    partialResult.add(grid[row][column])
                }
            }
            result.addAll(partialResult)
        }

        // from top right
        for (row in 0 until size) {
            val partialResult = mutableListOf<Tree>()
            partialResult.add(grid[row][size - 1])
            for (column in size - 1 downTo  1) {
                if (grid[row][column - 1].height > partialResult.maxOf { it.height }) {
                    partialResult.add(grid[row][column - 1])
                }
            }
            result.addAll(partialResult)
        }

        //from bottom right
        for (column in size - 1 downTo 0) {
            val partialResult = mutableListOf<Tree>()
            partialResult.add(grid[size - 1][column])
            for (row in size - 1 downTo  1) {
                if (grid[row - 1][column].height > partialResult.maxOf { it.height }) {
                    partialResult.add(grid[row - 1][column])
                }
            }
            result.addAll(partialResult)
        }

        // from bottom left
        for (row in size - 1 downTo 0) {
            val partialResult = mutableListOf<Tree>()
            partialResult.add(grid[row][0])
            for (column in 0 until size - 1) {
                if (grid[row][column + 1].height > partialResult.maxOf { it.height }) {
                    partialResult.add(grid[row][column + 1])
                }
            }
            result.addAll(partialResult)
        }

        return result.size
    }

    fun part2(input: List<String>): Int {
        val size = input.size
        val grid = prepareMatrix(size, input)

        val result = mutableSetOf<Int>()

        for (row in 1 until size - 1) {
            for (column in 1 until size - 1) {
                val currentHeight = grid[row][column].height

                // left
                var left = 0
                for (y in column - 1 downTo  0) {
                    if (grid[row][y].height < currentHeight) {
                        left++
                    } else if (grid[row][y].height >= currentHeight) {
                        left++
                        break
                    }
                }

                // right
                var right = 0
                for (y in column + 1 until size) {
                    if (grid[row][y].height < currentHeight) {
                        right++
                    } else if (grid[row][y].height >= currentHeight) {
                        right++
                        break
                    }
                }

                // up
                var up = 0
                for (x in row - 1 downTo 0) {
                    if (grid[x][column].height < currentHeight) {
                        up++
                    } else if (grid[x][column].height >= currentHeight) {
                        up++
                        break
                    }
                }

                // down
                var down = 0
                for (x in row + 1 until size) {
                    if (grid[x][column].height < currentHeight) {
                        down++
                    } else if (grid[x][column].height >= currentHeight) {
                        down++
                        break
                    }
                }
                val partialResult = left * right * up * down
                result.add(partialResult)
            }
        }
        return result.max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input)) // 1543
    println(part2(input)) // 595080
}

data class Tree(
    var x: Int = 0,
    var y: Int = 0,
    var height: Int = 0,
)

