import java.io.File

class `Day15-2` : Day {
    private val input = File("src/res/input 15-2-test.txt").readText()
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
            if (c == '[') {
                Box(x to y)
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
    private val boxDirections =
        listOf(listOf(-1 to -1, 0 to -1, 1 to -1), listOf(1 to 0), listOf(-1 to 1, 0 to 1, 1 to 1), listOf(-1 to 0))

    override fun getAnswer1(): Any {
        return 0
    }

    override fun getAnswer2(): Any {
        actions.forEach { c ->
            val newPos = move(robot, c)
            if (boxes.map { it.pos == newPos || it.pos2 == newPos }.contains(true)) {
                val doable = checkMove(newPos, c)
                if (doable) {
                    moveBox(newPos, c)
                    robot = newPos
                }
            } else if (walls.contains(newPos)) {
            } else {
                robot = newPos
            }
            println("--- Move $c ----")
            printStep(walls, boxes, robot)
        }

        return boxes.sumOf {
            100 * it.pos.second + it.pos.first
        }
    }

    private fun move(current: Pair<Int, Int>, direction: Char): Pair<Int, Int> {
        return when (direction) {
            '^' -> current.first + directions[0].first to current.second + directions[0].second
            '>' -> current.first + directions[1].first to current.second + directions[1].second
            'v' -> current.first + directions[2].first to current.second + directions[2].second
            '<' -> current.first + directions[3].first to current.second + directions[3].second
            else -> current
        }
    }

    private fun checkMove(current: Pair<Int, Int>, direction: Char): Boolean {
        if (walls.contains(current)) {
            return false
        } else {
            if (!boxes.map { it.pos == current || it.pos2 == current }.contains(true)) {
                return true
            } else {
                var index = boxes.indexOf(Box(current))
                if (index == -1) index = boxes.indexOf(Box(current.first - 1 to current.second))

                lateinit var nextBoxes: List<Pair<Int, Int>>
                when (direction) {
                    '^' -> nextBoxes = boxDirections[0]
                    '>' -> nextBoxes = boxDirections[1]
                    'v' -> nextBoxes = boxDirections[2]
                    '<' -> nextBoxes = boxDirections[3]
                }
                return nextBoxes.map {
                    val next = boxes[index].pos.first + it.first to boxes[index].pos.second + it.second
                    val nextIndex = boxes.indexOf(Box(next))
                    if (nextIndex != -1) {
                        checkMove(next, direction)
                    } else {
                        true
                    }
                }.contains(false).not()
            }
        }
    }

    private fun moveBox(current: Pair<Int, Int>, direction: Char): Boolean {
        var index = boxes.indexOf(Box(current))
        if (index == -1) index = boxes.indexOf(Box(current.first - 1 to current.second))
        val currentBox = boxes[index]

        lateinit var nextBoxes: List<Pair<Int, Int>>
        when (direction) {
            '^' -> nextBoxes = boxDirections[0]
            '>' -> nextBoxes = boxDirections[1]
            'v' -> nextBoxes = boxDirections[2]
            '<' -> nextBoxes = boxDirections[3]
        }
        nextBoxes.forEach {
            val next = Box(currentBox.pos.first + it.first to currentBox.pos.second + it.second)
            if (boxes.contains(next)) {
                moveBox(next.pos, direction)
            }
        }
        currentBox.update(move(currentBox.pos, direction))
        return true
    }

    private fun printStep(walls: List<Pair<Int, Int>>, boxes: List<Box>, robot: Pair<Int, Int>) {
        for (y in map.indices) {
            var x = 0
            while (x < map[y].length) {
                if (walls.contains(x to y)) {
                    print('#')
                } else if (boxes.map { it.pos == x to y }.contains(true)) {
                    print('[')
                    print(']')
                    x++
                } else if (robot.first == x && robot.second == y) {
                    print('@')
                } else {
                    print('.')
                }
                x++
            }
            println()
        }
    }

    private class Box(var pos: Pair<Int, Int>) {
        var pos2: Pair<Int, Int> = pos.first + 1 to pos.second

        fun update(newPos: Pair<Int, Int>) {
            pos = newPos
            pos2 = newPos.first + 1 to newPos.second
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Box) return false
            return pos.first == other.pos.first && pos.second == other.pos.second
        }
    }
}

