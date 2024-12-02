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
        val check = levelCheck(levels)
        if (check.first && check.second) {
            return Pair(true, true)
        }
        levels.forEachIndexed { i, _ ->
            val newLevels = levels.toMutableList().apply { removeAt(i) }
            val newCheck = levelCheck(newLevels)
            if (newCheck.first && newCheck.second) {
                return Pair(true, true)
            }
        }
        return Pair(check.first, check.second)
    }
}