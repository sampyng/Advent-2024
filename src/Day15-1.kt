import java.io.File

class `Day15-1` : Day {
    private val input = File("src/res/input 15-1.txt").readText()
    private val sections = input.split("\n\n")
    private val map = sections[0].split("\n")
    private val walls = map.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == '#') {
                x to y
            } else null
        }
    }.flatten().filterNotNull()
    private val boxes = map.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == 'O') {
                x to y
            } else null
        }
    }.flatten().filterNotNull().toMutableList()
    private var robot = map.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == '@') {
                x to y
            } else null
        }
    }.flatten().filterNotNull()[0]
    private val actions = sections[1].split("\n").joinToString(separator = "") { it }
    private val directions = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

    override fun getAnswer1(): Any {
        actions.forEach { c ->
            val newPos = move(robot, c)
            if (boxes.contains(newPos)) {
                val doable = moveBox(newPos, c)
                if (doable) {
                    robot = newPos
                }
            } else if (walls.contains(newPos)) {
            } else {
                robot = newPos
            }
//            println("--- Move $c ----")
//            printStep(walls, boxes, robot)
        }

        return boxes.sumOf {
            100 * it.second + it.first
        }
    }

    override fun getAnswer2(): Any {
        return 0
    }

    private fun move(current: Pair<Int, Int>, nextMove: Char): Pair<Int, Int> {
        return when (nextMove) {
            '^' -> current.first + directions[0].first to current.second + directions[0].second
            '>' -> current.first + directions[1].first to current.second + directions[1].second
            'v' -> current.first + directions[2].first to current.second + directions[2].second
            '<' -> current.first + directions[3].first to current.second + directions[3].second
            else -> current
        }
    }

    private fun moveBox(current: Pair<Int, Int>, nextMove: Char): Boolean {
        val newPos = move(current, nextMove)
        if (walls.contains(newPos)) {
            return false
        } else if (!boxes.contains(newPos)) {
            boxes[boxes.indexOf(current)] = newPos
            return true
        } else {
            val doable = moveBox(newPos, nextMove)
            if (doable) {
                boxes[boxes.indexOf(current)] = newPos
            }
            return doable
        }
    }

    private fun printStep(walls: List<Pair<Int, Int>>, boxes: List<Pair<Int, Int>>, robot: Pair<Int, Int>) {
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (walls.contains(x to y)) {
                    print('#')
                } else if (boxes.contains(x to y)) {
                    print('O')
                } else if (robot.first == x && robot.second == y) {
                    print('@')
                } else {
                    print('.')
                }
            }
            println()
        }
    }
}

