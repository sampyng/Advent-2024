import java.io.File
import kotlin.math.floor
import kotlin.math.pow

class Day17 : Day {
    private val input = File("src/res/input 17.txt").readText()
    private val lines = input.split("\n")
    private val regex = Regex("\\d+")
    private var registerA: Double = 0.0
    private var registerB: Double = 0.0
    private var registerC: Double = 0.0
    private lateinit var program: List<Int>

    override fun getAnswer1(): Any {
        init()

        return answer1()
    }

//    override fun getAnswer2(): Any {
//        init()
//        var i = 1.0
//        while (true) {
//            registerA = 4.0000024188692E13 + i  //4.0000645558289E13
//            println(registerA)
//            if (program.joinToString(",") { it.toString() } != answer1()) i++
//            else break
//        }
//
//        return registerA.toInt()
//    }

    private fun answer1(): String {
        var i = 0
        val output = mutableListOf<Int>()
        while (i in program.indices) {
            if (program[i] == 3) {
                if (registerA != 0.0) {
                    i = program[i + 1] - 2
                }
            } else {
                operate(program[i], program[i + 1])?.let { output.add(it) }
            }
            i += 2
        }

        return output.joinToString(",") { it.toString() }
    }

    override fun getAnswer2(): Any {
        // Part 1 each step:
        // B= A % 8 XOR 3 XOR A/32 XOR 5
        // A = A / 8
        // Print B % 8

        init()
        registerA = 0.0

//        var i = program.size - 1
//        val output = mutableListOf<Int>()
//        while (output.size != program.size) {
//            if (i == -1) {
//                i = program.size - 1
//            } else {
//                operate2(program[i - 1], program[i], program[program.size - 1 - output.size])?.let { output.add(it) }
//            }
//            i -= 2
//        }

//        println(output.reversed().joinToString(",") { it.toString() })
//        println(program.joinToString(",") { it.toString() })

        for (i in program.size - 1 downTo 0) {
            val expectedValue = program[i]
            var a = registerA
            var b = (a % 8).toInt() xor 3
            var c = (a / b).toInt()
            b = b xor c xor 5
            while (b % 8 != expectedValue) {
                a++
                b = (a % 8).toInt() xor 3
                c = (a / b).toInt()
                b = b xor c xor 5
            }
            registerA = a * 8.0
        }
        registerA /= 8.0
        println(program.joinToString(",") { it.toString() })
        println(String.format("%.0f", registerA))

        registerB = 0.0
        registerC = 0.0
        println(answer1())


        return String.format("%.0f", registerA)
    }

    private fun init() {
        registerA = regex.find(lines[0])?.value?.toDouble() ?: 0.0
        registerB = regex.find(lines[1])?.value?.toDouble() ?: 0.0
        registerC = regex.find(lines[2])?.value?.toDouble() ?: 0.0
        program = regex.findAll(lines[4]).map { it.value.toInt() }.toList()
    }

    private fun operate(opcode: Int, operand: Int): Int? {
        val combo = when (operand) {
            0, 1, 2, 3 -> operand.toDouble()
            4 -> registerA
            5 -> registerB
            6 -> registerC
            7 -> throw IllegalArgumentException("7 is reserved")
            else -> throw IllegalArgumentException("Invalid operand $operand")
        }

        when (opcode) {
            0 -> registerA = floor(registerA / 2.0.pow(combo))
            1 -> registerB = (registerB.toInt() xor operand).toDouble()
            2 -> registerB = combo % 8
            4 -> registerB = (registerB.toInt() xor registerC.toInt()).toDouble()
            5 -> return (combo % 8).toInt()
            6 -> registerB = floor(registerA / 2.0.pow(combo))
            7 -> registerC = floor(registerA / 2.0.pow(combo))
        }
        return null
    }
}

