import java.io.File
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.pow

class Day8 : Day {
    private val input = File("src/res/input 8.txt").readText()
    private val map = input.split("\n")
    private val locations = mutableMapOf<Char, MutableList<Pair<Int, Int>>>().apply {
        map.forEachIndexed { y, s ->
            for (x in s.indices) {
                if (s[x] != '.') {
                    if (this.contains(s[x])) {
                        this[s[x]]!!.add(x to y)
                    } else {
                        this[s[x]] = mutableListOf(x to y)
                    }
                }
            }
        }
    }

    override fun getAnswer1(): Any {
        val antiNodeSet = mutableSetOf<Pair<Int, Int>>()
        locations.values.forEach {
            for (i in it.indices) {
                for (j in it.indices) {
                    if (it[i] != it[j]) {
                        val antiNode = getLocationOfAntiNode(it[i], it[j])
                        try {
                            map[antiNode.first][antiNode.second]
                            antiNodeSet.add(antiNode)
                        } catch (_: IndexOutOfBoundsException) { }
                    }
                }
            }
        }
        return antiNodeSet.size
    }

    override fun getAnswer2(): Any {
        val antiNodeSet = mutableSetOf<Pair<Int, Int>>()
        locations.values.forEach {
            for (i in it.indices) {
                for (j in it.indices) {
                    if (it[i] != it[j]) {
                        var keepRunning = true
                        var a = it[i]
                        var b = it[j]
                        antiNodeSet.add(a)
                        antiNodeSet.add(b)
                        while (keepRunning) {
                            val antiNode = getLocationOfAntiNode(a,b)
                            try {
                                map[antiNode.first][antiNode.second]
                                antiNodeSet.add(antiNode)

                                a = b
                                b = antiNode
                            } catch (_: IndexOutOfBoundsException) {
                                keepRunning = false
                            }
                        }
                    }
                }
            }
        }
        return antiNodeSet.size
    }

    private fun getLocationOfAntiNode(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
        return b.first + (b.first - a.first) to b.second + (b.second - a.second)
    }
}

