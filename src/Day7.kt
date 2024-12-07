import java.io.File
import java.math.BigInteger
import kotlin.math.pow

class Day7 : Day {
    private val input = File("src/res/input 7.txt").readText()
    private val lines = input.split("\n")
    private val entries = lines.map { line ->
        val entry = line.split(": ")
        entry[0].toBigInteger() to entry[1].split(" ").map { it.toBigInteger() }
    }

    override fun getAnswer1(): Any {
        return getTotal(2)
    }

    override fun getAnswer2(): Any {
        return getTotal(3)
    }

    private fun getTotal(operatorCount: Int): BigInteger {
        return entries.sumOf { entry ->
            val possibility = operatorCount.toDouble().pow(entry.second.size - 1).toInt()
            (0..<possibility).forEach { i ->
                var total = 0.toBigInteger()
                val binary = i.toString(operatorCount).padStart(entry.second.size, '0')
                for (j in binary.indices) {
                    when (binary[j]) {
                        '0' -> total += entry.second[j]
                        '1' -> total *= entry.second[j]
                        '2' -> total = "$total${entry.second[j]}".toBigInteger()
                    }
                }
                if (total == entry.first) {
                    return@sumOf entry.first
                }
            }
            return@sumOf 0.toBigInteger()
        }
    }
}

