import java.io.File
import kotlin.math.round

class Day13 : Day {
    private val input = File("src/res/input 13.txt").readText()
    private val machinesInput = input.split("\n\n")

    override fun getAnswer1(): Any {
        val machines = machinesInput.map { Machine(it) }

        return calculate(machines)
    }

    override fun getAnswer2(): Any {
        val machines = machinesInput.map { Machine2(it) }

        return String.format("%.0f", calculate2(machines))
    }

    private fun calculate(machines: List<Machine>) = machines.sumOf {
        with(it) {
            val bt = round((prize.second - buttonA.second * prize.first / buttonA.first) / (buttonB.second - buttonA.second * buttonB.first / buttonA.first))
            val at = round((prize.first - buttonB.first * bt) / buttonA.first)

            if (buttonA.first * at + buttonB.first * bt == prize.first && buttonA.second * at + buttonB.second * bt == prize.second) {
                if (at in 0.0..100.0 && bt in 0.0..100.0) {
                    (at * 3).toInt() + bt.toInt()
                } else {
                    0
                }
            } else {
                0
            }
        }
    }

    private fun calculate2(machines: List<Machine2>) = machines.sumOf {
        with(it) {
            val bt = round((newPrize.second - buttonA.second * newPrize.first / buttonA.first) / (buttonB.second - buttonA.second * buttonB.first / buttonA.first))
            val at = round((newPrize.first - buttonB.first * bt) / buttonA.first)

            if (buttonA.first * at + buttonB.first * bt == newPrize.first && buttonA.second * at + buttonB.second * bt == newPrize.second) {
                if (at >= 0 && bt >= 0) {
                    at * 3 + bt
                } else {
                    0.0
                }
            } else {
                0.0
            }
        }
    }

    private open class Machine(input: String) {
        val buttonA: Pair<Double, Double>
        val buttonB: Pair<Double, Double>
        val prize: Pair<Double, Double>

        init {
            val regex = Regex("\\d+")
            val lines = input.lines()

            val a = regex.findAll(lines[0]).toList()
            val b = regex.findAll(lines[1]).toList()
            val p = regex.findAll(lines[2]).toList()
            buttonA = a[0].value.toDouble() to a[1].value.toDouble()
            buttonB = b[0].value.toDouble() to b[1].value.toDouble()
            prize = p[0].value.toDouble() to p[1].value.toDouble()
        }

        override fun toString(): String = "ButtonA:$buttonA ButtonB:$buttonB Prize:$prize"
    }


    private class Machine2(input: String) : Machine(input) {
        val newPrize = prize.first + 10000000000000 to prize.second + 10000000000000

        override fun toString(): String = "ButtonA:$buttonA ButtonB:$buttonB Prize:$newPrize"
    }
}

