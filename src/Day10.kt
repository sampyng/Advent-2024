import java.io.File
import java.math.BigInteger

class Day10 : Day {
    private val input = File("src/res/input 10.txt").readText()
    private val map = input.split("\n")
    private val directions = listOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)

    override fun getAnswer1(): Any {
        val trails: MutableList<MutableList<Pair<Int, Int>>> = calculateTrails()

        return trails.groupBy { it[0] }.values.sumOf { itTrails ->
            itTrails.groupBy { it[it.size - 1] }.keys.count()
        }
    }

    override fun getAnswer2(): Any {
        val trails: MutableList<MutableList<Pair<Int, Int>>> = calculateTrails()

        return trails.count()
    }

    private fun calculateTrails(): MutableList<MutableList<Pair<Int, Int>>> {
        val trails: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == '0') { // trailhead
                    // start searching for trails
                    trails.addAll(searchPath(x to y, mutableListOf(mutableListOf(x to y))))
                }
            }
        }
        trails.removeIf { it.size != 10 }
        return trails
    }

    private fun searchPath(origin: Pair<Int, Int>, route: MutableList<MutableList<Pair<Int, Int>>>): List<MutableList<Pair<Int, Int>>> {
        val originHeight = map[origin.second][origin.first].toString().toInt()
        if (originHeight == 9) {
            return route
        }
        var foundOnce = false
        val copy = route[route.size - 1].map { it.first to it.second }
        for (direction in directions) {
            val next = origin.first + direction.first to origin.second + direction.second
            try {
                if (map[next.second][next.first].toString().toInt() == originHeight + 1) {
                    if (!foundOnce) {
                        route[route.size - 1].add(next)
                        foundOnce = true
                    } else {
                        val newCopy = copy.map { it.first to it.second }.toMutableList()
                        route.add(newCopy.apply { add(next) })
                    }
                    searchPath(next, route)
                }
            } catch (_: IndexOutOfBoundsException) {}
        }
        return route
    }
}

