import java.io.File
import kotlin.math.abs

class Day2: Day {
    private val reports: List<List<Int>>

    init {
        val text = File("src/res/input 2.txt").readText()
        reports = text.split("\n").map { line ->
            line.split(" ").map { it.toInt() }
        }
    }
    override fun getAnswer1(): Any {
        var safeReports = 0
        reports.forEach { levels ->
            val pair = levelCheck(levels)
            if (pair.first && pair.second) safeReports++
        }
        return safeReports
    }

    override fun getAnswer2(): Any {
        var safeReports = 0
        reports.forEach { levels ->
            val pair = levelCheck2(levels)
            if (pair.first && pair.second) safeReports++
        }
        return safeReports
    }

    private fun levelCheck(levels: List<Int>, ): Pair<Boolean, Boolean> {
        val levelString = levels.joinToString(" ")
        var cond1 = levelString == levels.sorted().joinToString(" ") || levelString == levels.sortedDescending().joinToString(" ")
        var cond2 = true
        levels.forEachIndexed { i, it ->
            if (i != 0) {
                val diff = abs(it - levels[i - 1])
                if (diff < 1 || diff > 3) cond2 = false
            }
        }
        return Pair(cond1, cond2)
    }

    private fun levelCheck2(levels: List<Int>): Pair<Boolean, Boolean> {
        var badLevel = -1
        val directions = levels.mapIndexed { i, it ->
            if (i != 0) {
                Pair(it > levels[i - 1], i)
            } else null
        }.groupingBy { it?.first }.eachCount()
        if (directions[true] == 1) {
//            badLevel =
        }
        if (badLevel != -1) {
            val newLevels = levels.toMutableList().apply { removeAt(badLevel) }
            return levelCheck(newLevels)
        }
//        return Pair(cond1, true)
        return Pair(true, true)
    }
}