import java.io.File

class Day5: Day {
    private val input: String = File("src/res/input 5.txt").readText()
    private val sections = input.split("\n\n")
    private val rules = sections[0].split("\n")
    private val updates = sections[1].split("\n")
    private val ruleMap = mutableMapOf<String, MutableList<String>>()

    init {
        rules.forEach { rule ->
            val inputs = rule.split("|")
            if (ruleMap[inputs[0]] == null) {
                ruleMap[inputs[0]] = mutableListOf(inputs[1])
            } else {
                ruleMap[inputs[0]]!!.add(inputs[1])
            }
        }
    }

    override fun getAnswer1(): Any {
        return updates.sumOf { update ->
            var isRightOrder = true
            val pages = update.split(",")
            pages.forEach{
                ruleMap[it]?.forEach { nextPage ->
                    if (update.contains(nextPage) && update.indexOf(nextPage) < update.indexOf(it)) {
                        isRightOrder = false
                    }
                }
            }
            if (isRightOrder)
                pages[(pages.size - 1)/2].toInt()
            else
                0
        }
    }

    override fun getAnswer2(): Any {
        val answer1 = getAnswer1()
        val total = updates.sumOf { update ->
            val pages = update.split(",")
            var isReady = false
            var pagesToReorder = pages
            while (!isReady) {
                val result = applyRules(pagesToReorder)
                pagesToReorder = result.first
                isReady = result.second
            }
            pagesToReorder[(pagesToReorder.size - 1)/2].toInt()
        }
        return total - answer1 as Int
    }

    private fun applyRules(pages: List<String>): Pair<List<String>, Boolean> {
        pages.forEach {
            ruleMap[it]?.forEach { nextPage ->
                if (pages.contains(nextPage) && pages.indexOf(nextPage) < pages.indexOf(it)) {
                    val newPages = pages.toMutableList().apply {
                        removeAt(pages.indexOf(it))
                        add(pages.indexOf(nextPage), it)
                    }
                    return newPages to false
                }
            }
        }
        return pages to true
    }
}