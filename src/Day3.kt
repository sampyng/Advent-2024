import java.io.File

class Day3: Day {
    private val input: String = File("src/res/input 3.txt").readText()
    private val regexNumbers = Regex("\\d+")

    override fun getAnswer1(): Any {
        val regex = Regex("mul\\(\\d+,\\d+\\)")

        val numbers = mutableListOf<List<Int>>()
        regex.findAll(input).forEach { result ->
            val pairs = mutableListOf<Int>()
            regexNumbers.findAll(result.value).forEach { result2 -> pairs.add(result2.value.toInt()) }
            numbers.add(pairs)
        }

        return getTotal(numbers)
    }

    override fun getAnswer2(): Any {
        val regex = Regex("mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\)")
        var addUp = true

        val numbers = mutableListOf<List<Int>>()
        regex.findAll(input).forEach { result ->
            if (result.value == "do()") addUp = true
            else if (result.value == "don't()") addUp = false
            else if (addUp) {
                val pairs = mutableListOf<Int>()
                regexNumbers.findAll(result.value).forEach { result2 -> pairs.add(result2.value.toInt()) }
                numbers.add(pairs)
            }
        }

        return getTotal(numbers)
    }

    private fun getTotal(numbers: List<List<Int>>): Int {
        var total = 0
        numbers.forEach {
            total += it[0] * it[1]
        }
        return total
    }
}