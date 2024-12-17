import java.io.File
import kotlin.math.abs

typealias GridPosition = Pair<Int, Int>
typealias Barrier = Set<GridPosition>

private const val MAX_SCORE = 999999999

class Day16 : Day {
    private val input = File("src/res/input 16.txt").readText()
    private lateinit var start: Pair<Int, Int>
    private lateinit var end: Pair<Int, Int>
    private val barriers = mutableListOf<Barrier>()
    private val grid = input.split("\n").mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            when (c) {
                '#' -> barriers.add(setOf(x to y))
                'S' -> start = x to y
                'E' -> end = x to y
                else -> {}
            }
        }
    }

    override fun getAnswer1(): Any {
        val squareGrid = SquareGrid(grid[0].size, grid.size, barriers)
        val (path, cost) = aStarSearch(start, end, squareGrid)

        return cost
    }

    override fun getAnswer2(): Any {
        val walkedBefore = mutableSetOf<GridPosition>()
        var firstPathSize = 0
        var isSet = false

        while (true) {
            val squareGrid = SquareGrid(grid[0].size, grid.size, barriers, walkedBefore)
            val (path, cost) = aStarSearch(start, end, squareGrid)
            if (!isSet) {
                firstPathSize = path.size
                isSet = true
            }
            if (path.size > firstPathSize || walkedBefore == path.toSet()) {
                break
            }
            walkedBefore.addAll(path)
        }

        return walkedBefore.size
    }

    abstract class Grid(private val barriers: List<Barrier>, private val walkedBefore: Set<GridPosition>) {

        open fun heuristicDistance(start: GridPosition, finish: GridPosition): Int {
            val dx = abs(start.first - finish.first)
            val dy = abs(start.second - finish.second)
            return (dx + dy) + (-2) * minOf(dx, dy)
        }

        fun inBarrier(position: GridPosition) = barriers.any { it.contains(position) }

        private fun inWalkedBefore(position: GridPosition) = walkedBefore.contains(position)

        abstract fun getNeighbours(position: GridPosition): List<GridPosition>

        open fun moveCost(last: GridPosition, from: GridPosition, to: GridPosition): Int {
            val isSameDirection = from.first - last.first to from.second - last.second == to.first - from.first to to.second - from.second
            return if (isSameDirection) 1 else 1001
        }
    }

    class SquareGrid(width: Int, height: Int, barriers: List<Barrier>, walkedBefore: Set<GridPosition> = emptySet()) :
        Grid(barriers, walkedBefore) {

        private val heightRange: IntRange = (0 until height)
        private val widthRange: IntRange = (0 until width)

        private val validMoves = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

        override fun getNeighbours(position: GridPosition): List<GridPosition> = validMoves
            .map { GridPosition(position.first + it.first, position.second + it.second) }
            .filter { inGrid(it) }
            .filterNot { inBarrier(it) }

        private fun inGrid(it: GridPosition) = (it.first in widthRange) && (it.second in heightRange)
    }


    /**
     * Implementation of the A* Search Algorithm to find the optimum path between 2 points on a grid.
     *
     * The Grid contains the details of the barriers and methods which supply the neighboring vertices and the
     * cost of movement between 2 cells.  Examples use a standard Grid which allows movement in 8 directions
     * (i.e. includes diagonals) but alternative implementation of Grid can be supplied.
     *
     */
    fun aStarSearch(start: GridPosition, finish: GridPosition, grid: Grid): Pair<List<GridPosition>, Int> {

        /**
         * Use the cameFrom values to Backtrack to the start position to generate the path
         */
        fun generatePath(currentPos: GridPosition, cameFrom: Map<GridPosition, GridPosition>): List<GridPosition> {
            val path = mutableListOf(currentPos)
            var current = currentPos
            while (cameFrom.containsKey(current)) {
                current = cameFrom.getValue(current)
                path.add(0, current)
            }
            return path.toList()
        }

        val openVertices = mutableSetOf(start)
        val closedVertices = mutableSetOf<GridPosition>()
        val costFromStart = mutableMapOf(start to 0)
        val estimatedTotalCost = mutableMapOf(start to grid.heuristicDistance(start, finish))

        val cameFrom = mutableMapOf<GridPosition, GridPosition>()  // Used to generate path by back tracking

        while (openVertices.size > 0) {

            val currentPos = openVertices.minBy { estimatedTotalCost.getValue(it) }!!

            // Check if we have reached the finish
            if (currentPos == finish) {
                // Backtrack to generate the most efficient path
                val path = generatePath(currentPos, cameFrom)
                return Pair(path, estimatedTotalCost.getValue(finish)) // First Route to finish will be optimum route
            }

            // Mark the current vertex as closed
            openVertices.remove(currentPos)
            closedVertices.add(currentPos)

            grid.getNeighbours(currentPos)
                .filterNot { closedVertices.contains(it) }  // Exclude previous visited vertices
                .forEach { neighbour ->
                    val last = cameFrom[currentPos] ?: (currentPos.first - 1 to currentPos.second)
                    val score = costFromStart.getValue(currentPos) + grid.moveCost(last, currentPos, neighbour)
                    if (score < costFromStart.getOrDefault(neighbour, MAX_SCORE)) {
                        if (!openVertices.contains(neighbour)) {
                            openVertices.add(neighbour)
                        }
                        cameFrom[neighbour] = currentPos
                        costFromStart[neighbour] = score
                        estimatedTotalCost[neighbour] = score + grid.heuristicDistance(neighbour, finish)
                    }
                }
        }

        throw IllegalArgumentException("No Path from Start $start to Finish $finish")
    }
}

