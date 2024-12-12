import java.io.File

class Day12 : Day {
    private val input = File("src/res/input 12.txt").readText()
    private val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
    private val map = input.split("\n")
    private val regionsMap = mutableListOf<Set<Pair<Int, Int>>>().apply {
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (!this.flatten().contains(x to y)) {
                    val linkedTiles = mutableSetOf<Pair<Int, Int>>()
                    searchLinkedTiles(c, (x to y), linkedTiles)
                    this.add(linkedTiles)
                }
            }
        }
    }

    override fun getAnswer1(): Any {
        val fencesMap = mutableMapOf<Set<Pair<Int, Int>>, Int>()

        regionsMap.forEach { set ->
            set.forEach { p ->
                for (direction in directions) {
                    try {
                        if (map[p.second + direction.second][p.first + direction.first] != map[p.second][p.first]) {
                            fencesMap[set] = fencesMap.getOrDefault(set, 0) + 1
                        }
                    } catch (_: IndexOutOfBoundsException) {
                        fencesMap[set] = fencesMap.getOrDefault(set, 0) + 1
                    }
                }
            }
        }
        return fencesMap.keys.sumOf {
            fencesMap.getOrDefault(it, 0) * it.size
        }
    }

    override fun getAnswer2(): Any {
        val fencesMap = mutableMapOf<Set<Pair<Int,Int>>, List<Pair<Pair<Int, Int>, Int>>>()

        regionsMap.forEach { set ->
            val fencesList = mutableListOf<Pair<Pair<Int, Int>, Int>>()

            set.forEach { p ->
                directions.forEachIndexed { i, direction ->
                    try {
                        if (map[p.second + direction.second][p.first + direction.first] != map[p.second][p.first]) {
                            fencesList.add(p to i)
                        }
                    } catch (_: IndexOutOfBoundsException) {
                        fencesList.add(p to i)
                    }
                }
            }
            fencesMap[set] = fencesList
        }
        return fencesMap.entries.sumOf { entry ->
            val listOfSet = mutableListOf<MutableSet<Pair<Pair<Int, Int>, Int>>>()
            entry.value.sortedBy { it.first.first }.sortedBy { it.first.second }.forEach {
                listOfSet.forEach { set ->
                    set.forEach { p ->
                        for (direction in directions) {
                            if (p.first.first + direction.first == it.first.first
                                && p.first.second + direction.second == it.first.second
                                && p.second == it.second) {
                                set.add(it)
                            }
                        }
                    }
                }
                if (!listOfSet.flatten().contains(it)) {
                    listOfSet.add(mutableSetOf(it))
                }
            }
            listOfSet.size * entry.key.size
        }
    }

    private fun searchLinkedTiles(c: Char, currentTile: Pair<Int, Int>, linkedTiles: MutableSet<Pair<Int, Int>>) {
        try {
            if (map[currentTile.second][currentTile.first] == c) {
                linkedTiles.add(currentTile)
                for (direction in directions) {
                    val newTile = currentTile.first + direction.first to currentTile.second + direction.second
                    if (!linkedTiles.contains(newTile)) {
                        searchLinkedTiles(c, newTile, linkedTiles)
                    }
                }
            }
        } catch (_: IndexOutOfBoundsException) { }
    }
}

