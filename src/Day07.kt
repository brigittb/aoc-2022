fun main() {

    fun findDirs(input: List<String>): MutableMap<String, Long> {
        val stack = ArrayDeque<String>()
        val dirSizes = mutableMapOf<String, Long>()
        input.forEach {
            when {
                it.contains(Regex("cd /")) -> stack.addLast(it.substringAfter("cd "))
                it.contains(Regex("cd [a-z]")) -> stack.addLast("${it.substringAfter("cd ")}/")
                it.contains("cd ..") -> stack.removeLast()
                it.contains(Regex("[0-9]")) -> {
                    val path = stack.joinToString("")
                    val sizeOfCurrentFile = it.substringBefore(" ").toLong()
                    dirSizes[path] = dirSizes[path]
                        ?.plus(sizeOfCurrentFile)
                        ?: sizeOfCurrentFile
                }

                it.contains(Regex("dir [a-z]")) -> {
                    val path = stack.joinToString("")
                    dirSizes[path] = dirSizes[path] ?: 0
                }
            }
        }
        return dirSizes
    }

    fun getSummedUpDirSizes(input: List<String>): MutableMap<String, Long> {
        val result = mutableMapOf<String, Long>()
        findDirs(input)
            .toSortedMap(compareByDescending { it })
            .forEach { (path, size) ->
                result[path] = result[path]
                    ?.plus(size)
                    ?: size
                val parent = path
                    .substringBeforeLast("/")
                    .substringBeforeLast("/")
                    .plus("/")
                if (result[parent] != result[path]) {
                    result[parent] = result[parent]
                        ?.plus(result.getValue(path))
                        ?: result.getValue(path)
                }
            }
        return result
    }

    fun part1(input: List<String>): Long {
        val result = getSummedUpDirSizes(input)
        return result
            .filter { it.value <= 100000 }
            .values
            .sum()
    }

    fun part2(input: List<String>): Long {
        val total = 70000000
        val spaceRequired = 30000000
        val result = getSummedUpDirSizes(input)
        val rootSize = result["/"] ?: error("root should exist")
        val spaceNeeded = spaceRequired - (total - rootSize)
        return result
            .filter { it.value >= spaceNeeded }
            .values
            .min()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
