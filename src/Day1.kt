import java.io.File
import kotlin.math.abs

class Day1: Day{
    private val list1 = mutableListOf<Int>()
    private val list2 = mutableListOf<Int>()

    init {
        val text = File("src/res/input 1.txt").readText()
        text.split("\n").forEach { line ->
            val entry = line.split("   ")
            list1.add(entry[0].toInt())
            list2.add(entry[1].toInt())
        }
        list1.sort()
        list2.sort()
    }

    override fun getAnswer1(): Any {
        var distance = 0

        list1.forEachIndexed { i, s ->
            distance += abs(s-list2[i])
        }
        return distance
    }

    override fun getAnswer2(): Any {
        var similarity = 0
        list1.forEach { list1Entry ->
            similarity += list1Entry * list2.count { it == list1Entry }
        }
        return similarity
    }
}