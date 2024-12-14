import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


class Day14 : Day {
    private val input = File("src/res/input 14.txt").readText()
    private val wide = 101
    private val tall = 103

    override fun getAnswer1(): Any {
        val robots = input.split("\n").map { Robot(it) }
        val steps = 100
        move(robots, steps)

        return safetyFactor(robots)
    }

    private fun safetyFactor(robots: List<Robot>): Int {
        val quadrants = mutableListOf(0, 0, 0, 0)
        val halfX = (wide - 1) / 2
        val halfY = (tall - 1) / 2
        robots.forEach {
            with(it) {
                if (pos.first != halfX && pos.second != halfY) {
                    when {
                        pos.first < halfX && pos.second < halfY -> quadrants[0] += 1
                        pos.first > halfX && pos.second < halfY -> quadrants[1] += 1
                        pos.first > halfX && pos.second > halfY -> quadrants[2] += 1
                        pos.first < halfX && pos.second > halfY -> quadrants[3] += 1
                    }
                }
            }
        }

        return quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3]
    }

    override fun getAnswer2(): Any {
        val safetyFactors = mutableMapOf<Int, Int>()
        for (i in 0 .. 11000) {
            val robots = input.split("\n").map { Robot(it) }
            move(robots, i)
            safetyFactors[i] = safetyFactor(robots)
        }
        val step = safetyFactors.minBy { it.value }.key
        val robots = input.split("\n").map { Robot(it) }
        move(robots, step)
        drawToBitmap(robots, step)

        return step
    }

    private fun drawToBitmap(robots: List<Robot>, steps: Int) {
        val img = BufferedImage(wide, tall, BufferedImage.TYPE_INT_RGB)

        for (rc in 0..<tall) {
            for (cc in 0..<wide) {
                img.setRGB(cc, rc, Color.BLACK.rgb)
            }
        }
        robots.forEach {
            img.setRGB(it.pos.first, it.pos.second, Color.WHITE.rgb)
        }
        val outputfile = File("out/day14/steps$steps.jpg").apply { createNewFile() }
        ImageIO.write(img, "jpg", outputfile)
    }

    private fun move(robots: List<Robot>, sec: Int) {
        robots.forEach {
            with(it) {
                var newX = (pos.first + vector.first * sec) % wide
                var newY = (pos.second + vector.second * sec) % tall

                if (newX < 0) newX += wide
                if (newY < 0) newY += tall
                pos = newX to newY
            }
        }
    }

    private class Robot(line: String) {
        var pos: Pair<Int, Int>
        val vector: Pair<Int, Int>

        init {
            val regex = Regex("-?\\d+")
            val digits = regex.findAll(line).toList()
            pos = digits[0].value.toInt() to digits[1].value.toInt()
            vector = digits[2].value.toInt() to digits[3].value.toInt()
        }
    }
}

