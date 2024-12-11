import java.io.File

class Day11 : Day {
    private val input = File("src/res/input 11.txt").readText()

    override fun getAnswer1(): Any {
        val stones = input.split(" ").map { it.toLong() }
        var stonesMap = stones.associateWith { 1L }
        repeat(25) {
            stonesMap = blink(stonesMap)
        }
        return stonesMap.values.sum()
    }

    override fun getAnswer2(): Any {
        val stones = input.split(" ").map { it.toLong() }
        var stonesMap = stones.associateWith { 1L }
        repeat(75) {
            stonesMap = blink(stonesMap)
        }
        return stonesMap.values.sum()
    }

    private fun blink(stonesMap: Map<Long, Long>): Map<Long, Long> {
        val newMap = mutableMapOf<Long, Long>()
        stonesMap.keys.forEach { stone ->
            val toString = stone.toString()
            val newStones = when {
                stone == 0L -> listOf(1L)
                toString.length % 2 == 0 -> listOf(
                    toString.substring(0, toString.length / 2).toLong(),
                    toString.substring(toString.length / 2).toLong()
                )

                else -> listOf(stone * 2024)
            }
            newStones.forEach { newMap[it] = newMap.getOrDefault(it, 0) + stonesMap[stone]!! }
        }
        return newMap
    }
}

