import java.io.File

class Day4: Day {
    private val input: String = File("src/res/input 4.txt").readText()
    private val wordSearch = "XMAS"
    private val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 1 to -1, 1 to 0, 1 to 1, 0 to -1, 0 to 1)

    override fun getAnswer1(): Any {
        var total = 0
        val lines = input.split("\n")
        for ((i, line) in lines.withIndex()) {
            for (j in line.indices) {
                if (lines[i][j] == 'X') {
                    total += directions.sumOf {
                        searchWord(lines, i, j, it, 1)
                    }
                }
            }
        }
        return total
    }

    override fun getAnswer2(): Any {
        var total = 0
        val lines = input.split("\n")
        for ((i, line) in lines.withIndex()) {
            for (j in line.indices) {
                if (lines[i][j] == 'A') {
                    try {
                        total += when ("${lines[i - 1][j - 1]}${lines[i - 1][j + 1]}${lines[i + 1][j + 1]}${lines[i + 1][j - 1]}") {
                            "MMSS", "SMMS", "SSMM", "MSSM" -> 1
                            else -> 0
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                }
            }
        }
        return total
    }

    private fun searchWord(lines: List<String>, i: Int, j: Int, direction: Pair<Int, Int>, wordIndex: Int): Int {
        try {
            return if (lines[i + direction.first][j + direction.second] == wordSearch[wordIndex]) {
                if (wordIndex == wordSearch.length - 1) {
                    return 1
                } else {
                    searchWord(lines, i + direction.first, j + direction.second, direction, wordIndex + 1)
                }
            } else {
                0
            }
        } catch (_: IndexOutOfBoundsException) {
            return 0
        }
    }
}