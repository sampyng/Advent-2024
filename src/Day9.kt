import java.io.File
import java.math.BigInteger

class Day9 : Day {
    private val input = File("src/res/input 9.txt").readText()

    override fun getAnswer1(): Any {
        val blocks = input.toBlocks()
        val sortedBlocks = blocks.sort()
        return sortedBlocks.checksum()
    }

    override fun getAnswer2(): Any {
        val blocks = input.toBlocks()
        val sortedBlocks = blocks.sort2()
        return sortedBlocks.checksum()
    }

    private fun String.toBlocks(): MutableList<String> {
        var blocks = mutableListOf<String>()
        var id = 0

        forEachIndexed { i, character ->
            if (i % 2 == 0) {
                val element = id.toString()
                repeat(character.toString().toInt()) {
                    blocks.add(element)
                }
                id++
            } else {
                repeat(character.toString().toInt()) {
                    blocks.add(".")
                }
            }
        }
        return blocks
    }

    private fun MutableList<String>.sort(): List<String> {
        for (i in this.size -1 downTo 0) {
            val firstDot = this.indexOfFirst { it == "." }
            if (firstDot > i) {
                break
            }
            val temp = this[i]
            this[i] = this[firstDot]
            this[firstDot] = temp
        }
        return this
    }

    private fun MutableList<String>.sort2(): List<String> {
        for (i in input.length -1 downTo 0) {
            if (i % 2 == 0) {
                val value = input[i].toString().toInt()
                var dotCount = 0
                val temp = i / 2 // id
                val indexOfFirstId = this.indexOfFirst { it == temp.toString() }
                if (this.contains(".")) {
                    for (j in this.indexOfFirst { it == "." } .. indexOfFirstId) {
                        if (this[j] == ".") {
                            dotCount++
                        } else {
                            if (dotCount >= value) {
                                for (k in indexOfFirstId..<indexOfFirstId + value) {
                                    this[k] = "."
                                }
                                for (k in j - dotCount..<j - dotCount + value) {
                                    this[k] = temp.toString()
                                }
                                break
                            }
                            dotCount = 0
                        }
                    }
                }
            }
        }
        return this
    }

    private fun List<String>.checksum(): BigInteger {
        var index = BigInteger.ZERO
        val total = this.sumOf { value ->
            try {
                value.toBigInteger() * index
            } catch (_: NumberFormatException) {
                return@sumOf BigInteger.ZERO
            } finally {
                index++
            }
        }
        return total
    }
}

