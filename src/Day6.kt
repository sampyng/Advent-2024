import java.io.File
class Day6 : Day {
    private lateinit var input: String
    private lateinit var map: MutableList<String>
    private lateinit var guard: Pair<Int, Int>
    private lateinit var originalGuard: Pair<Int, Int>
    private lateinit var direction: Direction

    private fun init() {
        input = File("src/res/input 6.txt").readText()
        map = input.split("\n").toMutableList()
        val y = map.indexOfFirst { it.contains("^") }
        val x = map[y].indexOfFirst { it == '^' }
        guard = x to y
        originalGuard = guard
        direction = Direction.NORTH
    }

    override fun getAnswer1(): Any {
        init()
        try {
            while(true) {
                map[guard.second] = map[guard.second].replaceCharAtIndex(guard.first ,'X')
                checkObstacle()
                checkObstacle()
                guard = nextStep(guard)
            }
        } catch (_: IndexOutOfBoundsException) {}

        return map.sumOf { line -> line.count { it == 'X' } }
    }

    override fun getAnswer2(): Any {
        val maxSteps = 10000
        var total = 0

        init()
        map.forEachIndexed { index1, line ->
            line.forEachIndexed { index2, char ->
                if (char != '#' && char != '^' ) {
                    var step = setup(index1, index2)
                    try {
                        while(step <= maxSteps) {
                            checkObstacle()
                            checkObstacle()
                            guard = nextStep(guard)
                            step++
                        }
                    } catch (_: IndexOutOfBoundsException) { }

                    if (step >= maxSteps) {
                        total++
                    }
                    reset(index1, index2)
                }
            }
        }

        return total
    }

    private fun checkObstacle() {
        val checkNext = nextStep(guard)
        if (map[checkNext.second][checkNext.first] == '#') {
            turnRight()
        }
    }

    private fun setup(index1: Int, index2: Int): Int {
        val step = 0
        map[index1] = map[index1].replaceCharAtIndex(index2, '#')
        return step
    }

    private fun reset(index1: Int, index2: Int) {
        map[index1] = map[index1].replaceCharAtIndex(index2, '.')
        guard = originalGuard
        direction = Direction.NORTH
    }

    private fun nextStep(current: Pair<Int, Int>): Pair<Int, Int> {
        return when (direction) {
            Direction.NORTH -> current.first to current.second - 1
            Direction.SOUTH -> current.first to current.second + 1
            Direction.EAST -> current.first + 1 to current.second
            Direction.WEST -> current.first - 1 to current.second
        }
    }

    private fun turnRight() {
        direction = when (direction) {
            Direction.NORTH -> Direction.EAST
            Direction.SOUTH -> Direction.WEST
            Direction.EAST -> Direction.SOUTH
            Direction.WEST -> Direction.NORTH
        }
    }

    private fun String.replaceCharAtIndex(index: Int, newChar: Char): String {
        return substring(0, index) + newChar + substring(index + 1)
    }

    private enum class Direction {
        NORTH, SOUTH, EAST, WEST
    }
}

